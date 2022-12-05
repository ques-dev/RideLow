package rs.ac.uns.ftn.transport.mapper;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rs.ac.uns.ftn.transport.dto.PassengerIdEmailDTO;
import rs.ac.uns.ftn.transport.dto.RideCreationDTO;
import rs.ac.uns.ftn.transport.model.Location;
import rs.ac.uns.ftn.transport.model.Passenger;
import rs.ac.uns.ftn.transport.model.Ride;
import rs.ac.uns.ftn.transport.model.Route;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class RideCreationDTOMapper {

    static ModelMapper modelMapper;
    @Autowired
    public RideCreationDTOMapper(ModelMapper modelMapper) {

        RideCreationDTOMapper.modelMapper = modelMapper;
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE)
                .setMatchingStrategy(MatchingStrategies.STANDARD);
    }

    public static Ride fromDTOtoRide(RideCreationDTO dto) {

        //Ride ride = modelMapper.map(dto, Ride.class);
        //ride.getRoutes().add(getMainRoute(dto));
        //ride.setPassengers(fromPassengerIdEmailDTOtoPassenger(dto.getPassengers()));
        //ride.setVehicleType(VehicleTypeNameDTOMapper.fromDTOtoVehicleType(dto.getVehicleType()));
        return modelMapper.map(dto, Ride.class);
    }

    private static Set<Passenger> fromPassengerIdEmailDTOtoPassenger(List<PassengerIdEmailDTO> passengerDTOs)
    {
        Set<Passenger> passengers = new HashSet<>();
        for (PassengerIdEmailDTO passengerDTO: passengerDTOs) {
            passengers.add(PassengerIdEmailDTOMapper.fromDTOtoPassenger(passengerDTO));
        }
        return passengers;
    }

    /*private static Route getMainRoute(RideCreationDTO ride)
    {
        Location start = LocationDTOMapper.fromDTOtoLocation(ride.getLocations().get(0));
        Location end = LocationDTOMapper.fromDTOtoLocation(ride.getLocations().get(ride.getLocations().size() - 1));
        return new Route(start,end);
    }*/

    public static RideCreationDTO fromRideToDTO(Ride model) {

        RideCreationDTO ride = modelMapper.map(model, RideCreationDTO.class);
        return modelMapper.map(model, RideCreationDTO.class);
    }
}
