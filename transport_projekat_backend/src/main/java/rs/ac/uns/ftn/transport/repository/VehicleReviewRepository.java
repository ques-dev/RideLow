package rs.ac.uns.ftn.transport.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import rs.ac.uns.ftn.transport.model.VehicleReview;

import java.util.Set;

public interface VehicleReviewRepository extends JpaRepository<VehicleReview, Integer> {
    Set<VehicleReview> findByVehicleId(@Param("vehicle.getId()") Integer id);
}
