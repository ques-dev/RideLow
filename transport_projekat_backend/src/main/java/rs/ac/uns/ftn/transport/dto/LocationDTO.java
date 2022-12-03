package rs.ac.uns.ftn.transport.dto;

import lombok.Data;

@Data
public class LocationDTO {
    private String address;
    private Double latitude;
    private Double longitude;
}