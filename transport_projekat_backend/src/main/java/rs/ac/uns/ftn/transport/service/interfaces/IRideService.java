package rs.ac.uns.ftn.transport.service.interfaces;

import rs.ac.uns.ftn.transport.model.Ride;
import rs.ac.uns.ftn.transport.model.enumerations.RideStatus;

public interface IRideService {

    Ride save(Ride ride);
    Ride findActiveForDriver(Integer driverId);
    Ride findActiveForPassenger(Integer passengerId);
}
