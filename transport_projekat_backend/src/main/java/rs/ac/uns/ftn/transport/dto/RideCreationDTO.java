package rs.ac.uns.ftn.transport.dto;

import lombok.Data;
import java.util.List;

@Data
public class RideCreationDTO {

    private List<LocationDTO> locations;
    private List<PassengerIdEmailDTO> passengers;
    private VehicleTypeNameDTO vehicleType;
    private Boolean transportsBaby;
    private Boolean transportsPet;
}
