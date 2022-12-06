package rs.ac.uns.ftn.transport.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.transport.dto.VehicleReviewDTO;
import rs.ac.uns.ftn.transport.model.VehicleReview;
import rs.ac.uns.ftn.transport.service.interfaces.IReviewService;
import rs.ac.uns.ftn.transport.service.interfaces.IVehicleService;

@CrossOrigin("http://localhost:4200")
@RestController
@RequestMapping(value="api/review")
public class ReviewController {
    private final IReviewService reviewService;
    private final IVehicleService vehicleService;

    public ReviewController(IReviewService reviewService, IVehicleService vehicleService){
        this.reviewService = reviewService;
        this.vehicleService = vehicleService;
    }

    @PostMapping(value = "vehicle/{id}", consumes = "application/json")
    public ResponseEntity<VehicleReviewDTO> saveVehicleReview(@PathVariable Integer id, @RequestBody VehicleReview vehicleReview){
        vehicleReview.setVehicle(vehicleService.getVehicleById(id));
        vehicleReview = reviewService.saveVehicleReview(vehicleReview);
        return new ResponseEntity<>(new VehicleReviewDTO(vehicleReview), HttpStatus.CREATED);
    }
}
