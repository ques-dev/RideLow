package rs.ac.uns.ftn.transport.controller;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import rs.ac.uns.ftn.transport.dto.*;
import rs.ac.uns.ftn.transport.dto.User.LoginDTO;
import rs.ac.uns.ftn.transport.dto.ride.RidePage2DTO;
import rs.ac.uns.ftn.transport.mapper.RideDTOMapper;
import rs.ac.uns.ftn.transport.mapper.UserDTOMapper;
import rs.ac.uns.ftn.transport.model.*;
import rs.ac.uns.ftn.transport.model.enumerations.MessageType;
import rs.ac.uns.ftn.transport.service.interfaces.*;
import rs.ac.uns.ftn.transport.utils.TokenUtils;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Locale;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin("http://localhost:4200")
@RestController
@RequestMapping(value = "/api/user")
public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;
    private final IUserService userService;
    private final IDriverService driverService;
    private final IPassengerService passengerService;

    private final IRideService rideService;
    private final MessageSource messageSource;
    private final IMailService mailService;
    private final TokenUtils tokenUtils;

    public UserController(IUserService userService,
                          IDriverService driverService,
                          IPassengerService passengerService,
                          IRideService rideService,
                          MessageSource messageSource,
                          IMailService mailService,
                          TokenUtils tokenUtils) {
        this.tokenUtils = tokenUtils;
        this.rideService = rideService;
        this.passengerService = passengerService;
        this.driverService = driverService;
        this.userService = userService;
        this.messageSource = messageSource;
        this.mailService = mailService;
    }

    @PutMapping(value = "/{id}/changePassword", consumes = "application/json")
    public ResponseEntity<?> changePassword(@PathVariable Integer id,
                                            @Valid @RequestBody ChangePasswordDTO dto) throws ConstraintViolationException {
        User user;
        try {
            user = userService.findOne(id);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(messageSource.getMessage("user.notFound", null, Locale.getDefault()), HttpStatus.NOT_FOUND);
        }

        if (!dto.getOldPassword().equals(user.getPassword())) {
            return new ResponseEntity<>(new ResponseMessage(messageSource.getMessage("user.passwordMatch", null, Locale.getDefault())), HttpStatus.BAD_REQUEST);
        }

        user.setPassword(dto.getNewPassword());
        userService.save(user);

        return new ResponseEntity<>("Password successfully changed!", HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/{id}/resetPassword")
    public ResponseEntity<?> resetPassword(@PathVariable Integer id) throws MessagingException, UnsupportedEncodingException {
        User user;
        try {
            user = userService.findOne(id);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(messageSource.getMessage("user.notFound", null, Locale.getDefault()), HttpStatus.NOT_FOUND);
        }

        String token = String.valueOf(new Random().nextInt(900000) + 100000); // 6-digit random number

        user.setResetPasswordToken(token);
        user.setResetPasswordTokenExpiration(LocalDateTime.now().plusMinutes(10));
        userService.save(user);

        mailService.sendMail(user.getEmail(), token);

        return new ResponseEntity<>("Email with reset code has been sent!", HttpStatus.NO_CONTENT);
    }

    @PutMapping(value = "/{id}/resetPassword", consumes = "application/json")
    public ResponseEntity<?> resetPassword(@PathVariable Integer id,
                                            @Valid @RequestBody ResetPasswordDTO dto) throws ConstraintViolationException {
        User user;
        try {
            user = userService.findOne(id);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(messageSource.getMessage("user.notFound", null, Locale.getDefault()), HttpStatus.NOT_FOUND);
        }

        if (user.getResetPasswordToken() == null || user.getResetPasswordTokenExpiration().isBefore(LocalDateTime.now()) || !user.getResetPasswordToken().equals(dto.getCode())) {
            return new ResponseEntity<>(new ResponseMessage(messageSource.getMessage("user.resetToken", null, Locale.getDefault())), HttpStatus.BAD_REQUEST);
        }

        user.setPassword(dto.getNewPassword());
        user.setResetPasswordToken(null);
        user.setResetPasswordTokenExpiration(null);
        userService.save(user);

        return new ResponseEntity<>("Password successfully changed!", HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/{id}/ride")
    public ResponseEntity<RidePage2DTO> findRides(@PathVariable Integer id, Pageable page){
        Page<Ride> rides = rideService.findPassenger(id, (page));
        if(rides.isEmpty()){
            rides = rideService.findAllByDriver_Id(id, (page));
        }
        Set<RideDTO> rideDTOs = rides.stream()
                .map(RideDTOMapper:: fromRidetoDTO)
                .collect(Collectors.toSet());
        return new ResponseEntity<>(new RidePage2DTO(rides.getTotalElements(),rideDTOs),HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<UserPageDTO> findUsers(Pageable page){
        Page<User> users = userService.findAll(page);
        Set<UserDTO> userDTOS = new HashSet<>();
        if(users.getTotalElements() != 0)
             userDTOS = users.stream().map(UserDTOMapper::fromUsertoDTO).collect(Collectors.toSet());

        return new ResponseEntity<>(new UserPageDTO(users.getTotalElements(), userDTOS), HttpStatus.OK);
    }

    @PostMapping(value = "/login", consumes = "application/json")
    public ResponseEntity<TokenDTO> login(
            @RequestBody LoginDTO authenticationRequest, HttpServletResponse response){

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticationRequest.getEmail(), authenticationRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = (User) authentication.getPrincipal();
        String jwt = tokenUtils.generateToken(user.getUsername());

        return ResponseEntity.ok(new TokenDTO(jwt, jwt));
    }

    @GetMapping(value = "/{id}/message")
    public ResponseEntity<MessagePageDTO> findUserMessages(@PathVariable Integer id){
        Set<MessageDTO> messageDTOS = userService.findMessagesOfUser(id);
        return new ResponseEntity<>(new MessagePageDTO((long) messageDTOS.size(), messageDTOS), HttpStatus.OK);
    }

    @PostMapping(value = "/{id}/message")
    public ResponseEntity<MessageLightDTO> createMessage(@PathVariable Integer id, @RequestBody MessageLightDTO messageLight){
        Message message = new Message();
        message.setSender(userService.findOne(id));
        message.setReceiver(userService.findOne(messageLight.getReceiverId()));
        message.setMessage(messageLight.getMessage());
        message.setSentDateTime(LocalDateTime.now());
        message.setMessageType(MessageType.VOZNJA);
        message.setRide(rideService.findOne(id));
        message = userService.SaveMessage(message);

        messageLight.setId(message.getId());
        messageLight.setTimeOfSending(message.getSentDateTime());
        messageLight.setSenderId(id);
        return new ResponseEntity<>(messageLight, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}/block")
    public ResponseEntity<String> blockUser(@PathVariable Integer id){
        userService.blockUser(id);
        return new ResponseEntity<>("User is successfully blocked", HttpStatus.NO_CONTENT);
    }
    @PutMapping(value = "/{id}/unblock")
    public ResponseEntity<String> unblockUser(@PathVariable Integer id){
        userService.unblockUser(id);
        return new ResponseEntity<>("User is successfully unblocked", HttpStatus.NO_CONTENT);
    }

    @PostMapping(value = "/{id}/note")
    public ResponseEntity<NoteLiteDTO> creatingNote(@PathVariable Integer id, @RequestBody Note note){
        note = userService.saveNote(id, note);
        return new ResponseEntity<>(new NoteLiteDTO(note), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/note")
    public ResponseEntity<NotePageDTO> findNotes(@PathVariable Integer id, Pageable page){
        NotePageDTO notePageDTO = userService.findNotes(id, page);
        return new ResponseEntity<>(notePageDTO, HttpStatus.OK);
    }
}
