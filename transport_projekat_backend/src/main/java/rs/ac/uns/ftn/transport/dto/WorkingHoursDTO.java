package rs.ac.uns.ftn.transport.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WorkingHoursDTO {
    private Integer id;
    private LocalDateTime start;
    private LocalDateTime end;
}