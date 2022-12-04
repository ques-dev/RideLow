package rs.ac.uns.ftn.transport.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rs.ac.uns.ftn.transport.dto.VehicleTypeNameDTO;
import rs.ac.uns.ftn.transport.model.VehicleType;

@Component
public class VehicleTypeNameDTOMapper {

    private static ModelMapper modelMapper;

    @Autowired
    public VehicleTypeNameDTOMapper(ModelMapper modelMapper) {
        VehicleTypeNameDTOMapper.modelMapper = modelMapper;
    }

    public static VehicleType fromDTOtoVehicleType(VehicleTypeNameDTO dto) {
        return modelMapper.map(dto, VehicleType.class);
    }

    public static VehicleTypeNameDTO fromVehicleTypeToDTO(VehicleType model) {
        return modelMapper.map(model,VehicleTypeNameDTO.class);
    }
}
