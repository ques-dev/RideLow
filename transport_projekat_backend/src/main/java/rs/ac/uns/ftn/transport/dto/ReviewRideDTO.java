package rs.ac.uns.ftn.transport.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.ac.uns.ftn.transport.model.DriverReview;
import rs.ac.uns.ftn.transport.model.VehicleReview;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRideDTO {
    private DriverReviewDTO driverReview;
    private VehicleReviewDTO vehicleReview;
}
