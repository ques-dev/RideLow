package rs.ac.uns.ftn.transport.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.uns.ftn.transport.model.Driver;

public interface DriverRepository extends JpaRepository<Driver, Integer> {

}
