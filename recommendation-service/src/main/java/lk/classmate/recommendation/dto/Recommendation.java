package lk.classmate.recommendation.dto;

public class Recommendation {
    private ClassPost classPost;
    private double valueScore;
    private double percentBelowAverage;
    private String reason;
    private int rank;

    public ClassPost getClassPost() { return classPost; }
    public void setClassPost(ClassPost classPost) { this.classPost = classPost; }
    public double getValueScore() { return valueScore; }
    public void setValueScore(double valueScore) { this.valueScore = valueScore; }
    public double getPercentBelowAverage() { return percentBelowAverage; }
    public void setPercentBelowAverage(double p) { this.percentBelowAverage = p; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public int getRank() { return rank; }
    public void setRank(int rank) { this.rank = rank; }
}