package lk.classmate.recommendation.dto;

public class Recommendation {
    private ClassPost classPost;
    private double valueScore;          // FINAL score (60% rating + 40% price)
    private double ratingScore;         // NEW: rating part, out of 100
    private double priceScore;          // NEW: price part, out of 100
    private double percentBelowAverage;
    private String reason;
    private int rank;

    public ClassPost getClassPost() { return classPost; }
    public void setClassPost(ClassPost classPost) { this.classPost = classPost; }
    public double getValueScore() { return valueScore; }
    public void setValueScore(double valueScore) { this.valueScore = valueScore; }
    public double getRatingScore() { return ratingScore; }
    public void setRatingScore(double ratingScore) { this.ratingScore = ratingScore; }
    public double getPriceScore() { return priceScore; }
    public void setPriceScore(double priceScore) { this.priceScore = priceScore; }
    public double getPercentBelowAverage() { return percentBelowAverage; }
    public void setPercentBelowAverage(double p) { this.percentBelowAverage = p; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public int getRank() { return rank; }
    public void setRank(int rank) { this.rank = rank; }
}