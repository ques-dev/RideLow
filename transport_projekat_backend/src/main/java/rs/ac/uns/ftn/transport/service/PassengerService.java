package rs.ac.uns.ftn.transport.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.transport.model.Passenger;
import rs.ac.uns.ftn.transport.repository.PassengerRepository;
import rs.ac.uns.ftn.transport.service.interfaces.IPassengerService;
import org.springframework.data.domain.Pageable;


@Service
public class PassengerService implements IPassengerService {

    private final PassengerRepository passengerRepository;

    public PassengerService(PassengerRepository passengerRepository) {
        this.passengerRepository = passengerRepository;
    }

    @Override
    public Passenger save(Passenger passenger) {
        return passengerRepository.save(passenger);
    }

    @Override
    public Page<Passenger> findAll(Pageable page) {
        return passengerRepository.findAll(page);
    }
}
