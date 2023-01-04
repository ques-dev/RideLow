package rs.ac.uns.ftn.transport.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.transport.model.Passenger;
import rs.ac.uns.ftn.transport.model.Ride;
import rs.ac.uns.ftn.transport.model.UserActivation;
import rs.ac.uns.ftn.transport.repository.PassengerRepository;
import rs.ac.uns.ftn.transport.service.interfaces.IPassengerService;
import org.springframework.data.domain.Pageable;
import rs.ac.uns.ftn.transport.service.interfaces.IRideService;
import rs.ac.uns.ftn.transport.service.interfaces.IUserActivationService;

import java.time.LocalDateTime;


@Service
public class PassengerServiceImpl implements IPassengerService {

    private final PassengerRepository passengerRepository;
    private final IRideService rideService;
    private final IUserActivationService activationService;

    public PassengerServiceImpl(PassengerRepository passengerRepository, IRideService rideService, IUserActivationService activationService) {
        this.passengerRepository = passengerRepository;
        this.rideService = rideService;
        this.activationService = activationService;
    }

    @Override
    public Passenger save(Passenger passenger) {
        passenger.setIsBlocked(false);
        passenger.setIsActivated(false);
        try {
            Passenger created =  passengerRepository.save(passenger);
            UserActivation activation = new UserActivation(created);
            activationService.save(activation);
            return created;
        }
        catch(DataIntegrityViolationException ex) {
            throw ex;
        }
    }

    @Override
    public Passenger findOne(Integer id) {
        System.out.println(id);
        return passengerRepository.findById(id).orElseGet(null);
    }

    public Page<Passenger> findAll(Pageable page) {
        return passengerRepository.findAll(page);
    }

    @Override
    public Page<Ride> findRidesBetweenTimeRange(Integer passengerId, LocalDateTime start, LocalDateTime end, Pageable page) {
        return rideService.findBetweenDateRange(passengerId,start,end, page);
    }
}
