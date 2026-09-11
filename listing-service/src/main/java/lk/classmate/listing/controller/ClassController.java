package lk.classmate.listing.controller;

import jakarta.validation.Valid;
import lk.classmate.listing.dto.RatingRequest;
import lk.classmate.listing.entity.ClassPost;
import lk.classmate.listing.entity.Rating;
import lk.classmate.listing.repository.ClassPostRepository;
import lk.classmate.listing.repository.RatingRepository;
import lk.classmate.listing.security.JwtUtil;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/classes")
public class ClassController {

    private final ClassPostRepository repo;
    private final RatingRepository ratingRepo;
    private final JwtUtil jwtUtil;

    public ClassController(ClassPostRepository repo,
                           RatingRepository ratingRepo,
                           JwtUtil jwtUtil) {
        this.repo = repo;
        this.ratingRepo = ratingRepo;
        this.jwtUtil = jwtUtil;
    }

    // CREATE — Teacher adds a class (validated)
    @PostMapping
    public ResponseEntity<ClassPost> addClass(@Valid @RequestBody ClassPost classPost) {
        ClassPost saved = repo.save(classPost);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // READ ALL — /classes  or filter: /classes?subject=Maths&grade=A/L
    @GetMapping
    public List<ClassPost> getClasses(@RequestParam(required = false) String subject,
                                      @RequestParam(required = false) String grade) {
        List<ClassPost> results;

        if (subject == null || subject.isBlank()) {
            results = repo.findAll();
        } else {
            results = repo.findBySubjectContainingIgnoreCase(subject);
        }

        if (grade != null && !grade.isBlank()) {
            results = results.stream()
                    .filter(c -> grade.equalsIgnoreCase(c.getGrade()))
                    .toList();
        }

        attachRatingsToAll(results);   // NEW
        return results;
    }

    // READ ONE — /classes/5
    @GetMapping("/{id}")
    public ResponseEntity<ClassPost> getClassById(@PathVariable Long id) {
        return repo.findById(id)
                .map(c -> ResponseEntity.ok(attachRatings(c)))   // NEW: includes rating
                .orElse(ResponseEntity.notFound().build());
    }

    // UPDATE — teacher edits a class
    @PutMapping("/{id}")
    public ResponseEntity<ClassPost> updateClass(@PathVariable Long id,
                                                 @Valid @RequestBody ClassPost updated) {
        return repo.findById(id)
                .map(existing -> {
                    existing.setSubject(updated.getSubject());
                    existing.setTeacherName(updated.getTeacherName());
                    existing.setDistrict(updated.getDistrict());
                    existing.setMode(updated.getMode());
                    existing.setGrade(updated.getGrade());
                    existing.setPlace(updated.getPlace());
                    existing.setFee(updated.getFee());
                    return ResponseEntity.ok(attachRatings(repo.save(existing)));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE — teacher removes a class
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClass(@PathVariable Long id) {
        if (!repo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        ratingRepo.deleteByClassId(id);   // NEW: remove its ratings too
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ===================== NEW: RATINGS =====================

    // RATE — POST /classes/5/ratings   body: {"stars": 4}
    // Needs header: Authorization: Bearer <token>
    // Each logged-in student can rate a class only ONCE.
    @PostMapping("/{id}/ratings")
    public ResponseEntity<?> rateClass(@PathVariable Long id,
                                       @RequestHeader(value = "Authorization", required = false) String authHeader,
                                       @Valid @RequestBody RatingRequest request) {

        // 1. Who is this? (checks the login token)
        String email = jwtUtil.getEmailFromHeader(authHeader);
        if (email == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Please log in to rate this class"));
        }

        // 2. Does the class exist?
        ClassPost classPost = repo.findById(id).orElse(null);
        if (classPost == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Class not found"));
        }

        // 3. Already rated?
        if (ratingRepo.findByClassIdAndUserEmail(id, email).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "You have already rated this class"));
        }

        // 4. Save. The database unique rule is a second safety net.
        try {
            ratingRepo.save(new Rating(id, email, request.getStars()));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "You have already rated this class"));
        }

        // 5. Return the class with its NEW average
        return ResponseEntity.status(HttpStatus.CREATED).body(attachRatings(classPost));
    }

    // MY RATING — GET /classes/5/my-rating
    // Returns {"loggedIn": true, "stars": 4}   (stars = 0 means not rated yet)
    @GetMapping("/{id}/my-rating")
    public Map<String, Object> getMyRating(@PathVariable Long id,
                                           @RequestHeader(value = "Authorization", required = false) String authHeader) {
        String email = jwtUtil.getEmailFromHeader(authHeader);

        int stars = 0;
        if (email != null) {
            stars = ratingRepo.findByClassIdAndUserEmail(id, email)
                    .map(Rating::getStars)
                    .orElse(0);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("loggedIn", email != null);
        result.put("stars", stars);
        return result;
    }

    // ---- helpers: fill in averageRating + ratingCount ----

    private ClassPost attachRatings(ClassPost c) {
        List<Rating> ratings = ratingRepo.findByClassId(c.getId());
        double avg = ratings.stream().mapToInt(Rating::getStars).average().orElse(0);
        c.setAverageRating(Math.round(avg * 10) / 10.0);
        c.setRatingCount(ratings.size());
        return c;
    }

    private void attachRatingsToAll(List<ClassPost> classes) {
        Map<Long, Object[]> stats = new HashMap<>();
        for (Object[] row : ratingRepo.findStatsForAllClasses()) {
            stats.put(((Number) row[0]).longValue(), row);
        }
        for (ClassPost c : classes) {
            Object[] row = stats.get(c.getId());
            if (row != null) {
                double avg = ((Number) row[1]).doubleValue();
                c.setAverageRating(Math.round(avg * 10) / 10.0);
                c.setRatingCount(((Number) row[2]).longValue());
            }
        }
    }
}