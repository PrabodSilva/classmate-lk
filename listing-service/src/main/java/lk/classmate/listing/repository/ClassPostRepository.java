package lk.classmate.listing.repository;

import lk.classmate.listing.entity.ClassPost;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ClassPostRepository extends JpaRepository<ClassPost, Long> {
    List<ClassPost> findBySubjectContainingIgnoreCase(String subject);
}