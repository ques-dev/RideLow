package rs.ac.uns.ftn.transport.mapper;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rs.ac.uns.ftn.transport.dto.PassengerIdEmailDTO;
import rs.ac.uns.ftn.transport.dto.RouteDTO;
import rs.ac.uns.ftn.transport.model.Passenger;
import rs.ac.uns.ftn.transport.model.Route;

@Component
public class RouteDTOMapper {
    private static ModelMapper modelMapper;

    @Autowired
    public RouteDTOMapper(ModelMapper modelMapper) {
        RouteDTOMapper.modelMapper = modelMapper;
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE)
                .setMatchingStrategy(MatchingStrategies.STANDARD);
    }

    public static Route fromDTOtoRoute(RouteDTO dto) {
        //Route converted = modelMapper.map(dto, Route.class);
        //converted.setStartLocation(LocationDTOMapper.fromDTOtoLocation(dto.getStartLocation()));
        //converted.setEndLocation(LocationDTOMapper.fromDTOtoLocation(dto.getEndLocation()));
        return modelMapper.map(dto, Route.class);
    }

    public static RouteDTO fromRouteToDTO(Route model) {
        return modelMapper.map(model,RouteDTO.class);
    }
}
