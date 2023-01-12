package rs.ac.uns.ftn.transport.controller;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import rs.ac.uns.ftn.transport.dto.RidePageDTO;
import rs.ac.uns.ftn.transport.dto.passenger.PassengerDTO;
import rs.ac.uns.ftn.transport.dto.passenger.PassengerCreatedDTO;
import rs.ac.uns.ftn.transport.dto.passenger.PassengerPageDTO;
import rs.ac.uns.ftn.transport.dto.passenger.PassengerWithoutIdPasswordDTO;
import rs.ac.uns.ftn.transport.dto.ride.RideCreatedDTO;
import rs.ac.uns.ftn.transport.mapper.passenger.PassengerCreatedDTOMapper;
import rs.ac.uns.ftn.transport.mapper.passenger.PassengerDTOMapper;
import rs.ac.uns.ftn.transport.mapper.ride.RideCreatedDTOMapper;
import rs.ac.uns.ftn.transport.model.Passenger;
import rs.ac.uns.ftn.transport.model.ResponseMessage;
import rs.ac.uns.ftn.transport.model.Ride;
import rs.ac.uns.ftn.transport.model.UserActivation;
import rs.ac.uns.ftn.transport.service.interfaces.IImageService;
import rs.ac.uns.ftn.transport.service.interfaces.IPassengerService;
import rs.ac.uns.ftn.transport.service.interfaces.IUserActivationService;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;

import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin("http://localhost:4200")
@RestController
@RequestMapping(value = "/api/passenger")
public class PassengerController {

    private final IPassengerService passengerService;
    private final IUserActivationService userActivationService;
    private final IImageService imageService;
    private final MessageSource messageSource;


    public PassengerController(IPassengerService passengerService,
                               IUserActivationService userActivationService,
                               IImageService imageService,
                               MessageSource messageSource) {
        this.passengerService = passengerService;
        this.userActivationService = userActivationService;
        this.imageService = imageService;
        this.messageSource = messageSource;
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<?> savePassenger(@Valid @RequestBody PassengerDTO passenger)
    {
        Passenger created = PassengerDTOMapper.fromDTOtoPassenger(passenger);
        if (created.getProfilePicture() != null) {
            ResponseEntity<ResponseMessage> invalidProfilePicture = imageService.decodeAndValidateImage(created.getProfilePicture());
            if (invalidProfilePicture != null) {
                return invalidProfilePicture;
            }
        }
        try {
            created = passengerService.save(created);
            return new ResponseEntity<>(PassengerCreatedDTOMapper.fromPassengerToDTO(created), HttpStatus.OK);
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>(new ResponseMessage(messageSource.getMessage("user.emailExists", null, Locale.getDefault())), HttpStatus.BAD_REQUEST);
        } catch (MessagingException | UnsupportedEncodingException ex) {
            return new ResponseEntity<>(new ResponseMessage(messageSource.getMessage("mail.activationError", null, Locale.getDefault())), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getPassenger(@PathVariable Integer id)
    {
        try {
            Passenger retrieved = passengerService.findOne(id);
            return new ResponseEntity<>(PassengerCreatedDTOMapper.fromPassengerToDTO(retrieved), HttpStatus.OK);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(messageSource.getMessage("passenger.notFound", null, Locale.getDefault()), HttpStatus.NOT_FOUND);
        }

    }

    @PutMapping(value = "/{id}", consumes = "application/json")
    public ResponseEntity<?> updatePassenger(@PathVariable Integer id, @Valid @RequestBody PassengerWithoutIdPasswordDTO newInfo)
    {
        Passenger retrieved;
        try {
            retrieved = passengerService.findOne(id);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(messageSource.getMessage("passenger.notFound", null, Locale.getDefault()), HttpStatus.NOT_FOUND);
        }
        if (newInfo.getProfilePicture() != null) {
            ResponseEntity<ResponseMessage> invalidProfilePicture = imageService.decodeAndValidateImage(newInfo.getProfilePicture());
            if (invalidProfilePicture != null) {
                return invalidProfilePicture;
            }
        }
        try {
            retrieved.update(newInfo);
            passengerService.update(retrieved);
            return new ResponseEntity<>(PassengerCreatedDTOMapper.fromPassengerToDTO(retrieved), HttpStatus.OK);
        }
        catch(DataIntegrityViolationException ex){
            return new ResponseEntity<>(new ResponseMessage(messageSource.getMessage("user.emailExists", null, Locale.getDefault())), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<PassengerPageDTO> getPassengers(Pageable page) {
        Page<Passenger> passengers = passengerService.findAll(page);

        Set<PassengerCreatedDTO> passengerCreatedDTOs = passengers.stream()
                .map(PassengerCreatedDTOMapper :: fromPassengerToDTO)
                .collect(Collectors.toSet());

        return new ResponseEntity<>(new PassengerPageDTO(passengers.getTotalElements(), passengerCreatedDTOs), HttpStatus.OK);
    }

    @GetMapping(value = "/activate/{activationId}")
    public ResponseEntity<?> activatePassenger(@PathVariable Integer activationId)
    {
        try {
            UserActivation activation = userActivationService.findOne(activationId);
            if (activation.checkIfExpired()) {
                userActivationService.delete(activation);
                UserActivation renewed = new UserActivation(activation.getUser());
                userActivationService.save(renewed);
                return new ResponseEntity<>(new ResponseMessage(messageSource.getMessage("activation.expired", null, Locale.getDefault())), HttpStatus.BAD_REQUEST);
            }
            Passenger toActivate = (Passenger) activation.getUser();
            if (toActivate.getIsActivated()) {
                return new ResponseEntity<>(new ResponseMessage(messageSource.getMessage("activation.alreadyActivated", null, Locale.getDefault())), HttpStatus.BAD_REQUEST);
            }
            toActivate.setIsActivated(true);
            passengerService.update(toActivate);
            return new ResponseEntity<>(new ResponseMessage("Successful account activation!"), HttpStatus.OK);
        }
        catch(ResponseStatusException ex) {
            return new ResponseEntity<>(messageSource.getMessage("activation.nonExisting", null, Locale.getDefault()), HttpStatus.NOT_FOUND);
        } catch (MessagingException | UnsupportedEncodingException ex) {
            return new ResponseEntity<>(new ResponseMessage(messageSource.getMessage("mail.activationError", null, Locale.getDefault())), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/{id}/ride")
    public ResponseEntity<?> findRidesBetweenTimeSpan(Pageable page,
                                                                @PathVariable Integer id,
                                                                @RequestParam(required = false) LocalDateTime from,
                                                                @RequestParam(required = false) LocalDateTime to
                                                                )
    {
        try {
            passengerService.findOne(id);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(messageSource.getMessage("passenger.notFound", null, Locale.getDefault()), HttpStatus.NOT_FOUND);
        }
        Page<Ride> rides;
        if (from == null && to == null) {
            rides = passengerService.findAllByPassenger_Id(id, page);
        } else if (from != null && to == null) {
            rides = passengerService.findAllByPassenger_IdAndStartTimeIsAfter(id, from, page);
        } else if (from == null) {
            rides = passengerService.findAllByPassenger_IdAndEndTimeIsBefore(id, to, page);
        } else {
            rides = passengerService.findAllByPassenger_IdAndStartTimeIsAfterAndEndTimeIsBefore(id, from, to, page);
        }
        Set<RideCreatedDTO> rideDTOs = rides.stream()
                .map(RideCreatedDTOMapper:: fromRideToDTO).collect(Collectors.toSet());
        return new ResponseEntity<>(new RidePageDTO(rides.getTotalElements(),rideDTOs),HttpStatus.OK);
    }
}
