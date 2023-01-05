package rs.ac.uns.ftn.transport.dto.ride;

import lombok.Data;
import rs.ac.uns.ftn.transport.dto.RouteDTO;
import rs.ac.uns.ftn.transport.dto.passenger.PassengerIdEmailDTO;

import java.util.Set;

@Data
public class FavoriteRideDTO extends FavoriteRideWithoutIdDTO {

    private Integer id;

}
