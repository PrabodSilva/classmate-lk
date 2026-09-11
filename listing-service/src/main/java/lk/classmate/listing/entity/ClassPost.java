package lk.classmate.listing.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "class_posts")
public class ClassPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Subject is required")
    private String subject;

    @NotBlank(message = "Teacher name is required")
    private String teacherName;

    @NotBlank(message = "District is required")
    private String district;

    @NotBlank(message = "Mode is required (Individual / Mass / Online)")
    private String mode;

    @NotBlank(message = "Grade is required")
    private String grade;

    private String place;

    @Positive(message = "Fee must be greater than 0")
    private double fee;

    // ---- Ratings: NOT stored in class_posts. Calculated from the ratings table. ----
    @Transient
    private double averageRating;

    @Transient
    private long ratingCount;

    public ClassPost() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getTeacherName() { return teacherName; }
    public void setTeacherName(String teacherName) { this.teacherName = teacherName; }
    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }
    public String getMode() { return mode; }
    public void setMode(String mode) { this.mode = mode; }
    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }
    public String getPlace() { return place; }
    public void setPlace(String place) { this.place = place; }
    public double getFee() { return fee; }
    public void setFee(double fee) { this.fee = fee; }

    public double getAverageRating() { return averageRating; }
    public void setAverageRating(double averageRating) { this.averageRating = averageRating; }
    public long getRatingCount() { return ratingCount; }
    public void setRatingCount(long ratingCount) { this.ratingCount = ratingCount; }
}