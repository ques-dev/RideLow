package rs.ac.uns.ftn.transport.service;

import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.transport.model.Driver;
import rs.ac.uns.ftn.transport.model.Ride;
import rs.ac.uns.ftn.transport.model.enumerations.RideStatus;
import rs.ac.uns.ftn.transport.repository.RideRepository;
import rs.ac.uns.ftn.transport.service.interfaces.IRideService;

import java.time.LocalDateTime;

@Service
public class RideService implements IRideService {

    private final RideRepository rideRepository;

    public RideService(RideRepository rideRepository) {
        this.rideRepository = rideRepository;
    }

    @Override
    public Ride save(Ride ride) {

        Ride created = rideRepository.save(ride);
        created.setEstimatedTimeInMinutes(5);
        created.setStartTime(LocalDateTime.now());
        Driver driver = new Driver();
        driver.setId(2);
        driver.setEmail("driver@mail.com");
        created.setDriver(driver);
        created.setTotalCost(1234.0);
        created.setStatus(RideStatus.PENDING);
        return created;
    }
}
