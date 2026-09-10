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

    private final ListingClient listingClient;

    public RecommendationController(ListingClient listingClient) {
        this.listingClient = listingClient;
    }

    // Example: /recommendations?subject=Maths&grade=A/L
    @GetMapping
    public RecommendationResponse recommend(@RequestParam String subject,
                                            @RequestParam(required = false) String grade) {

        // 1. Ask Service 1 (listing-service) for matching classes
        List<ClassPost> classes = listingClient.getClasses(subject, grade);

        RecommendationResponse response = new RecommendationResponse();
        response.setSubject(subject);
        response.setGrade(grade);
        response.setTotalClassesFound(classes.size());
        response.setRecommendations(new ArrayList<>());

        if (classes.isEmpty()) {
            return response;
        }

        // 2. Market statistics — logic that only Service 2 performs
        double total = 0;
        double lowest = classes.get(0).getFee();
        double highest = classes.get(0).getFee();

        for (ClassPost c : classes) {
            total += c.getFee();
            if (c.getFee() < lowest) lowest = c.getFee();
            if (c.getFee() > highest) highest = c.getFee();
        }

        double average = total / classes.size();

        response.setAverageFee(round(average));
        response.setLowestFee(round(lowest));
        response.setHighestFee(round(highest));

        // 3. Rank cheapest first, take top 3
        List<ClassPost> top = classes.stream()
                .sorted(Comparator.comparingDouble(ClassPost::getFee))
                .limit(3)
                .toList();

        // 4. Score each pick against the market average
        List<Recommendation> recs = new ArrayList<>();
        int rank = 1;

        for (ClassPost c : top) {
            Recommendation r = new Recommendation();
            r.setClassPost(c);
            r.setRank(rank);

            double percentBelow = ((average - c.getFee()) / average) * 100;
            r.setPercentBelowAverage(round(percentBelow));

            // Value score out of 100: cheaper than average scores higher
            double score = 50 + (percentBelow / 2);
            if (score > 100) score = 100;
            if (score < 0) score = 0;
            r.setValueScore(round(score));

            r.setReason(buildReason(rank, percentBelow, c));
            recs.add(r);
            rank++;
        }

        response.setRecommendations(recs);
        return response;
    }

    private String buildReason(int rank, double percentBelow, ClassPost c) {
        StringBuilder sb = new StringBuilder();

        if (rank == 1) {
            sb.append("Best value pick");
        } else if (percentBelow > 0) {
            sb.append("Good value");
        } else {
            sb.append("Above average price");
        }

        if (percentBelow > 0) {
            sb.append(" — ").append(round(percentBelow)).append("% below market average");
        } else if (percentBelow < 0) {
            sb.append(" — ").append(round(-percentBelow)).append("% above market average");
        } else {
            sb.append(" — exactly at market average");
        }

        if ("Online".equalsIgnoreCase(c.getMode())) {
            sb.append(" · Online, no travel needed");
        } else if ("Individual".equalsIgnoreCase(c.getMode())) {
            sb.append(" · One-to-one attention");
        }

        return sb.toString();
    }

    private double round(double v) {
        return Math.round(v * 10.0) / 10.0;
    }
}