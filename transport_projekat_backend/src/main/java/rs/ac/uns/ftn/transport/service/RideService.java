package rs.ac.uns.ftn.transport.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.transport.model.Ride;
import rs.ac.uns.ftn.transport.repository.RideRepository;
import rs.ac.uns.ftn.transport.service.interfaces.IRideService;

@Service
public class RideService implements IRideService {

    @Autowired
    private RideRepository rideRepository;


    @Override
    public Ride save(Ride ride) {
        return rideRepository.save(ride);
    }
}
