package rs.ac.uns.ftn.transport.service.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rs.ac.uns.ftn.transport.model.Ride;
import rs.ac.uns.ftn.transport.model.enumerations.RideStatus;

import java.time.LocalDateTime;

public interface IRideService {

    Ride save(Ride ride);
    Ride findActiveForDriver(Integer driverId);
    Ride findActiveForPassenger(Integer passengerId);
    Page<Ride> findBetweenDateRange(Integer passengerId, LocalDateTime start, LocalDateTime end, Pageable page);
}
