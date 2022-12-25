package rs.ac.uns.ftn.transport.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class VehicleDTO {
    private Integer id;

    private Integer driverId;

    @NotBlank
    private String vehicleType;

    @NotBlank
    @Length(max = 100)
    private String model;

    @NotBlank
    @Length(max = 20)
    private String licenseNumber;

    private LocationDTO currentLocation;

    @NotNull
    @Min(1)
    @Max(20)
    private Integer passengerSeats;

    private Boolean babyTransport;

    private Boolean petTransport;
}
