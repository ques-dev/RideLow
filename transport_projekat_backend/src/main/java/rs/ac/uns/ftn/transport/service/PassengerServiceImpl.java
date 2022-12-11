package rs.ac.uns.ftn.transport.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.transport.model.Passenger;
import rs.ac.uns.ftn.transport.model.Ride;
import rs.ac.uns.ftn.transport.repository.PassengerRepository;
import rs.ac.uns.ftn.transport.repository.RideRepository;
import rs.ac.uns.ftn.transport.service.interfaces.IPassengerService;
import org.springframework.data.domain.Pageable;
import rs.ac.uns.ftn.transport.service.interfaces.IRideService;

import java.time.LocalDateTime;


@Service
public class PassengerServiceImpl implements IPassengerService {

    private final PassengerRepository passengerRepository;
    private final IRideService rideService;

    public PassengerServiceImpl(PassengerRepository passengerRepository,IRideService rideService) {
        this.passengerRepository = passengerRepository;
        this.rideService = rideService;
    }

    @Override
    public Passenger save(Passenger passenger) {
        return passengerRepository.save(passenger);
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
