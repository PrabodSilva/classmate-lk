package lk.classmate.listing.controller;

import lk.classmate.listing.entity.ClassPost;
import lk.classmate.listing.repository.ClassPostRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/classes")
@CrossOrigin(origins = "*")
public class ClassController {

    private final ClassPostRepository repo;

    public ClassController(ClassPostRepository repo) {
        this.repo = repo;
    }

    // Teacher adds a class
    @PostMapping
    public ClassPost addClass(@RequestBody ClassPost classPost) {
        return repo.save(classPost);
    }

    // Get all classes, or filter by subject: /classes?subject=Maths
    @GetMapping
    public List<ClassPost> getClasses(@RequestParam(required = false) String subject) {
        if (subject == null || subject.isBlank()) {
            return repo.findAll();
        }
        return repo.findBySubjectContainingIgnoreCase(subject);
    }
}