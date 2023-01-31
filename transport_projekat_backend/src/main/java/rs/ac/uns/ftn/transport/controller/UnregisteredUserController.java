package rs.ac.uns.ftn.transport.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.transport.dto.ride.RideCreatedDTO;
import rs.ac.uns.ftn.transport.dto.ride.RideCreationDTO;
import rs.ac.uns.ftn.transport.dto.ride.RideEstimationDTO;
import rs.ac.uns.ftn.transport.mapper.ride.RideCreationDTOMapper;
import rs.ac.uns.ftn.transport.model.Ride;
import rs.ac.uns.ftn.transport.model.Route;
import rs.ac.uns.ftn.transport.service.EstimatesService;

@CrossOrigin("http://localhost:4200")
@RestController
@RequestMapping(value="/api/unregisteredUser")
public class UnregisteredUserController {


    private final EstimatesService estimatesService;

    public UnregisteredUserController(EstimatesService estimatesService) {
        this.estimatesService = estimatesService;
    }

    @PostMapping
    public ResponseEntity<RideEstimationDTO> getEstimation(@RequestBody RideCreationDTO ride){
        RideEstimationDTO estimationDTO = new RideEstimationDTO();
        estimationDTO.setEstimatedCost(100);
        estimationDTO.setEstimatedTimeInMinutes(10);
        return new ResponseEntity<>(estimationDTO, HttpStatus.OK);
    }
}
