package rs.ac.uns.ftn.transport.mapper.ride;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rs.ac.uns.ftn.transport.dto.ride.FavoriteRideDTO;
import rs.ac.uns.ftn.transport.dto.ride.FavoriteRideWithoutIdDTO;
import rs.ac.uns.ftn.transport.model.FavoriteRide;
import rs.ac.uns.ftn.transport.service.interfaces.IVehicleTypeService;

@Component
public class FavoriteRideWithoutIdDTOMapper {

    private static ModelMapper modelMapper;
    private static IVehicleTypeService vehicleTypeService;
    @Autowired
    public FavoriteRideWithoutIdDTOMapper(ModelMapper modelMapper, IVehicleTypeService vehicleTypeService) {

        FavoriteRideWithoutIdDTOMapper.modelMapper = modelMapper;
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE)
                .setMatchingStrategy(MatchingStrategies.STANDARD);
        FavoriteRideWithoutIdDTOMapper.vehicleTypeService = vehicleTypeService;
    }

    public static FavoriteRideWithoutIdDTO fromFavoriteRideToDTO(FavoriteRide model) {
        String vehicleType = model.getVehicleType().getName();
        FavoriteRideWithoutIdDTO rideDTO = modelMapper.map(model,FavoriteRideWithoutIdDTO.class);
        rideDTO.setVehicleType(vehicleType);
        return rideDTO;
    }

    public static FavoriteRide fromDTOtoFavoriteRide(FavoriteRideWithoutIdDTO dto)
    {
        FavoriteRide ride = modelMapper.map(dto, FavoriteRide.class);
        ride.setVehicleType(vehicleTypeService.findByName(dto.getVehicleType().toUpperCase()));
        return ride;
    }
}
