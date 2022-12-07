package rs.ac.uns.ftn.transport.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.transport.dto.LocationDTO;
import rs.ac.uns.ftn.transport.mapper.LocationDTOMapper;
import rs.ac.uns.ftn.transport.model.Location;
import rs.ac.uns.ftn.transport.service.interfaces.IVehicleService;

@RestController
@RequestMapping(value="/api/vehicle")
public class VehicleController {

    private final IVehicleService vehicleService;

    public VehicleController(IVehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @PutMapping(value = "/{id}/location", consumes = "application/json")
    public ResponseEntity<String> changeLocation(@PathVariable Integer id, @RequestBody LocationDTO currentLocation)
    {
        Location newLocation = LocationDTOMapper.fromDTOtoLocation(currentLocation);
        vehicleService.changeLocation(id,newLocation);
        return new ResponseEntity<>("Coordinates successfully updated.",HttpStatus.OK);
    }

}
