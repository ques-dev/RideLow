package rs.ac.uns.ftn.transport.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.transport.dto.RideCreatedDTO;
import rs.ac.uns.ftn.transport.dto.RideCreationDTO;
import rs.ac.uns.ftn.transport.mapper.RideCreatedDTOMapper;
import rs.ac.uns.ftn.transport.mapper.RideCreationDTOMapper;
import rs.ac.uns.ftn.transport.model.Ride;
import rs.ac.uns.ftn.transport.service.interfaces.IRideService;

@RestController
@RequestMapping(value="/api/ride")
public class RideController {

    private final IRideService rideService;

    public RideController(IRideService rideService) {
        this.rideService = rideService;
    }


    @PostMapping(consumes = "application/json")
    public ResponseEntity<RideCreatedDTO> createRide(@RequestBody RideCreationDTO rideCreationDTO)
    {

        Ride ride = rideService.save(RideCreationDTOMapper.fromDTOtoRide(rideCreationDTO));

        return new ResponseEntity<>(RideCreatedDTOMapper.fromRideToDTO(ride), HttpStatus.CREATED);
    }
}
