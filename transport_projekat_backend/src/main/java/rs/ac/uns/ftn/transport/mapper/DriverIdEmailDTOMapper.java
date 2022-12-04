package rs.ac.uns.ftn.transport.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rs.ac.uns.ftn.transport.dto.DriverIdEmailDTO;
import rs.ac.uns.ftn.transport.dto.PassengerIdEmailDTO;
import rs.ac.uns.ftn.transport.model.Driver;
import rs.ac.uns.ftn.transport.model.Passenger;

@Component
public class DriverIdEmailDTOMapper {
    private static ModelMapper modelMapper;

    @Autowired
    public DriverIdEmailDTOMapper(ModelMapper modelMapper) {
        DriverIdEmailDTOMapper.modelMapper = modelMapper;
    }

    public static Driver fromDTOtoDriver(DriverIdEmailDTO dto) {
        return modelMapper.map(dto, Driver.class);
    }

    public static DriverIdEmailDTO fromDriverToDTO(Driver model) {
        return modelMapper.map(model,DriverIdEmailDTO.class);
    }
}
