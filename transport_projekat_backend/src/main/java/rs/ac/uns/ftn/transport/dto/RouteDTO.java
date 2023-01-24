package rs.ac.uns.ftn.transport.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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
