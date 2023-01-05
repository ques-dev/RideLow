package rs.ac.uns.ftn.transport.service;

import jakarta.mail.MessagingException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import rs.ac.uns.ftn.transport.model.Passenger;
import rs.ac.uns.ftn.transport.model.Ride;
import rs.ac.uns.ftn.transport.model.UserActivation;
import rs.ac.uns.ftn.transport.repository.PassengerRepository;
import rs.ac.uns.ftn.transport.service.interfaces.IPassengerService;
import org.springframework.data.domain.Pageable;
import rs.ac.uns.ftn.transport.service.interfaces.IRideService;
import rs.ac.uns.ftn.transport.service.interfaces.IUserActivationService;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.Optional;


@Service
public class PassengerServiceImpl implements IPassengerService {

    private final PassengerRepository passengerRepository;
    private final IRideService rideService;
    private final IUserActivationService activationService;

    public PassengerServiceImpl(PassengerRepository passengerRepository,
                                IRideService rideService,
                                IUserActivationService activationService) {
        this.passengerRepository = passengerRepository;
        this.rideService = rideService;
        this.activationService = activationService;
    }

    @Override
    public Passenger save(Passenger passenger) throws MessagingException, UnsupportedEncodingException {
        passenger.setIsBlocked(false);
        passenger.setIsActivated(false);
        try {
            Passenger created = passengerRepository.save(passenger);
            UserActivation activation = new UserActivation(created);
            activationService.save(activation);
            return created;
        }
        catch(DataIntegrityViolationException | MessagingException | UnsupportedEncodingException ex) {
            throw ex;
        }
    }

    @Override
    public Passenger update(Passenger passenger) {
        try{
            return passengerRepository.save(passenger);
        }
        catch(DataIntegrityViolationException ex) {
            throw ex;
        }
    }

    @Override
    public Passenger findOne(Integer id) {
        Optional<Passenger> passenger = passengerRepository.findById(id);
        if(passenger.isEmpty() || !passenger.get().getIsActivated())
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return passenger.get();
    }

    public Page<Passenger> findAll(Pageable page) {
        return passengerRepository.findAll(page);
    }

    @Override
    public Page<Ride> findAllByPassenger_Id(Integer id, Pageable page) {
        return rideService.findAllByPassenger_Id(id, page);
    }

    @Override
    public Page<Ride> findAllByPassenger_IdAndStartTimeIsAfterAndEndTimeIsBefore(Integer id, LocalDateTime start, LocalDateTime end, Pageable page) {
        return rideService.findAllByPassenger_IdAndStartTimeIsAfterAndEndTimeIsBefore(id, start, end, page);
    }

    @Override
    public Page<Ride> findAllByPassenger_IdAndStartTimeIsAfter(Integer id, LocalDateTime start, Pageable page) {
        return rideService.findAllByPassenger_IdAndStartTimeIsAfter(id,start,page);
    }

    @Override
    public Page<Ride> findAllByPassenger_IdAndEndTimeIsBefore(Integer id, LocalDateTime end, Pageable page) {
        return rideService.findAllByPassenger_IdAndEndTimeIsBefore(id,end,page);
    }
}
