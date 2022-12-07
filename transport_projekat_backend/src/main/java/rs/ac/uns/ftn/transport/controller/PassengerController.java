package rs.ac.uns.ftn.transport.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.transport.dto.RidePageDTO;
import rs.ac.uns.ftn.transport.dto.passenger.PassengerDTO;
import rs.ac.uns.ftn.transport.dto.passenger.PassengerCreatedDTO;
import rs.ac.uns.ftn.transport.dto.passenger.PassengerPageDTO;
import rs.ac.uns.ftn.transport.dto.ride.RideCreatedDTO;
import rs.ac.uns.ftn.transport.mapper.passenger.PassengerCreatedDTOMapper;
import rs.ac.uns.ftn.transport.mapper.passenger.PassengerDTOMapper;
import rs.ac.uns.ftn.transport.mapper.ride.RideCreatedDTOMapper;
import rs.ac.uns.ftn.transport.model.Passenger;
import rs.ac.uns.ftn.transport.model.Ride;
import rs.ac.uns.ftn.transport.model.UserActivation;
import rs.ac.uns.ftn.transport.service.interfaces.IPassengerService;
import rs.ac.uns.ftn.transport.service.interfaces.IUserActivationService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin("http://localhost:4200")
@RestController
@RequestMapping(value = "/api/passenger")
public class PassengerController {

    private final IPassengerService passengerService;
    private final IUserActivationService userActivationService;

    public PassengerController(IPassengerService passengerService,IUserActivationService userActivationService) {
        this.passengerService = passengerService;
        this.userActivationService = userActivationService;
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<PassengerCreatedDTO> savePassenger(@RequestBody PassengerDTO passenger)
    {
        Passenger created = passengerService.save(PassengerDTOMapper.fromDTOtoPassenger(passenger));
        return new ResponseEntity<>(PassengerCreatedDTOMapper.fromPassengerToDTO(created), HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<PassengerCreatedDTO> getPassenger(@PathVariable Integer id)
    {
        Passenger retrieved = passengerService.findOne(id);
        if (retrieved == null)
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return  new ResponseEntity<>(PassengerCreatedDTOMapper.fromPassengerToDTO(retrieved),HttpStatus.OK);
    }

    @PutMapping(value = "/{id}", consumes = "application/json")
    public ResponseEntity<PassengerCreatedDTO> updatePassenger(@PathVariable Integer id, @RequestBody PassengerDTO newInfo)
    {
        Passenger retrieved = passengerService.findOne(id);
        if(retrieved == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        retrieved.update(newInfo);
        passengerService.save(retrieved);
        return new ResponseEntity<>(PassengerCreatedDTOMapper.fromPassengerToDTO(retrieved),HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<PassengerPageDTO> getPassengers(Pageable page) {
        Page<Passenger> passengers = passengerService.findAll(page);

        Set<PassengerCreatedDTO> passengerCreatedDTOs = passengers.stream()
                .map(PassengerCreatedDTOMapper :: fromPassengerToDTO)
                .collect(Collectors.toSet());

        return new ResponseEntity<>(new PassengerPageDTO(passengers.getTotalElements(), passengerCreatedDTOs), HttpStatus.OK);
    }

    @PostMapping(value = "/activate/{activationId}")
    public ResponseEntity<String> activatePassenger(@PathVariable Integer activationId)
    {
        UserActivation activation = userActivationService.findOne(activationId);
        if(activation.checkIfExpired()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Passenger toActivate = (Passenger) activation.getUser();
        if(toActivate.getIsActivated()) {
            return new ResponseEntity<>("Account has already been activated.",HttpStatus.BAD_REQUEST);
        }
        toActivate.setIsActivated(true);
        passengerService.save(toActivate);
        return new ResponseEntity<>("Successful account activation.",HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/ride")
    public ResponseEntity<RidePageDTO> findRidesBetweenTimeSpan(Pageable page,
                                                                @PathVariable Integer id,
                                                                @RequestParam(required = false) LocalDateTime from,
                                                                @RequestParam(required = false) LocalDateTime to)
    {
        Page<Ride> retrieved = passengerService.findRidesBetweenTimeRange(id,from,to, page);
        Set<RideCreatedDTO> rideDTOs = retrieved.stream()
                .map(RideCreatedDTOMapper:: fromRideToDTO)
                .collect(Collectors.toSet());
        return new ResponseEntity<>(new RidePageDTO(retrieved.getTotalElements(),rideDTOs),HttpStatus.OK);
    }
}
