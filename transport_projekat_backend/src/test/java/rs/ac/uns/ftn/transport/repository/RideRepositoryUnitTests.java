package rs.ac.uns.ftn.transport.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import rs.ac.uns.ftn.transport.model.*;
import rs.ac.uns.ftn.transport.model.enumerations.RideStatus;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class RideRepositoryUnitTests {

    @Autowired
    private RideRepository rideRepository;

    @Test
    @DisplayName("Should save ride based on passed ride object")
    public void shouldSaveRide() {
        Location location = new Location();
        location.setAddress("Bulevar oslobodjenja 46");
        location.setLatitude(45.0);
        location.setLongitude(19.4);
        Route route = new Route(location,location);
        Passenger passenger = new Passenger();
        passenger.setId(1);
        passenger.setEmail("passenger1@mail.com");
        Set<Route> locations = new HashSet<>();
        locations.add(route);
        Set<Passenger> passengers = new HashSet<>();
        passengers.add(passenger);
        Ride rideOrder = new Ride();
        rideOrder.setPassengers(passengers);
        rideOrder.setLocations(locations);
        rideOrder.setBabyTransport(true);
        rideOrder.setPetTransport(true);
        VehicleType vehicleType = new VehicleType();
        vehicleType.setName("STANDARD");
        rideOrder.setEstimatedTimeInMinutes(5);
        rideOrder.setTotalCost(300.0);
        Driver suitableDriver = new Driver();
        suitableDriver.setEmail("driver1@mail.com");
        rideOrder.setDriver(suitableDriver);
        rideOrder.setVehicleType(vehicleType);
        Ride retrievedRide = rideRepository.save(rideOrder);
        assertThat(rideOrder).usingRecursiveComparison().ignoringFields("id").isEqualTo(retrievedRide);
    }

    @Test
    @DisplayName("Should get ride by id")
    @Sql("classpath:test-data-insert-ride.sql")
    public void shouldGetRideById(){
        Optional<Ride> retrievedRide = rideRepository.findById(1);
        assertThat(retrievedRide).isNotEmpty();
        assertThat(retrievedRide.get().getId()).isEqualTo(1);
        assertThat(retrievedRide.get().getTotalCost()).isEqualTo(1200);
    }

    @Test
    @DisplayName("Shouldn't get ride by id if it doesn't exist")
    @Sql("classpath:test-data-insert-ride.sql")
    public void shouldNotGetRideByIdIfDoesntExist(){
        Optional<Ride> retrievedRide = rideRepository.findById(2);
        assertThat(retrievedRide).isEmpty();
    }

    @ParameterizedTest
    @DisplayName("Should get ride by it's status for driver")
    @Sql("classpath:test-data-insert-ride.sql")
    public void shouldGetRideForDriverByIdAndStatus(){
        Optional<Ride> retrievedRide = rideRepository.findById(1);
        retrievedRide.get().setStatus(RideStatus.ACTIVE);
        rideRepository.save(retrievedRide.get());
        Optional<Ride> activeRide = rideRepository.findByDriver_IdAndStatus(4,RideStatus.ACTIVE);
        assertThat(activeRide).isNotEmpty();

    }
}
