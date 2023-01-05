package rs.ac.uns.ftn.transport.dto.ride;

import lombok.Data;
import rs.ac.uns.ftn.transport.dto.RouteDTO;
import rs.ac.uns.ftn.transport.dto.passenger.PassengerIdEmailDTO;

import java.util.Set;

@Data
public class FavoriteRideWithoutIdDTO {

    private String favoriteName;
    private Set<RouteDTO> locations;
    private Set<PassengerIdEmailDTO> passengers;
    private String vehicleType;
    private Boolean babyTransport;
    private Boolean petTransport;
}
