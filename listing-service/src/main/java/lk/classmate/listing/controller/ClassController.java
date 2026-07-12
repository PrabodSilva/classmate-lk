package lk.classmate.listing.controller;

import jakarta.validation.Valid;
import lk.classmate.listing.entity.ClassPost;
import lk.classmate.listing.repository.ClassPostRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    // CREATE — Teacher adds a class (now validated)
    @PostMapping
    public ResponseEntity<ClassPost> addClass(@Valid @RequestBody ClassPost classPost) {
        ClassPost saved = repo.save(classPost);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // READ ALL — /classes  or filter: /classes?subject=Maths
    @GetMapping
    public List<ClassPost> getClasses(@RequestParam(required = false) String subject) {
        if (subject == null || subject.isBlank()) {
            return repo.findAll();
        }
        return repo.findBySubjectContainingIgnoreCase(subject);
    }

    // READ ONE — /classes/5
    @GetMapping("/{id}")
    public ResponseEntity<ClassPost> getClassById(@PathVariable Long id) {
        return repo.findById(id)
                .map(ResponseEntity::ok)
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
                    existing.setFee(updated.getFee());
                    return ResponseEntity.ok(repo.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE — teacher removes a class
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClass(@PathVariable Long id) {
        if (!repo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}