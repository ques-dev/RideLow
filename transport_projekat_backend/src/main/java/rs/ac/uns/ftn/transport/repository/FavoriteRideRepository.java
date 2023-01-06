package rs.ac.uns.ftn.transport.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.uns.ftn.transport.model.FavoriteRide;

public interface FavoriteRideRepository extends JpaRepository<FavoriteRide,Integer> {

}
