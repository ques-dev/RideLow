package rs.ac.uns.ftn.transport.controller;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import rs.ac.uns.ftn.transport.dto.PanicDTO;
import rs.ac.uns.ftn.transport.dto.RejectionReasonDTO;
import rs.ac.uns.ftn.transport.dto.VehicleSimulationDTO;
import rs.ac.uns.ftn.transport.dto.panic.ExtendedPanicDTO;
import rs.ac.uns.ftn.transport.dto.panic.PanicReasonDTO;
import rs.ac.uns.ftn.transport.dto.ride.OutgoingRideSimulationDTO;
import rs.ac.uns.ftn.transport.dto.ride.RideCreatedDTO;
import rs.ac.uns.ftn.transport.dto.ride.RideCreationDTO;
import rs.ac.uns.ftn.transport.dto.ride.IncomingRideSimulationDTO;
import rs.ac.uns.ftn.transport.mapper.RejectionReasonDTOMapper;
import rs.ac.uns.ftn.transport.mapper.panic.ExtendedPanicDTOMapper;
import rs.ac.uns.ftn.transport.mapper.panic.PanicReasonDTOMapper;
import rs.ac.uns.ftn.transport.mapper.ride.RideCreatedDTOMapper;
import rs.ac.uns.ftn.transport.mapper.ride.RideCreationDTOMapper;
import rs.ac.uns.ftn.transport.model.Panic;
import rs.ac.uns.ftn.transport.model.Rejection;
import rs.ac.uns.ftn.transport.model.ResponseMessage;
import rs.ac.uns.ftn.transport.model.Ride;
import rs.ac.uns.ftn.transport.service.interfaces.IPanicService;
import rs.ac.uns.ftn.transport.service.interfaces.IRideService;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value="/api/ride")
public class RideController {

    private final IRideService rideService;
    private final IPanicService panicService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final MessageSource messageSource;

    public RideController(IRideService rideService, IPanicService panicService, SimpMessagingTemplate simpMessagingTemplate, MessageSource messageSource) {
        this.rideService = rideService;
        this.panicService = panicService;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.messageSource = messageSource;
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<RideCreatedDTO> createRide(@RequestBody RideCreationDTO rideCreationDTO)
    {
        Ride ride = rideService.save(RideCreationDTOMapper.fromDTOtoRide(rideCreationDTO));
        RideCreatedDTO rideCreatedDTO = RideCreatedDTOMapper.fromRideToDTO(ride);
        return new ResponseEntity<>(rideCreatedDTO, HttpStatus.OK);
    }

    @PostMapping(value="/sim", consumes = "application/json")
    public ResponseEntity<OutgoingRideSimulationDTO> createRideForSimulation(@RequestBody IncomingRideSimulationDTO dto)
    {
        Ride ride = rideService.saveForSimulation(dto);
        VehicleSimulationDTO vehicle = new VehicleSimulationDTO(ride.getDriver().getVehicle());
        OutgoingRideSimulationDTO rideDTO = new OutgoingRideSimulationDTO();
        rideDTO.setId(ride.getId());
        rideDTO.setVehicle(vehicle);
        this.simpMessagingTemplate.convertAndSend("/map-updates/new-ride", rideDTO);
        return new ResponseEntity<>(rideDTO, HttpStatus.OK);
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<OutgoingRideSimulationDTO>> getActiveRides() {
        List<Ride> rides = this.rideService.getActiveRides();
        List<OutgoingRideSimulationDTO> rideDTOs = new ArrayList<>();
        for (Ride ride: rides) {
            VehicleSimulationDTO vehicle = new VehicleSimulationDTO(ride.getDriver().getVehicle());
            OutgoingRideSimulationDTO rideDTO = new OutgoingRideSimulationDTO();
            rideDTO.setId(ride.getId());
            rideDTO.setVehicle(vehicle);
            rideDTOs.add(rideDTO);
        }
        return new ResponseEntity<>(rideDTOs, HttpStatus.OK);
    }

    @GetMapping(value = "/driver/{driverId}/active")
    public ResponseEntity<?> getActiveForDriver(@PathVariable Integer driverId)
    {
        try {
            Ride active = rideService.findActiveForDriver(driverId);
            return new ResponseEntity<>(RideCreatedDTOMapper.fromRideToDTO(active),HttpStatus.OK);
        }
        catch(ResponseStatusException ex) {
            return new ResponseEntity<>(new ResponseMessage(messageSource.getMessage("activeRide.notFound", null, Locale.getDefault())), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/passenger/{passengerId}/active")
    public ResponseEntity<?> getActiveForPassenger(@PathVariable Integer passengerId)
    {
        try {
            Ride active = rideService.findActiveForPassenger(passengerId);
            return new ResponseEntity<>(RideCreatedDTOMapper.fromRideToDTO(active),HttpStatus.OK);
        }
        catch(ResponseStatusException ex) {
            return new ResponseEntity<>(new ResponseMessage(messageSource.getMessage("activeRide.notFound", null, Locale.getDefault())), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getRideDetails(@PathVariable Integer id)
    {
        try {
            Ride active = rideService.findActiveForPassenger(id);
            return new ResponseEntity<>(RideCreatedDTOMapper.fromRideToDTO(active),HttpStatus.OK);
        }
        catch(ResponseStatusException ex) {
            return new ResponseEntity<>(new ResponseMessage(messageSource.getMessage("ride.notFound", null, Locale.getDefault())), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/{id}/withdraw") 
    public ResponseEntity<?> cancelRide(@PathVariable Integer id)
    {
        try {
            Ride toCancel = rideService.cancelRide(id);
            return new ResponseEntity<>(RideCreatedDTOMapper.fromRideToDTO(toCancel),HttpStatus.OK);
        }
        catch(ResponseStatusException ex) {
            return new ResponseEntity<>(new ResponseMessage(ex.getReason()), ex.getStatusCode());
        }
    }

    @PutMapping(value = "/{id}/panic", consumes = "application/json")
    public ResponseEntity<?> panic(@PathVariable Integer id, @Valid @RequestBody PanicReasonDTO panic)
    {
        try{
            Panic retrieved = panicService.save(PanicReasonDTOMapper.fromDTOtoPanic(panic),id);
            return new ResponseEntity<>(ExtendedPanicDTOMapper.fromPanicToDTO(retrieved),HttpStatus.OK);
        }
        catch(ResponseStatusException ex) {
            return new ResponseEntity<>(new ResponseMessage(messageSource.getMessage("ride.notFound", null, Locale.getDefault())), HttpStatus.NOT_FOUND);
        }

    }

    @PutMapping(value = "/{id}/accept")
    public ResponseEntity<RideCreatedDTO> acceptRide(@PathVariable Integer id)
    {
        Ride toAccept = rideService.acceptRide(id);
        return new ResponseEntity<>(RideCreatedDTOMapper.fromRideToDTO(toAccept),HttpStatus.OK);
    }

    @PutMapping(value = "/{id}/end")
    public ResponseEntity<RideCreatedDTO> endRide(@PathVariable Integer id)
    {
        Ride ride = rideService.endRide(id);
        VehicleSimulationDTO vehicle = new VehicleSimulationDTO(ride.getDriver().getVehicle());
        OutgoingRideSimulationDTO rideDTO = new OutgoingRideSimulationDTO();
        rideDTO.setId(ride.getId());
        rideDTO.setVehicle(vehicle);
        this.simpMessagingTemplate.convertAndSend("/map-updates/ended-ride", rideDTO);
        return new ResponseEntity<>(RideCreatedDTOMapper.fromRideToDTO(ride),HttpStatus.OK);
    }

    @PutMapping(value = "/{id}/cancel")
    public ResponseEntity<RideCreatedDTO> cancelRideWithExplanation(@PathVariable Integer id ,
                                                                    @RequestBody RejectionReasonDTO explanation)
    {
        Rejection rejection = RejectionReasonDTOMapper.fromDTOtoRejection(explanation);
        Ride rejected = rideService.cancelWithExplanation(id,rejection);
        return new ResponseEntity<>(RideCreatedDTOMapper.fromRideToDTO(rejected),HttpStatus.OK);
    }

}

