package rs.ac.uns.ftn.transport.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.transport.dto.PanicDTO;
import rs.ac.uns.ftn.transport.dto.panic.ExtendedPanicDTO;
import rs.ac.uns.ftn.transport.dto.ride.RideCreatedDTO;
import rs.ac.uns.ftn.transport.dto.ride.RideCreationDTO;
import rs.ac.uns.ftn.transport.mapper.panic.ExtendedPanicDTOMapper;
import rs.ac.uns.ftn.transport.mapper.panic.PanicReasonDTOMapper;
import rs.ac.uns.ftn.transport.mapper.ride.RideCreatedDTOMapper;
import rs.ac.uns.ftn.transport.mapper.ride.RideCreationDTOMapper;
import rs.ac.uns.ftn.transport.model.Panic;
import rs.ac.uns.ftn.transport.model.Ride;
import rs.ac.uns.ftn.transport.service.interfaces.IPanicService;
import rs.ac.uns.ftn.transport.service.interfaces.IRideService;

@RestController
@RequestMapping(value="/api/ride")
public class RideController {

    private final IRideService rideService;
    private final IPanicService panicService;

    public RideController(IRideService rideService, IPanicService panicService) {
        this.rideService = rideService;
        this.panicService = panicService;
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

    @GetMapping(value = "/{id}")
    public ResponseEntity<RideCreatedDTO> getRideDetails(@PathVariable Integer id)
    {
        Ride ride = rideService.findOne(id);
        if(ride == null)
        {
            return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(RideCreatedDTOMapper.fromRideToDTO(ride),HttpStatus.OK);
    }

    @PutMapping(value = "/{id}/withdraw") 
    public ResponseEntity<RideCreatedDTO> cancelRide(@PathVariable Integer id)
    {
        Ride toCancel = rideService.cancelRide(id);
        return new ResponseEntity<>(RideCreatedDTOMapper.fromRideToDTO(toCancel),HttpStatus.OK);
    }

    @PutMapping(value = "/{id}/panic", consumes = "application/json")
    public ResponseEntity<ExtendedPanicDTO> panic(@PathVariable Integer id, @RequestBody PanicDTO panic)
    {
        Panic retrieved = panicService.save(PanicReasonDTOMapper.fromDTOtoPanic(panic),id);
        System.out.println(retrieved);
        System.out.println(retrieved.getRide());
        System.out.println(retrieved.getUser());
        return new ResponseEntity<>(ExtendedPanicDTOMapper.fromPanicToDTO(retrieved),HttpStatus.OK);
    }

}

