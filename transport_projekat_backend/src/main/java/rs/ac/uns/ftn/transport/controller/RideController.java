package rs.ac.uns.ftn.transport.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.transport.dto.ride.RideCreatedDTO;
import rs.ac.uns.ftn.transport.dto.ride.RideCreationDTO;
import rs.ac.uns.ftn.transport.mapper.ride.RideCreatedDTOMapper;
import rs.ac.uns.ftn.transport.mapper.ride.RideCreationDTOMapper;
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

    @GetMapping(value = "/driver/{driverId}/active")
    public ResponseEntity<RideCreatedDTO> getActiveForDriver(@PathVariable Integer driverId)
    {
        Ride active = rideService.findActiveForDriver(driverId);
        if(active == null)
        {
            return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(RideCreatedDTOMapper.fromRideToDTO(active),HttpStatus.OK);
    }

    @GetMapping(value = "/passenger/{passengerId}/active")
    public ResponseEntity<RideCreatedDTO> getActiveForPassenger(@PathVariable Integer passengerId)
    {
        Ride active = rideService.findActiveForPassenger(passengerId);
        if(active == null)
        {
            return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(RideCreatedDTOMapper.fromRideToDTO(active),HttpStatus.OK);
    }
}

