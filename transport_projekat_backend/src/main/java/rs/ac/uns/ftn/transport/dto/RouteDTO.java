package rs.ac.uns.ftn.transport.dto;

import lombok.Data;

@Data
public class RouteDTO {

    private LocationDTO departure;
    private LocationDTO destination;
}
