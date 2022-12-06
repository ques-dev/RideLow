package rs.ac.uns.ftn.transport.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import rs.ac.uns.ftn.transport.dto.RideDTO;
import rs.ac.uns.ftn.transport.service.interfaces.IPassengerService;
import rs.ac.uns.ftn.transport.service.interfaces.IUserService;

import java.awt.print.Pageable;

@RequestMapping(value = "/api/user")
public class UserController {
    private final IUserService userService;

    public UserController(IUserService userService){this.userService = userService;}

    @GetMapping(value = "/{id}/ride")
    public ResponseEntity<RideDTO> findRides(@PathVariable Integer id, Pageable page){

        return null;
    }

}
