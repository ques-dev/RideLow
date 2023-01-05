package rs.ac.uns.ftn.transport.service;

import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import rs.ac.uns.ftn.transport.model.Panic;
import rs.ac.uns.ftn.transport.model.Ride;
import rs.ac.uns.ftn.transport.repository.PanicRepository;
import rs.ac.uns.ftn.transport.service.interfaces.IPanicService;
import rs.ac.uns.ftn.transport.service.interfaces.IRideService;
import rs.ac.uns.ftn.transport.service.interfaces.IUserService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PanicServiceImpl implements IPanicService {

    private final PanicRepository panicRepository;
    private final IUserService userService;
    private final IRideService rideService;

    public PanicServiceImpl(PanicRepository panicRepository,
                            IUserService userService,
                            IRideService rideService){
        this.panicRepository = panicRepository;
        this.userService = userService;
        this.rideService = rideService;
    }

    public List<Panic> findAll(){return panicRepository.findAll();}

    @Override
    public Panic save(Panic panic,Integer rideId) {
        panic.setTime(LocalDateTime.now());
        try {
            Ride ride = rideService.findOne(rideId);
            panic.setRide(ride);
            //TODO:Get user from token when JWT security will be implemented
            panic.setUser(userService.findOne(1));
            return panicRepository.save(panic);
        }
        catch(ResponseStatusException ex) {
            throw ex;
        }

    }
}
