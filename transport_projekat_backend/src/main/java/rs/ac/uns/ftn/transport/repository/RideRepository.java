package rs.ac.uns.ftn.transport.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.uns.ftn.transport.model.Ride;

public interface RideRepository extends JpaRepository<Ride,Integer> {

}
