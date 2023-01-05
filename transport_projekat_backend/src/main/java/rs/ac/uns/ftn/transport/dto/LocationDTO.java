package rs.ac.uns.ftn.transport.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class LocationDTO {

    @Length(max=255,message="{maxLength}")
    @NotBlank(message="{required}")
    private String address;

    @DecimalMin(value= "-90.0",message="{latitude.minLimit}")
    @DecimalMax(value="90.0",message = "{latitude.maxLimit}")
    private Double latitude;
    @DecimalMin(value= "-180.0",message="{longitude.minLimit}")
    @DecimalMax(value="180.0",message = "{longitude.maxLimit}")
    private Double longitude;
}