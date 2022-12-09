package rs.ac.uns.ftn.transport.dto.review;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.ac.uns.ftn.transport.dto.passenger.PassengerIdEmailDTO;
import rs.ac.uns.ftn.transport.model.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverReviewDTO {
    private Integer id;
    private Integer rating;
    private String comment;
    private PassengerIdEmailDTO passenger;

    public DriverReviewDTO(DriverReview driverReview){
        this.id = driverReview.getId();
        this.comment = driverReview.getComment();
        this.rating = driverReview.getRating();
    }
}
