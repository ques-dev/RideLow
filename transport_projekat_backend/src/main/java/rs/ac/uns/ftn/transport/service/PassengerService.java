package rs.ac.uns.ftn.transport.service;

import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.transport.model.Passenger;
import rs.ac.uns.ftn.transport.repository.PassengerRepository;
import rs.ac.uns.ftn.transport.service.interfaces.IPassengerService;

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
    public Passenger findOne(Integer id) {
        return passengerRepository.findById(id).orElseGet(null);
    }


}
