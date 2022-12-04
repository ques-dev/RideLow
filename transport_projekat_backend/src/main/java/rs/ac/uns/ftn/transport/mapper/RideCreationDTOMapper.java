package rs.ac.uns.ftn.transport.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rs.ac.uns.ftn.transport.dto.RideCreationDTO;
import rs.ac.uns.ftn.transport.model.Ride;

@Component
public class RideCreationDTOMapper {

    private static ModelMapper modelMapper;


    //TODO Zbrljana je Ride klasa, moram pitati kako je mislio vracati te lokacije umesto putanja i kako da odrzavam redosled
    @Autowired
    public RideCreationDTOMapper(ModelMapper modelMapper) {

       /* modelMapper.typeMap(Ride.class, RideCreationDTOMapper.class).addMappings(mapper -> {
            mapper.map(src -> src..getStreet(),
                    Destination::setBillingStreet);
            mapper.map(src -> src.getBillingAddress().getCity(),
                    Destination::setBillingCity);
        });*/
        RideCreationDTOMapper.modelMapper = modelMapper;

    }

    public static Ride fromDTOtoRide(RideCreationDTO dto) {
        return modelMapper.map(dto, Ride.class);
    }

    public static RideCreationDTO fromRidetoDTO(Ride dto) {
        return modelMapper.map(dto, RideCreationDTO.class);
    }
}
