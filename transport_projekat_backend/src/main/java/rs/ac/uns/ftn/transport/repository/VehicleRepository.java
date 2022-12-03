package rs.ac.uns.ftn.transport.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.uns.ftn.transport.model.Vehicle;

public interface VehicleRepository extends JpaRepository<Vehicle, Integer> {

}
