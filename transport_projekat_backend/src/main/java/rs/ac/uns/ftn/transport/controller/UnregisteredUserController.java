package rs.ac.uns.ftn.transport.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.transport.dto.ride.RideCreatedDTO;
import rs.ac.uns.ftn.transport.dto.ride.RideEstimationDTO;

@CrossOrigin("http://localhost:4200")
@RestController
@RequestMapping(value="api/unregisteredUser")
public class UnregisteredUserController {

    @PostMapping
    public ResponseEntity<RideEstimationDTO> getEstimation(@RequestBody RideCreatedDTO ride){
        RideEstimationDTO estimationDTO = new RideEstimationDTO();
        estimationDTO.setEstimatedCost(100);
        estimationDTO.setEstimatedTimeInMinutes(10);
        return new ResponseEntity<>(estimationDTO, HttpStatus.OK);
    }
}
