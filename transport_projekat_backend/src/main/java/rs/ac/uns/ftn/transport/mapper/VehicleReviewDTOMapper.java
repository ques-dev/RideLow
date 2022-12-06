package rs.ac.uns.ftn.transport.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rs.ac.uns.ftn.transport.dto.DriverDTO;
import rs.ac.uns.ftn.transport.dto.VehicleReviewDTO;
import rs.ac.uns.ftn.transport.model.Driver;
import rs.ac.uns.ftn.transport.model.VehicleReview;

@Component
public class VehicleReviewDTOMapper {

    private static ModelMapper modelMapper;

    @Autowired
    public VehicleReviewDTOMapper(ModelMapper modelMapper) {
        VehicleReviewDTOMapper.modelMapper = modelMapper;
    }

    public static VehicleReview fromDTOtoVehicleReview(VehicleReviewDTO dto) {
        return modelMapper.map(dto, VehicleReview.class);
    }

    public static VehicleReviewDTO fromVehicleReviewtoDTO(VehicleReview dto) {
        return modelMapper.map(dto, VehicleReviewDTO.class);
    }
}
