package lk.classmate.listing.repository;

import lk.classmate.listing.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Long> {

    // Has this student already rated this class?
    Optional<Rating> findByClassIdAndUserEmail(Long classId, String userEmail);

    // All ratings for one class
    List<Rating> findByClassId(Long classId);

    // One row per class: [classId, average stars, number of ratings]
    @Query("SELECT r.classId, AVG(r.stars), COUNT(r) FROM Rating r GROUP BY r.classId")
    List<Object[]> findStatsForAllClasses();

    // When a class is deleted, delete its ratings too
    @Transactional
    void deleteByClassId(Long classId);
}