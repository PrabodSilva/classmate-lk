package lk.classmate.recommendation.controller;

import lk.classmate.recommendation.client.ListingClient;
import lk.classmate.recommendation.dto.ClassPost;
import org.springframework.web.bind.annotation.*;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/recommendations")
@CrossOrigin(origins = "*")
public class RecommendationController {

    private final ListingClient listingClient;

    public RecommendationController(ListingClient listingClient) {
        this.listingClient = listingClient;
    }

    // Example: /recommendations?subject=Maths
    @GetMapping
    public List<ClassPost> recommend(@RequestParam String subject) {
        List<ClassPost> classes = listingClient.getClasses(subject);
        return classes.stream()
                .sorted(Comparator.comparingDouble(ClassPost::getFee))
                .limit(3)
                .collect(Collectors.toList());
    }
}