package rs.ac.uns.ftn.transport.dto.ride;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ScheduledRideCreationDTO extends RideCreationDTO{
    @NotNull(message="{required}")
    private LocalDateTime scheduledFor;
}
