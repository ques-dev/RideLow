package rs.ac.uns.ftn.transport.controller;

import jakarta.validation.Valid;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import rs.ac.uns.ftn.transport.dto.RejectionReasonDTO;
import rs.ac.uns.ftn.transport.dto.VehicleSimulationDTO;
import rs.ac.uns.ftn.transport.dto.panic.PanicReasonDTO;
import rs.ac.uns.ftn.transport.dto.ride.*;
import rs.ac.uns.ftn.transport.mapper.RejectionReasonDTOMapper;
import rs.ac.uns.ftn.transport.mapper.panic.ExtendedPanicDTOMapper;
import rs.ac.uns.ftn.transport.mapper.panic.PanicReasonDTOMapper;
import rs.ac.uns.ftn.transport.mapper.ride.*;
import rs.ac.uns.ftn.transport.model.*;
import rs.ac.uns.ftn.transport.service.interfaces.IFavoriteRideService;
import rs.ac.uns.ftn.transport.service.interfaces.IPanicService;
import rs.ac.uns.ftn.transport.service.interfaces.IRideService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value="/api/ride")
public class RideController {

    private final IRideService rideService;
    private final IPanicService panicService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final MessageSource messageSource;
    private final IFavoriteRideService favoriteRideService;

    public RideController(IRideService rideService,
                          IPanicService panicService,
                          SimpMessagingTemplate simpMessagingTemplate,
                          MessageSource messageSource,
                          IFavoriteRideService favoriteRideService) {
        this.rideService = rideService;
        this.panicService = panicService;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.messageSource = messageSource;
        this.favoriteRideService = favoriteRideService;
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<?> createRide(@Valid @RequestBody RideCreationDTO rideCreationDTO)
    {
        try {
            Ride ride;
            if(rideCreationDTO.getScheduledTime() != null){
                rideService.reserve(RideCreationDTOMapper.fromDTOtoRide(rideCreationDTO));
                return new ResponseEntity<>(new ResponseMessage("Ride successfully reserved!"), HttpStatus.OK);
            }
            else {
                ride = rideService.save(RideCreationDTOMapper.fromDTOtoRide(rideCreationDTO), false);
                RideCreatedDTO rideCreatedDTO = RideCreatedDTOMapper.fromRideToDTO(ride);
                this.simpMessagingTemplate.convertAndSend("/ride-ordered/get-ride", rideCreatedDTO);
                return new ResponseEntity<>(rideCreatedDTO, HttpStatus.OK);
            }
        }
        catch(ResponseStatusException ex) {
            if(ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                return new ResponseEntity<>(ex.getReason(), ex.getStatusCode());
            }
            return new ResponseEntity<>(new ResponseMessage(ex.getReason()), ex.getStatusCode());
        }
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
            return new ResponseEntity<>(messageSource.getMessage("activeRide.notFound", null, Locale.getDefault()), HttpStatus.NOT_FOUND);
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
            return new ResponseEntity<>(messageSource.getMessage("activeRide.notFound", null, Locale.getDefault()), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getRideDetails(@PathVariable Integer id)
    {
        try {
            Ride ride = rideService.findOne(id);
            return new ResponseEntity<>(RideCreatedDTOMapper.fromRideToDTO(ride),HttpStatus.OK);
        }
        catch(ResponseStatusException ex) {
            return new ResponseEntity<>(messageSource.getMessage("ride.notFound", null, Locale.getDefault()), HttpStatus.NOT_FOUND);
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
            if(ex.getStatusCode() == HttpStatus.NOT_FOUND){
                return new ResponseEntity<>(ex.getReason(), ex.getStatusCode());
            }
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
            return new ResponseEntity<>(messageSource.getMessage("ride.notFound", null, Locale.getDefault()), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/{id}/start")
    public ResponseEntity<?> startRide(@PathVariable Integer id)
    {
        try {
            Ride toStart = rideService.startRide(id);
            return new ResponseEntity<>(RideCreatedDTOMapper.fromRideToDTO(toStart),HttpStatus.OK);
        }
        catch(ResponseStatusException ex) {
            if(ex.getStatusCode() == HttpStatus.NOT_FOUND){
                return new ResponseEntity<>(ex.getReason(), ex.getStatusCode());
            }
            return new ResponseEntity<>(new ResponseMessage(ex.getReason()), ex.getStatusCode());
        }
    }

    @PutMapping(value = "/{id}/accept")
    public ResponseEntity<?> acceptRide(@PathVariable Integer id)
    {
        try {
            Ride toStart = rideService.acceptRide(id);
            return new ResponseEntity<>(RideCreatedDTOMapper.fromRideToDTO(toStart),HttpStatus.OK);
        }
        catch(ResponseStatusException ex) {
            if(ex.getStatusCode() == HttpStatus.NOT_FOUND){
                return new ResponseEntity<>(ex.getReason(), ex.getStatusCode());
            }
            return new ResponseEntity<>(new ResponseMessage(ex.getReason()), ex.getStatusCode());
        }
    }

    @PutMapping(value = "/{id}/end")
    public ResponseEntity<?> endRide(@PathVariable Integer id)
    {
        try {
            Ride ride = rideService.endRide(id);
            VehicleSimulationDTO vehicle = new VehicleSimulationDTO(ride.getDriver().getVehicle());
            OutgoingRideSimulationDTO rideDTO = new OutgoingRideSimulationDTO();
            rideDTO.setId(ride.getId());
            rideDTO.setVehicle(vehicle);
            this.simpMessagingTemplate.convertAndSend("/map-updates/ended-ride", rideDTO);
            return new ResponseEntity<>(RideCreatedDTOMapper.fromRideToDTO(ride),HttpStatus.OK);
        }
        catch(ResponseStatusException ex) {
            if(ex.getStatusCode() == HttpStatus.NOT_FOUND){
                return new ResponseEntity<>(ex.getReason(), ex.getStatusCode());
            }
            return new ResponseEntity<>(new ResponseMessage(ex.getReason()), ex.getStatusCode());
        }

    }

    @PutMapping(value = "/{id}/cancel")
    public ResponseEntity<?> cancelRideWithExplanation(@PathVariable Integer id ,
                                                                    @Valid @RequestBody RejectionReasonDTO explanation)
    {
        try {
            Rejection rejection = RejectionReasonDTOMapper.fromDTOtoRejection(explanation);
            Ride rejected = rideService.cancelWithExplanation(id,rejection);
            return new ResponseEntity<>(RideCreatedDTOMapper.fromRideToDTO(rejected),HttpStatus.OK);
        }
        catch(ResponseStatusException ex) {
            if(ex.getStatusCode() == HttpStatus.NOT_FOUND){
                return new ResponseEntity<>(ex.getReason(), ex.getStatusCode());
            }
            return new ResponseEntity<>(new ResponseMessage(ex.getReason()), ex.getStatusCode());
        }
    }

    @PostMapping(value="/favorites", consumes = "application/json")
    public ResponseEntity<?> createFavoriteRide(@Valid @RequestBody FavoriteRideWithoutIdDTO favoriteRideDTO)
    {
        try{

            FavoriteRide ride = favoriteRideService.save(FavoriteRideWithoutIdDTOMapper.fromDTOtoFavoriteRide(favoriteRideDTO));
            FavoriteRideDTO favoriteRideCreatedDTO = FavoriteRideDTOMapper.fromFavoriteRideToDTO(ride);
            return new ResponseEntity<>(favoriteRideCreatedDTO, HttpStatus.OK);
        }
        catch(ResponseStatusException ex) {
            if(ex.getStatusCode() == HttpStatus.NOT_FOUND){
                return new ResponseEntity<>(ex.getReason(), ex.getStatusCode());
            }
            return new ResponseEntity<>(new ResponseMessage(ex.getReason()), ex.getStatusCode());
        }
    }

    @GetMapping(value = "/favorites")
    public ResponseEntity<?> getFavorites()
    {
        Set<FavoriteRide> favorites = favoriteRideService.findAll();
        Set<FavoriteRideDTO> favoriteDTOs = favorites.stream()
                .map(FavoriteRideDTOMapper:: fromFavoriteRideToDTO)
                .collect(Collectors.toSet());
        return new ResponseEntity<>(favoriteDTOs,HttpStatus.OK);

    }

    @DeleteMapping(value = "/favorites/{id}")
    public ResponseEntity<?> deleteFavorite(@PathVariable Integer id) {
        try {
            this.favoriteRideService.delete(id);
            return new ResponseEntity<>("Successful deletion of favorite location!",HttpStatus.NO_CONTENT);
        } catch (ResponseStatusException ex) {
            if(ex.getStatusCode() == HttpStatus.NOT_FOUND){
                return new ResponseEntity<>(ex.getReason(), ex.getStatusCode());
            }
            return new ResponseEntity<>(new ResponseMessage(ex.getReason()), ex.getStatusCode());
        }
    }

    @MessageMapping("/on-location")
    public void broadcastNotification(String rideId) {
        int rideID = Integer.parseInt(rideId);
        Ride ride = rideService.findOne(rideID);
        List<Integer> passengersId = new ArrayList<>();
        for(Passenger passenger : ride.getPassengers()) {
            passengersId.add(passenger.getId());
        }
        this.simpMessagingTemplate.convertAndSend("/driver-at-location/notification", passengersId);
    }

}

