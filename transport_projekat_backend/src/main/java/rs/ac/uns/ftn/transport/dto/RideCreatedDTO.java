package rs.ac.uns.ftn.transport.dto;

import lombok.Data;
import rs.ac.uns.ftn.transport.model.enumerations.RideStatus;

import java.text.SimpleDateFormat;
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
