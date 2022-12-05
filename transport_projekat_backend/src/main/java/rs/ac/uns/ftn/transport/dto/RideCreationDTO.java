package rs.ac.uns.ftn.transport.dto;

import lombok.Data;
import rs.ac.uns.ftn.transport.model.enumerations.RideStatus;

import java.util.List;
import java.util.Set;

@Data

public class RideCreationDTO {

    private Set<RouteDTO> locations;
    private Set<PassengerIdEmailDTO> passengers;
    private VehicleTypeNameDTO vehicleType;
    private Boolean babyTransport;
    private Boolean petTransport;
}
