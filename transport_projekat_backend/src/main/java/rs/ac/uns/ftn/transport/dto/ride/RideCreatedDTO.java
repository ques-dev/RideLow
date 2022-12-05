package rs.ac.uns.ftn.transport.dto.ride;

import lombok.Data;
import rs.ac.uns.ftn.transport.dto.DriverIdEmailDTO;
import rs.ac.uns.ftn.transport.model.enumerations.RideStatus;

import java.time.LocalDateTime;

@Data
public class RideCreatedDTO extends RideCreationDTO {

    private Integer id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Double totalCost;
    private DriverIdEmailDTO driver;
    private Integer estimatedTimeInMinutes;
    private RideStatus status;
}
