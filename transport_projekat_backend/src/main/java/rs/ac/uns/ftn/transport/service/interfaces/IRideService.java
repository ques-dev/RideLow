package rs.ac.uns.ftn.transport.service.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rs.ac.uns.ftn.transport.model.Ride;
import java.time.LocalDateTime;

public interface IRideService {

    Ride save(Ride ride);
    Ride findActiveForDriver(Integer driverId);
    Ride findActiveForPassenger(Integer passengerId);

    Page<Ride> findBetweenDateRange(Integer passengerId, LocalDateTime start, LocalDateTime end, Pageable page);
    Ride findOne(Integer id);
    Page<Ride> findAllByDriver_Id(Integer id, Pageable page);
    Page<Ride> findAllByDriver_IdAndStartTimeIsAfterAndEndTimeIsBefore(Integer id, LocalDateTime start, LocalDateTime end, Pageable page);
    Page<Ride> findAllByDriver_IdAndStartTimeIsAfter(Integer id, LocalDateTime start, Pageable page);
    Page<Ride> findAllByDriver_IdAndEndTimeIsBefore(Integer id, LocalDateTime end, Pageable page);
    Ride cancelRide(Integer id);


}
