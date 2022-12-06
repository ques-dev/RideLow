package rs.ac.uns.ftn.transport.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.uns.ftn.transport.model.DriverReview;

public interface DriverReviewRepository extends JpaRepository<DriverReview, Integer> {
}
