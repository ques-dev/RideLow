package rs.ac.uns.ftn.transport.dto;

import rs.ac.uns.ftn.transport.model.*;

public class DriverReviewDTO {
    private Integer id;
    private Driver driver;
    private User reviewer;
    private Ride currentRide;
    private String comment;
    private Integer rating;

    public DriverReviewDTO(DriverReview driverReview){
        this.id = driverReview.getId();
        this.driver = driverReview.getDriver();
        this.reviewer = driverReview.getReviewer();
        this.currentRide = driverReview.getCurrentRide();
        this.comment = driverReview.getComment();
        this.rating = driverReview.getRating();
    }
}
