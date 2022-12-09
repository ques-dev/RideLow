package rs.ac.uns.ftn.transport.dto.review;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRideDTO {
    private DriverReviewDTO driverReview;
    private VehicleReviewDTO vehicleReview;
}
