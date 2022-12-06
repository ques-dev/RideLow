package rs.ac.uns.ftn.transport.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.ac.uns.ftn.transport.dto.PanicDTO;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleReviewPageDTO {
    public Long totalCount;
    public Set<VehicleReviewDTO> results;
}
