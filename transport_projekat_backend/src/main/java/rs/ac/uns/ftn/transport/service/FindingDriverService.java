package rs.ac.uns.ftn.transport.service;

import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import rs.ac.uns.ftn.transport.model.*;
import rs.ac.uns.ftn.transport.model.enumerations.RideStatus;
import rs.ac.uns.ftn.transport.repository.DriverRepository;
import rs.ac.uns.ftn.transport.repository.RideRepository;
import rs.ac.uns.ftn.transport.service.interfaces.IFindingDriverService;
import rs.ac.uns.ftn.transport.service.interfaces.IRideService;
import rs.ac.uns.ftn.transport.service.interfaces.IWorkingHoursService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class FindingDriverService implements IFindingDriverService {

    private final EstimatesService estimatesService;
    private final DriverRepository driverRepository;
    private final IRideService rideService;
    private final RideRepository rideRepository;
    private final IWorkingHoursService workingHoursService;

    public FindingDriverService(EstimatesService estimatesService,
                                DriverRepository driverRepository,
                                IRideService rideService,
                                RideRepository rideRepository,
                                IWorkingHoursService workingHoursService) {
        this.estimatesService = estimatesService;
        this.driverRepository = driverRepository;
        this.rideService = rideService;
        this.rideRepository = rideRepository;
        this.workingHoursService = workingHoursService;
    }

    @Override
    public Driver findSuitableDriver(Ride order) {
        LocalDateTime now = LocalDateTime.now();

        //PHASE 1: Filtering drivers based on status (active/logged in -- inactive)
        Set<Driver> activeDrivers = this.driverRepository.getAllByIsActiveTrue();
        if (activeDrivers.size() == 0) return null; //no logged in drivers
        List<Driver> inRide = new ArrayList<>();
        List<Driver> free = new ArrayList<>();

        //PHASE 2 : Filtering active drivers based on working hours (>=8 hours do not get rides) and vehicle
        // (if vehicle is suitable, driver can go to other stages of filtration) and sorting them
        //according to their current state (ride in progress -- free drivers)
        for (Driver driver : activeDrivers) {
            long minutesWorked = this.workingHoursService.getDurationWorkedInPastDay(driver.getId()).toMinutes();
            if(minutesWorked >= 8*60) continue; //8 hours in minutes
            if(driver.getVehicle() == null) continue; //doesn't have assigned vehicle
            if(this.isVehicleSuitable(order,driver.getVehicle())) {
                if(this.isInActiveRide(driver)) inRide.add(driver);
                else free.add(driver);
            }
        }

        //PHASE 3 : Filtering free drivers based on time(if they have a scheduled ride, they should have time for the ordered ride)
        //Since they do not have a ride going on, they have higher priority for ride assignment
        List<Driver> mostSuitable = new ArrayList<>();
        for(Driver freeDriver : free) {
            //He is not in ride, active, and doesn't have scheduled ride
            if(!this.hasScheduledRide(freeDriver, now)) mostSuitable.add(freeDriver);
            else {
                Ride scheduled = this.rideRepository.findFirstByDriver_IdAndOrderedForIsAfter(freeDriver.getId(), now).get();
                //He has a scheduled ride, but has time to take in order and finish it before the scheduled one
                if (this.hasTimeBeforeScheduledRide(scheduled, order)) mostSuitable.add(freeDriver);
            }
        }

        //PHASE 4: Filtering drivers based on distance (the closest one to order's departure is the most suitable one)
        if(mostSuitable.size() != 0) {
            Location departure = null;
            for(Route route : order.getLocations()) {
                departure = route.getDeparture(); break;
            }
            return this.getClosestDriver(mostSuitable,departure);
        }

        //PHASE 5: Filtering drivers that have ride in progress based on time(if they have a scheduled ride, they should have time for the ordered ride)
        for(Driver inRideDriver : inRide) {
            //He is in ride, active, and doesn't have scheduled ride
            if(!this.hasScheduledRide(inRideDriver, now)) mostSuitable.add(inRideDriver);
            else {
                Ride scheduled = this.rideRepository.findFirstByDriver_IdAndOrderedForIsAfter(inRideDriver.getId(), now).get();
                //He has a scheduled ride, but has time to take in order and finish it before the scheduled one
                if (this.hasTimeBeforeScheduledRide(scheduled, order)) mostSuitable.add(inRideDriver);
            }
        }

        //PHASE 6: Filtering drivers based on finish time (the closest one to finish current order will be the most suitable)
        if(mostSuitable.size() != 0) {
            return this.getSoonestToFinishDriver(mostSuitable);
        }
        return null;
    }

    private boolean isVehicleSuitable(Ride order, Vehicle driversVehicle) {
        if(driversVehicle.getVehicleType().getId().equals(order.getVehicleType().getId()) &&
                driversVehicle.getPassengerSeats() >= order.getPassengers().size()) {
            return (driversVehicle.getPetTransport() && driversVehicle.getBabyTransport()) ||
                    ((!driversVehicle.getBabyTransport() && !order.getBabyTransport()) &&
                            (!driversVehicle.getPetTransport() && !order.getPetTransport())
                    );
        }
        return false;
    }

    private boolean isInActiveRide(Driver driver) {
        try {
            this.rideService.findActiveForDriver(driver.getId());
            return true;
        } catch (ResponseStatusException ex) { //driver does not have active ride
            return false;
        }
    }

    private boolean hasScheduledRide(Driver driver, LocalDateTime now) {
        Optional<Ride> scheduled = this.rideRepository.findFirstByDriver_IdAndOrderedForIsAfter(driver.getId(), now);
        if (scheduled.isEmpty()) return false;
        return scheduled.get().getStatus() != RideStatus.CANCELLED && scheduled.get().getStatus() != RideStatus.REJECTED;
    }

    private boolean hasTimeBeforeScheduledRide(Ride scheduled, Ride order) {
        double totalTimeOfTravelForDriver = 0;
        Location currentVehicleLocation = order.getDriver().getVehicle().getCurrentLocation();
        Location departureOfOrder = null;
        for (Route route : order.getLocations()) {
            if (departureOfOrder == null) departureOfOrder = route.getDeparture(); //will change only first time
            double routeDistance = this.estimatesService.calculateDistance(route.getDeparture(), route.getDestination());
            totalTimeOfTravelForDriver += this.estimatesService.getEstimatedTime(routeDistance);
        }
        double distanceBetweenVehicleAndOrderDeparture = this.estimatesService.calculateDistance(currentVehicleLocation, departureOfOrder);
        totalTimeOfTravelForDriver += this.estimatesService.getEstimatedTime(distanceBetweenVehicleAndOrderDeparture);
        LocalDateTime orderEstimatedEndTime = order.getOrderedFor().plus(Math.round(totalTimeOfTravelForDriver), ChronoUnit.MINUTES);
        return !orderEstimatedEndTime.isAfter(scheduled.getOrderedFor());
    }

    private Driver getClosestDriver(List<Driver> freeDrivers, Location departure) {
        Driver closest = null;
        double closestDistance = -1;
        for (Driver driver : freeDrivers) {
            if (driver.getVehicle() == null) continue;
            Location currentDriverLocation = driver.getVehicle().getCurrentLocation();
            double distanceBetweenDriverAndDeparture = this.estimatesService.calculateDistance(currentDriverLocation, departure);
            if (distanceBetweenDriverAndDeparture > closestDistance) continue;
            closest = driver;
            closestDistance = distanceBetweenDriverAndDeparture;
        }
        return closest;
    }

    private Driver getSoonestToFinishDriver(List<Driver> inRideDrivers) {
        Driver soonest = null;
        LocalDateTime soonestFinish = LocalDateTime.MIN;
        for (Driver driver : inRideDrivers) {
            Ride inProgress = this.rideService.findActiveForDriver(driver.getId());
            LocalDateTime rideInProgressFinish = inProgress.getStartTime().plus(inProgress.getEstimatedTimeInMinutes(),ChronoUnit.MINUTES);
            if(rideInProgressFinish.isBefore(soonestFinish)) continue;
            soonest = driver;
            soonestFinish = rideInProgressFinish;
        }
        return soonest;
    }
}
