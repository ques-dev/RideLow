package rs.ac.uns.ftn.transport.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rs.ac.uns.ftn.transport.dto.DriverReviewDTO;
import rs.ac.uns.ftn.transport.model.DriverReview;

@Component
public class DriverReviewDTOMapper {
    private static ModelMapper modelMapper;

    @Autowired
    public DriverReviewDTOMapper(ModelMapper modelMapper) {
        DriverReviewDTOMapper.modelMapper = modelMapper;
    }

    public static DriverReview fromDTOtoDriverReview(DriverReviewDTO dto) {
        return modelMapper.map(dto, DriverReview.class);
    }

    public static DriverReviewDTO fromVehicleReviewtoDTO(DriverReview dto) {
        return modelMapper.map(dto, DriverReviewDTO.class);
    }
}
