package rs.ac.uns.ftn.transport.repository;

import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rs.ac.uns.ftn.transport.model.Ride;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface RideRepository extends JpaRepository<Ride,Integer> {

    @Query("SELECT r FROM Ride r LEFT JOIN r.passengers pass " +
            "WHERE pass.id = ?1 AND r.startTime >= ?2 AND r.endTime <= ?3")
    Page<Ride> findRidesBetweenDateRange(Integer passengerId, LocalDateTime start, LocalDateTime end, Pageable page);
    Page<Ride> findAllByDriver_Id(@Param("id") Integer id, Pageable page);
    Page<Ride> findAllByDriver_IdAndStartTimeIsAfterAndEndTimeIsBefore(@Param("id") Integer id, @Param("from") LocalDateTime start, @Param("to") LocalDateTime end, Pageable page);
    Page<Ride> findAllByDriver_IdAndStartTimeIsAfter(@Param("id") Integer id, @Param("from") LocalDateTime start, Pageable page);
    Page<Ride> findAllByDriver_IdAndEndTimeIsBefore(@Param("id") Integer id, @Param("to") LocalDateTime end, Pageable page);
}
