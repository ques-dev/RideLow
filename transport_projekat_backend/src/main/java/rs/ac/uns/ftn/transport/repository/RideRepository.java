package rs.ac.uns.ftn.transport.repository;

import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import rs.ac.uns.ftn.transport.model.Ride;
import rs.ac.uns.ftn.transport.model.enumerations.RideStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface RideRepository extends JpaRepository<Ride,Integer> {

    @Query("SELECT r FROM Ride r LEFT JOIN r.passengers pass " +
            "WHERE pass.id = ?1 AND r.startTime >= ?2 AND r.endTime <= ?3")
    Page<Ride> findRidesBetweenDateRange(Integer passengerId, LocalDateTime start, LocalDateTime end, Pageable page);

    Ride findByDriver_IdAndStatus(Integer driverId, RideStatus status);

}


