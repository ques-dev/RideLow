package rs.ac.uns.ftn.transport.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.transport.model.Passenger;
import rs.ac.uns.ftn.transport.model.Ride;
import rs.ac.uns.ftn.transport.repository.PassengerRepository;
import rs.ac.uns.ftn.transport.repository.RideRepository;
import rs.ac.uns.ftn.transport.service.interfaces.IPassengerService;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;


@Service
public class PassengerService implements IPassengerService {

    private final PassengerRepository passengerRepository;
    private final RideRepository rideRepository;

    public PassengerService(PassengerRepository passengerRepository, RideRepository rideRepository) {
        this.passengerRepository = passengerRepository;
        this.rideRepository = rideRepository;
    }

    @Override
    public Passenger save(Passenger passenger) {
        return passengerRepository.save(passenger);
    }

    @Override
    public Passenger findOne(Integer id) {
        return passengerRepository.findById(id).orElseGet(null);
    }

    public Page<Passenger> findAll(Pageable page) {
        return passengerRepository.findAll(page);
    }

    @Override
    public Page<Ride> findRidesBetweenTimeRange(Integer passengerId, LocalDateTime start, LocalDateTime end, Pageable page) {
        return rideRepository.findRidesBetweenDateRange(passengerId,start,end, page);
    }
}
