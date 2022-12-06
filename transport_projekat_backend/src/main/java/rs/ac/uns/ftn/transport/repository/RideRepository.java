package rs.ac.uns.ftn.transport.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rs.ac.uns.ftn.transport.model.Ride;
import rs.ac.uns.ftn.transport.model.enumerations.RideStatus;

import java.time.LocalDateTime;

public interface RideRepository extends JpaRepository<Ride,Integer> {

    @Query("SELECT r FROM Ride r LEFT JOIN r.passengers pass " +
            "WHERE pass.id = ?1 AND r.startTime >= ?2 AND r.endTime <= ?3")
    Page<Ride> findRidesBetweenDateRange(Integer passengerId, LocalDateTime start, LocalDateTime end, Pageable page);

    Ride findByDriver_IdAndStatus(Integer driverId, RideStatus status);

    Ride findByPassengers_IdAndStatus(Integer passengerId, RideStatus status);

    Page<Ride> findAllByDriver_Id(@Param("id") Integer id, Pageable page);
    Page<Ride> findAllByDriver_IdAndStartTimeIsAfterAndEndTimeIsBefore(@Param("id") Integer id, @Param("from") LocalDateTime start, @Param("to") LocalDateTime end, Pageable page);
    Page<Ride> findAllByDriver_IdAndStartTimeIsAfter(@Param("id") Integer id, @Param("from") LocalDateTime start, Pageable page);
    Page<Ride> findAllByDriver_IdAndEndTimeIsBefore(@Param("id") Integer id, @Param("to") LocalDateTime end, Pageable page);
}


