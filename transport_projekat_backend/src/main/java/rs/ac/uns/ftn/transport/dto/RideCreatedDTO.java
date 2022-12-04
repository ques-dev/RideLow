package rs.ac.uns.ftn.transport.dto;

import lombok.Data;

import java.text.SimpleDateFormat;

@Data
public class RideCreatedDTO extends RideCreationDTO {

    private SimpleDateFormat startTime;
    private SimpleDateFormat endTime;
    private Double totalPrice;
    private DriverIdEmailDTO driver;
    private SimpleDateFormat timeEstimate;
}
