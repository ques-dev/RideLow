package rs.ac.uns.ftn.transport.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.transport.dto.DriverReviewDTO;
import rs.ac.uns.ftn.transport.dto.DriverReviewPageDTO;
import rs.ac.uns.ftn.transport.dto.VehicleReviewDTO;
import rs.ac.uns.ftn.transport.dto.VehicleReviewPageDTO;
import rs.ac.uns.ftn.transport.mapper.DriverReviewDTOMapper;
import rs.ac.uns.ftn.transport.mapper.VehicleReviewDTOMapper;
import rs.ac.uns.ftn.transport.model.DriverReview;
import rs.ac.uns.ftn.transport.model.VehicleReview;
import rs.ac.uns.ftn.transport.service.interfaces.IDriverService;
import rs.ac.uns.ftn.transport.service.interfaces.IReviewService;
import rs.ac.uns.ftn.transport.service.interfaces.IVehicleService;

import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin("http://localhost:4200")
@RestController
@RequestMapping(value="api/review")
public class ReviewController {
    private final IReviewService reviewService;
    private final IDriverService driverService;
    private final IVehicleService vehicleService;

    public ReviewController(IReviewService reviewService, IVehicleService vehicleService, IDriverService driverService){
        this.reviewService = reviewService;
        this.vehicleService = vehicleService;
        this.driverService = driverService;
    }

    @PostMapping(value = "vehicle/{id}", consumes = "application/json")
    public ResponseEntity<VehicleReviewDTO> saveVehicleReview(@PathVariable Integer id, @RequestBody VehicleReview vehicleReview){
        vehicleReview.setVehicle(vehicleService.getVehicleById(id));
        vehicleReview = reviewService.saveVehicleReview(vehicleReview);
        return new ResponseEntity<>(new VehicleReviewDTO(vehicleReview), HttpStatus.CREATED);
    }

    @GetMapping(value = "vehicle/{id}")
    public ResponseEntity<VehicleReviewPageDTO> getVehicleReviewsForVehicle(@PathVariable Integer id){
        Set<VehicleReview> reviews = reviewService.getVehicleReviewsofVehicle(id);

        Set<VehicleReviewDTO> vehicleReviewDTOS = reviews.stream()
                .map(VehicleReviewDTOMapper::fromVehicleReviewtoDTO)
                .collect(Collectors.toSet());

        return new ResponseEntity<>(new VehicleReviewPageDTO((long) reviews.size(), vehicleReviewDTOS), HttpStatus.OK);
    }

    @PostMapping(value = "driver/{id}", consumes = "application/json")
    public ResponseEntity<DriverReviewDTO> saveDriverReview(@PathVariable Integer id, @RequestBody DriverReview driverReview){
        driverReview.setDriver(driverService.findOne(id));
        driverReview = reviewService.saveDriverReview(driverReview);
        return new ResponseEntity<>(new DriverReviewDTO(driverReview), HttpStatus.CREATED);
    }

    @GetMapping(value = "driver/{id}")
    public ResponseEntity<DriverReviewPageDTO> getDriverReviewsForDriver(@PathVariable Integer id){
        Set<DriverReview> reviews = reviewService.getDriverReviewsofDriver(id);

        Set<DriverReviewDTO> driverReviewDTOS = reviews.stream()
                .map(DriverReviewDTOMapper::fromDriverReviewtoDTO)
                .collect(Collectors.toSet());

        return new ResponseEntity<>(new DriverReviewPageDTO((long) reviews.size(), driverReviewDTOS), HttpStatus.OK);
    }
}
