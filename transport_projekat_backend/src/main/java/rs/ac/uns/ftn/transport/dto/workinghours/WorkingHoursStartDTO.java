package rs.ac.uns.ftn.transport.dto.workinghours;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WorkingHoursStartDTO {
    @NotNull
    private LocalDateTime start;
}