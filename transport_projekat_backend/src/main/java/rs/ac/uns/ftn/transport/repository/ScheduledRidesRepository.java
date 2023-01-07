package rs.ac.uns.ftn.transport.repository;

import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.transport.model.ScheduledRide;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ScheduledRidesRepository {

    private List<ScheduledRide> scheduledRides = new ArrayList<>();

    public void addRide(ScheduledRide ride) {
        this.scheduledRides.add(ride);
    }

    public void deleteRide(ScheduledRide ride) {
        this.scheduledRides.remove(ride);
    }

    public List<ScheduledRide> getAllScheduledRides() {
        return this.scheduledRides;
    }

}
