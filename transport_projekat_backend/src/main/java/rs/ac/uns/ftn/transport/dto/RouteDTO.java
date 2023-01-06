package rs.ac.uns.ftn.transport.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RouteDTO {

    @NotNull(message="{required}")
    @Valid
    private LocationDTO departure;

    @NotNull(message = "{required}")
    @Valid
    private LocationDTO destination;
}
