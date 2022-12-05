package rs.ac.uns.ftn.transport.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.uns.ftn.transport.model.Passenger;

public interface PassengerRepository extends JpaRepository<Passenger,Integer> {

}
