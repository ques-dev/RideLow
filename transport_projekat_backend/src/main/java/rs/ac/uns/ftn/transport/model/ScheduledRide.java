package rs.ac.uns.ftn.transport.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ScheduledRide {

    private Ride ride;
    private LocalDateTime scheduledFor;
}
