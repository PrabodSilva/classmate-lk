package lk.classmate.listing.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public class RatingRequest {

    @Min(value = 1, message = "Stars must be between 1 and 5")
    @Max(value = 5, message = "Stars must be between 1 and 5")
    private int stars;

    public RatingRequest() {}

    public int getStars() { return stars; }
    public void setStars(int stars) { this.stars = stars; }
}