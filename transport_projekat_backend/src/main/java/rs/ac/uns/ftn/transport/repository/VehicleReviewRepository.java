package rs.ac.uns.ftn.transport.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.uns.ftn.transport.model.VehicleReview;

public interface VehicleReviewRepository extends JpaRepository<VehicleReview, Integer> {
}
