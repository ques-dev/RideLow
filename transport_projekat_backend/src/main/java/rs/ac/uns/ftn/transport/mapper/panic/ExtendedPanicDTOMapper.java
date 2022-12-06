package rs.ac.uns.ftn.transport.mapper.panic;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import rs.ac.uns.ftn.transport.dto.PanicDTO;
import rs.ac.uns.ftn.transport.dto.panic.ExtendedPanicDTO;
import rs.ac.uns.ftn.transport.mapper.ride.RideCreatedDTOMapper;
import rs.ac.uns.ftn.transport.model.Panic;

public class ExtendedPanicDTOMapper {

    private static ModelMapper modelMapper;

    @Autowired
    public ExtendedPanicDTOMapper(ModelMapper modelMapper){

        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE)
                .setMatchingStrategy(MatchingStrategies.STANDARD);
        ExtendedPanicDTOMapper.modelMapper = modelMapper;
    }

    public static Panic fromDTOtoPanic(PanicDTO dto) {return modelMapper.map(dto,  Panic.class);}

    public static ExtendedPanicDTO fromPanicToDTO(Panic dto) {return modelMapper.map(dto, ExtendedPanicDTO.class);}
}
