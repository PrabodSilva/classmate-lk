package lk.classmate.recommendation.client;

import lk.classmate.recommendation.dto.ClassPost;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

// Calls Tharaka's LISTING-SERVICE by its Eureka name
@FeignClient(name = "LISTING-SERVICE")
public interface ListingClient {
    @GetMapping("/classes")
    List<ClassPost> getClasses(@RequestParam(required = false) String subject);
}