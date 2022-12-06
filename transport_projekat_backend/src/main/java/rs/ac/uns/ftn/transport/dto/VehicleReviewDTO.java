package rs.ac.uns.ftn.transport.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.ac.uns.ftn.transport.model.Ride;
import rs.ac.uns.ftn.transport.model.User;
import rs.ac.uns.ftn.transport.model.Vehicle;
import rs.ac.uns.ftn.transport.model.VehicleReview;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleReviewDTO {
    private Integer id;
    private Vehicle vehicle;
    private User reviewer;
    private Ride currentRide;
    private String comment;
    private Integer rating;

    public VehicleReviewDTO(VehicleReview vehicleReview){
        this.id = vehicleReview.getId();
        this.vehicle = vehicleReview.getVehicle();
        this.reviewer = vehicleReview.getReviewer();
        this.currentRide = vehicleReview.getCurrentRide();
        this.comment = vehicleReview.getComment();
        this.rating = vehicleReview.getRating();
    }
}
