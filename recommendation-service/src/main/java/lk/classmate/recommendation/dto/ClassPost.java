package lk.classmate.recommendation.dto;

public class ClassPost {
    private Long id;
    private String subject;
    private String teacherName;
    private String district;
    private String mode;
    private String grade;
    private String place;
    private double fee;

    // NEW: sent by listing-service (Service 1)
    private double averageRating;
    private long ratingCount;

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