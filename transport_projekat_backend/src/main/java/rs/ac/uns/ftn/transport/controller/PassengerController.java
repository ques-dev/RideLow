package rs.ac.uns.ftn.transport.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.transport.dto.PassengerDTO;
import rs.ac.uns.ftn.transport.dto.PassengerCreatedDTO;
import rs.ac.uns.ftn.transport.mapper.PassengerCreatedDTOMapper;
import rs.ac.uns.ftn.transport.mapper.PassengerDTOMapper;
import rs.ac.uns.ftn.transport.model.Passenger;
import rs.ac.uns.ftn.transport.service.interfaces.IPassengerService;

@RestController
@RequestMapping(value = "/api/passenger")
public class PassengerController {

    private final IPassengerService passengerService;

    public PassengerController(IPassengerService passengerService) {
        this.passengerService = passengerService;
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<PassengerCreatedDTO> savePassenger(PassengerDTO passenger)
    {
        Passenger created = passengerService.save(PassengerDTOMapper.fromDTOtoPassenger(passenger));
        return new ResponseEntity<>(PassengerCreatedDTOMapper.fromPassengerToDTO(created), HttpStatus.CREATED);
    }

    @PostMapping(value = "/{activationId}")
    public ResponseEntity<String> activateUser(@PathVariable Integer activationId)
    {
        //return new ResponseEntity<>("")
        return null;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<PassengerCreatedDTO> findPassenger(@PathVariable Integer id)
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

}
