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
public class RideService implements IRideService {

    private final RideRepository rideRepository;

    public RideService(RideRepository rideRepository) {
        this.rideRepository = rideRepository;
    }

    @Override
    public Ride save(Ride ride) {

        ride.setEstimatedTimeInMinutes(5);
        ride.setStartTime(LocalDateTime.now());
        ride.setEndTime(LocalDateTime.now().plus(5, ChronoUnit.MINUTES));
        /*Driver driver = new Driver();
        driver.setId(2);
        driver.setEmail("driver@mail.com");
        ride.setDriver(driver);*/
        Rejection r = new Rejection();
        ride.setRejection(r);
        ride.setTotalCost(1234.0);
        ride.setStatus(RideStatus.PENDING);
        return rideRepository.save(ride);
    }

    public Page<Ride> findAllByDriver_Id(Integer id, Pageable page) {
        return rideRepository.findAllByDriver_Id(id, page);
    }

    public Page<Ride> findAllByDriver_IdAndStartTimeIsAfterAndEndTimeIsBefore(Integer id, LocalDateTime start, LocalDateTime end, Pageable page) {
        return rideRepository.findAllByDriver_IdAndStartTimeIsAfterAndEndTimeIsBefore(id, start, end, page);
    }

    public Page<Ride> findAllByDriver_IdAndStartTimeIsAfter(Integer id, LocalDateTime start, Pageable page) {
        return rideRepository.findAllByDriver_IdAndStartTimeIsAfter(id, start, page);
    }

    public Page<Ride> findAllByDriver_IdAndEndTimeIsBefore(Integer id, LocalDateTime end, Pageable page) {
        return rideRepository.findAllByDriver_IdAndEndTimeIsBefore(id, end, page);
    }
}
