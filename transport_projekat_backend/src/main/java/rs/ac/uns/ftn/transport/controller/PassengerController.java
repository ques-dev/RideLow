package rs.ac.uns.ftn.transport.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.transport.dto.*;
import rs.ac.uns.ftn.transport.mapper.DriverDTOMapper;
import rs.ac.uns.ftn.transport.mapper.PassengerCreatedDTOMapper;
import rs.ac.uns.ftn.transport.mapper.PassengerDTOMapper;
import rs.ac.uns.ftn.transport.model.Driver;
import rs.ac.uns.ftn.transport.model.Passenger;
import rs.ac.uns.ftn.transport.service.interfaces.IPassengerService;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/passenger")
public class PassengerController {

    private final IPassengerService passengerService;

    public PassengerController(IPassengerService passengerService) {
        this.passengerService = passengerService;
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<PassengerCreatedDTO> savePassenger(@RequestBody PassengerDTO passenger)
    {
        Passenger created = passengerService.save(PassengerDTOMapper.fromDTOtoPassenger(passenger));
        return new ResponseEntity<>(PassengerCreatedDTOMapper.fromPassengerToDTO(created), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<PassengerPageDTO> getPassengers(Pageable page) {
        Page<Passenger> passengers = passengerService.findAll(page);

        Set<PassengerCreatedDTO> passengerCreatedDTOs = passengers.stream()
                .map(PassengerCreatedDTOMapper :: fromPassengerToDTO)
                .collect(Collectors.toSet());

        return new ResponseEntity<>(new PassengerPageDTO(passengers.getTotalElements(), passengerCreatedDTOs), HttpStatus.OK);
    }
}
