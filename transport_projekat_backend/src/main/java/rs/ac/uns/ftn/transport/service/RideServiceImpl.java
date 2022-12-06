package rs.ac.uns.ftn.transport.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.transport.model.Driver;
import rs.ac.uns.ftn.transport.model.Rejection;
import rs.ac.uns.ftn.transport.model.Ride;
import rs.ac.uns.ftn.transport.model.enumerations.RideStatus;
import rs.ac.uns.ftn.transport.repository.RideRepository;
import rs.ac.uns.ftn.transport.service.interfaces.IRideService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class RideServiceImpl implements IRideService {

    private final RideRepository rideRepository;

    public RideServiceImpl(RideRepository rideRepository) {
        this.rideRepository = rideRepository;
    }

    @Override
    public Ride save(Ride ride) {

        ride.setEstimatedTimeInMinutes(5);
        ride.setStartTime(LocalDateTime.now());
        ride.setEndTime(LocalDateTime.now().plus(5, ChronoUnit.MINUTES));
        Driver driver = new Driver();
        driver.setId(2);
        driver.setEmail("driver@mail.com");
        ride.setDriver(driver);
        Rejection r = new Rejection();
        r.setReason("Boba");
        r.setTimeOfRejection(LocalDateTime.now());
        r.setRide(ride);
        ride.setRejection(r);
        ride.setTotalCost(1234.0);
        ride.setStatus(RideStatus.ACTIVE);
        return rideRepository.save(ride);
    }

    @Override
    public Ride findActiveForDriver(Integer driverId) {
        return rideRepository.findByDriver_IdAndStatus(driverId, RideStatus.ACTIVE);
    }

    @Override
    public Ride findActiveForPassenger(Integer passengerId) {
        return rideRepository.findByPassengers_IdAndStatus(passengerId, RideStatus.ACTIVE);
    }

    @Override
    public Page<Ride> findBetweenDateRange(Integer passengerId, LocalDateTime start, LocalDateTime end, Pageable page) {
        return rideRepository.findRidesBetweenDateRange(passengerId,start,end,page);
    }
}
