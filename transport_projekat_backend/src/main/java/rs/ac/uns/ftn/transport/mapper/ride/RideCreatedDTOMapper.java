package rs.ac.uns.ftn.transport.mapper.ride;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rs.ac.uns.ftn.transport.dto.ride.RideCreatedDTO;
import rs.ac.uns.ftn.transport.model.Ride;

@Component
public class RideCreatedDTOMapper {

    private static ModelMapper modelMapper;
    @Autowired
    public RideCreatedDTOMapper(ModelMapper modelMapper) {

        RideCreatedDTOMapper.modelMapper = modelMapper;
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE)
                .setMatchingStrategy(MatchingStrategies.STANDARD);
    }

    public static RideCreatedDTO fromRideToDTO(Ride model) {

        return modelMapper.map(model, RideCreatedDTO.class);
    }

    public static Ride fromDTOtoRide(RideCreatedDTO dto)
    {
        return modelMapper.map(dto,Ride.class);
    }
}
