package lk.classmate.recommendation.dto;

public class ClassPost {
    private Long id;
    private String subject;
    private String teacherName;
    private String district;
    private String mode;
    private double fee;

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
    public double getFee() { return fee; }
    public void setFee(double fee) { this.fee = fee; }
}