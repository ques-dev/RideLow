package rs.ac.uns.ftn.transport.dto.ride;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import rs.ac.uns.ftn.transport.dto.RouteDTO;
import rs.ac.uns.ftn.transport.dto.passenger.PassengerIdEmailDTO;

import java.util.Set;

@Data
public class FavoriteRideWithoutIdDTO {

    @Length(max=255,message = "{maxLength}")
    @NotBlank(message = "{required}")
    private String favoriteName;

    @Size(min = 1, message = "{minLength}")
    @Valid
    private Set<RouteDTO> locations;

    @Size(min=1, message = "{minLength}")
    @Valid
    private Set<PassengerIdEmailDTO> passengers;

    @NotNull(message="{required}")
    private Boolean babyTransport;

    @NotNull(message="{required}")
    private Boolean petTransport;

    @Length(max=255,message = "{maxLength}")
    @NotBlank(message = "{required}")
    private String vehicleType;
}
