package lk.classmate.recommendation.dto;

import java.util.List;

public class RecommendationResponse {
    private String subject;
    private String grade;
    private int totalClassesFound;
    private double averageFee;
    private double lowestFee;
    private double highestFee;
    private List<Recommendation> recommendations;

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }
    public int getTotalClassesFound() { return totalClassesFound; }
    public void setTotalClassesFound(int t) { this.totalClassesFound = t; }
    public double getAverageFee() { return averageFee; }
    public void setAverageFee(double averageFee) { this.averageFee = averageFee; }
    public double getLowestFee() { return lowestFee; }
    public void setLowestFee(double lowestFee) { this.lowestFee = lowestFee; }
    public double getHighestFee() { return highestFee; }
    public void setHighestFee(double highestFee) { this.highestFee = highestFee; }
    public List<Recommendation> getRecommendations() { return recommendations; }
    public void setRecommendations(List<Recommendation> r) { this.recommendations = r; }
}