package rs.ac.uns.ftn.transport.dto.panic;

import rs.ac.uns.ftn.transport.dto.LocationDTO;
import rs.ac.uns.ftn.transport.dto.UserDTO;
import rs.ac.uns.ftn.transport.dto.ride.RideCreatedDTO;
import rs.ac.uns.ftn.transport.mapper.ride.RideCreatedDTOMapper;

import java.time.LocalDateTime;

public class ExtendedPanicDTO {

    private Integer id;
    private UserDTO user;
    private RideCreatedDTO ride;
    private LocalDateTime time;
    private String reason;
}
