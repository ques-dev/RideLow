package rs.ac.uns.ftn.transport.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import rs.ac.uns.ftn.transport.dto.RideDTO;
import rs.ac.uns.ftn.transport.dto.RidePageDTO;
import rs.ac.uns.ftn.transport.dto.ride.RideCreatedDTO;
import rs.ac.uns.ftn.transport.dto.ride.RidePage2DTO;
import rs.ac.uns.ftn.transport.mapper.RideDTOMapper;
import rs.ac.uns.ftn.transport.mapper.ride.RideCreatedDTOMapper;
import rs.ac.uns.ftn.transport.model.Driver;
import rs.ac.uns.ftn.transport.model.Passenger;
import rs.ac.uns.ftn.transport.model.Ride;
import rs.ac.uns.ftn.transport.model.User;
import rs.ac.uns.ftn.transport.service.interfaces.IDriverService;
import rs.ac.uns.ftn.transport.service.interfaces.IPassengerService;
import rs.ac.uns.ftn.transport.service.interfaces.IRideService;
import rs.ac.uns.ftn.transport.service.interfaces.IUserService;

import java.awt.print.Pageable;
import java.util.Set;
import java.util.stream.Collectors;

@RequestMapping(value = "/api/user")
public class UserController {
    private final IUserService userService;
    private final IDriverService driverService;
    private final IPassengerService passengerService;

    private final IRideService rideService;

    public UserController(IUserService userService, IDriverService driverService, IPassengerService passengerService,
                          IRideService rideService){
        this.rideService = rideService;
        this.passengerService = passengerService;
        this.driverService = driverService;
        this.userService = userService;
    }

    @GetMapping(value = "/{id}/ride")
    public ResponseEntity<RidePage2DTO> findRides(@PathVariable Integer id, Pageable page){
        Page<Ride> rides = rideService.findPassenger(id, (org.springframework.data.domain.Pageable) page);
        if(rides.isEmpty()){
            rides = rideService.findAllByDriver_Id(id, (org.springframework.data.domain.Pageable) page);
        }
        Set<RideDTO> rideDTOs = rides.stream()
                .map(RideDTOMapper:: fromRidetoDTO)
                .collect(Collectors.toSet());
        return new ResponseEntity<>(new RidePage2DTO(rides.getTotalElements(),rideDTOs),HttpStatus.OK);
    }

    /*@GetMapping
    public ResponseEntity<UserPageDTO>*/

}
