package rs.ac.uns.ftn.transport.service.interfaces;

import org.springframework.data.domain.Page;
import rs.ac.uns.ftn.transport.model.Passenger;
import org.springframework.data.domain.Pageable;

public interface IPassengerService {

    Passenger save(Passenger passenger);
    Passenger findOne(Integer id);
    Page<Passenger> findAll(Pageable page);
}
