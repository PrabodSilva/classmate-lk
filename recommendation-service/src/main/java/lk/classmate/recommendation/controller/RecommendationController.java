package lk.classmate.recommendation.controller;

import lk.classmate.recommendation.client.ListingClient;
import lk.classmate.recommendation.dto.ClassPost;
import lk.classmate.recommendation.dto.Recommendation;
import lk.classmate.recommendation.dto.RecommendationResponse;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/recommendations")
public class RecommendationController {

    // ---------- Scoring settings ----------
    // Final score = 60% student rating + 40% price value
    private static final double RATING_WEIGHT = 0.6;
    private static final double PRICE_WEIGHT = 0.4;

    // Every class starts as if it already had 2 "average" (3-star) ratings.
    // - ONE lucky 5-star rating cannot beat a class with many good ratings.
    // - A class with NO ratings yet counts as neutral (3 stars), not as zero.
    private static final double PRIOR_RATING_COUNT = 2;
    private static final double PRIOR_STARS = 3.0;

    private static final String DASH = " \u2014 ";   // long dash
    private static final String DOT = " \u00B7 ";    // middle dot
    private static final String STAR = "\u2605";     // star

    private final ListingClient listingClient;

    public RecommendationController(ListingClient listingClient) {
        this.listingClient = listingClient;
    }

    // Example: /recommendations?subject=Physics&grade=A/L
    @GetMapping
    public RecommendationResponse recommend(@RequestParam String subject,
                                            @RequestParam(required = false) String grade) {

        // 1. Ask Service 1 (listing-service) for matching classes (now WITH ratings)
        List<ClassPost> classes = listingClient.getClasses(subject, grade);

        RecommendationResponse response = new RecommendationResponse();
        response.setSubject(subject);
        response.setGrade(grade);
        response.setTotalClassesFound(classes.size());
        response.setRecommendations(new ArrayList<>());

        if (classes.isEmpty()) {
            return response;
        }

        // 2. Market statistics: logic that only Service 2 performs
        double total = 0;
        double lowest = classes.get(0).getFee();
        double highest = classes.get(0).getFee();
        int ratedClasses = 0;

        for (ClassPost c : classes) {
            total += c.getFee();
            if (c.getFee() < lowest) lowest = c.getFee();
            if (c.getFee() > highest) highest = c.getFee();
            if (c.getRatingCount() > 0) ratedClasses++;
        }

        double average = total / classes.size();

        response.setAverageFee(round(average));
        response.setLowestFee(round(lowest));
        response.setHighestFee(round(highest));
        response.setRatedClasses(ratedClasses);

        // 3. Score EVERY class (not just the cheapest ones)
        List<Recommendation> scored = new ArrayList<>();

        for (ClassPost c : classes) {
            // Price score (0-100): cheaper than the market average scores higher
            double percentBelow = average > 0 ? ((average - c.getFee()) / average) * 100 : 0;
            double priceScore = clamp(50 + (percentBelow / 2));

            // Rating score (0-100): 1 star = 0, 3 stars = 50, 5 stars = 100
            double adjustedStars =
                    (PRIOR_RATING_COUNT * PRIOR_STARS + c.getAverageRating() * c.getRatingCount())
                    / (PRIOR_RATING_COUNT + c.getRatingCount());
            double ratingScore = clamp(((adjustedStars - 1) / 4) * 100);

            // Final score
            double finalScore = RATING_WEIGHT * ratingScore + PRICE_WEIGHT * priceScore;

            Recommendation r = new Recommendation();
            r.setClassPost(c);
            r.setPercentBelowAverage(round(percentBelow));
            r.setRatingScore(round(ratingScore));
            r.setPriceScore(round(priceScore));
            r.setValueScore(round(finalScore));
            scored.add(r);
        }

        // 4. Highest score first (if equal, cheaper first), keep the top 3
        List<Recommendation> top = scored.stream()
                .sorted(Comparator.comparingDouble(Recommendation::getValueScore).reversed()
                        .thenComparingDouble(rec -> rec.getClassPost().getFee()))
                .limit(3)
                .toList();

        int rank = 1;
        for (Recommendation rec : top) {
            rec.setRank(rank);
            rec.setReason(buildReason(rank, rec.getPercentBelowAverage(), rec.getClassPost()));
            rank++;
        }

        response.setRecommendations(new ArrayList<>(top));
        return response;
    }

    private String buildReason(int rank, double percentBelow, ClassPost c) {
        StringBuilder sb = new StringBuilder();
        boolean rated = c.getRatingCount() > 0;

        // Headline
        if (rank == 1) {
            sb.append("Top pick");
        } else if (rated && c.getAverageRating() >= 4.0) {
            sb.append("Highly rated");
        } else if (percentBelow > 0) {
            sb.append("Good value");
        } else {
            sb.append("Worth considering");
        }

        // Rating part
        sb.append(DASH);
        if (rated) {
            sb.append("Rated ").append(c.getAverageRating()).append(STAR)
              .append(" by ").append(c.getRatingCount())
              .append(c.getRatingCount() == 1 ? " student" : " students");
        } else {
            sb.append("No ratings yet");
        }

        // Price part
        sb.append(DOT);
        if (percentBelow > 0) {
            sb.append(round(percentBelow)).append("% below market average");
        } else if (percentBelow < 0) {
            sb.append(round(-percentBelow)).append("% above market average");
        } else {
            sb.append("at market average");
        }

        // Mode part
        if ("Online".equalsIgnoreCase(c.getMode())) {
            sb.append(DOT).append("Online, no travel needed");
        } else if ("Individual".equalsIgnoreCase(c.getMode())) {
            sb.append(DOT).append("One-to-one attention");
        }

        return sb.toString();
    }

    private double clamp(double v) {
        return Math.max(0, Math.min(100, v));
    }

    private double round(double v) {
        return Math.round(v * 10.0) / 10.0;
    }
}