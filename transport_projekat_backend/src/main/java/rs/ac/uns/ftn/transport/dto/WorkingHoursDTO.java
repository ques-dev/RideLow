package rs.ac.uns.ftn.transport.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WorkingHoursDTO {
    private Integer id;

    @NotNull
    private LocalDateTime start;

    private LocalDateTime end;
}