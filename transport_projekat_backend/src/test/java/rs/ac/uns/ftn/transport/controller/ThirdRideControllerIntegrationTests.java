package rs.ac.uns.ftn.transport.controller;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.MessageSource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import rs.ac.uns.ftn.transport.dto.LocationDTO;
import rs.ac.uns.ftn.transport.dto.RouteDTO;
import rs.ac.uns.ftn.transport.dto.TokenDTO;
import rs.ac.uns.ftn.transport.dto.User.LoginDTO;
import rs.ac.uns.ftn.transport.dto.panic.PanicReasonDTO;
import rs.ac.uns.ftn.transport.dto.passenger.PassengerIdEmailDTO;
import rs.ac.uns.ftn.transport.dto.ride.FavoriteRideDTO;
import rs.ac.uns.ftn.transport.dto.ride.FavoriteRideWithoutIdDTO;
import rs.ac.uns.ftn.transport.dto.ride.RideCreatedDTO;
import rs.ac.uns.ftn.transport.dto.ride.RideCreationDTO;
import rs.ac.uns.ftn.transport.model.enumerations.RideStatus;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ThirdRideControllerIntegrationTests {
    @Autowired
    private TestRestTemplate restTemplatePlain;
    @Autowired
    private MessageSource messageSource;
    private TestRestTemplate restTemplatePassenger,restTemplatePassengerSecond,restTemplateDriver,restTemplateAdmin;
    private String passengerJwtToken,passengerJwtTokenSecond,driverJwtToken,adminJwtToken;

    private final String BASE_URL_PATH = "http://localhost:8080/api";

    private void instantiatePassengerRestTemplate(){
        RestTemplateBuilder builder = new RestTemplateBuilder(rt-> rt.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().add("Authorization", "Bearer "+ this.passengerJwtToken);
            return execution.execute(request, body);
        }));
        this.restTemplatePassenger = new TestRestTemplate(builder);
        RestTemplateBuilder builderSecond = new RestTemplateBuilder(rt-> rt.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().add("Authorization", "Bearer "+ this.passengerJwtTokenSecond);
            return execution.execute(request, body);
        }));
        this.restTemplatePassengerSecond = new TestRestTemplate(builderSecond);
    }

    private void instantiateDriverRestTemplate(){
        RestTemplateBuilder builder = new RestTemplateBuilder(rt-> rt.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().add("Authorization", "Bearer "+ this.driverJwtToken);
            return execution.execute(request, body);
        }));
        this.restTemplateDriver = new TestRestTemplate(builder);
    }

    private void instantiateAdminRestTemplate(){
        RestTemplateBuilder builder = new RestTemplateBuilder(rt-> rt.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().add("Authorization", "Bearer "+ this.adminJwtToken);
            return execution.execute(request, body);
        }));
        this.restTemplateAdmin = new TestRestTemplate(builder);
    }

    private void instantiateAllJwtTokens(){

        HttpEntity<LoginDTO> driverLoginCredentials = new HttpEntity<>(new LoginDTO("driver1@mail.com","Test1Test"));
        ResponseEntity<TokenDTO> loginDriver = this.restTemplatePlain.exchange("/api/user/login",
                HttpMethod.POST,
                driverLoginCredentials,
                TokenDTO.class);
        this.driverJwtToken = loginDriver.getBody().getAccessToken();

        HttpEntity<LoginDTO> passengerLoginCredentials = new HttpEntity<>(new LoginDTO("passenger1@mail.com","Test1Test"));
        ResponseEntity<TokenDTO> loginPassenger = restTemplatePlain.exchange("/api/user/login",
                HttpMethod.POST,
                passengerLoginCredentials,
                TokenDTO.class);
        this.passengerJwtToken = loginPassenger.getBody().getAccessToken();

        passengerLoginCredentials = new HttpEntity<>(new LoginDTO("passenger2@mail.com","Test1Test"));
        ResponseEntity<TokenDTO> loginPassengerSecond = restTemplatePlain.exchange("/api/user/login",
                HttpMethod.POST,
                passengerLoginCredentials,
                TokenDTO.class);
        this.passengerJwtTokenSecond = loginPassengerSecond.getBody().getAccessToken();

        HttpEntity<LoginDTO> adminLoginCredentials = new HttpEntity<>(new LoginDTO("admin1@mail.com","Test1Test"));
        ResponseEntity<TokenDTO> loginAdmin = this.restTemplatePlain.exchange("/api/user/login",
                HttpMethod.POST,
                adminLoginCredentials,
                TokenDTO.class);
        this.adminJwtToken = loginAdmin.getBody().getAccessToken();
    }

    @BeforeAll
    public void setupTemplates() {
        instantiateAllJwtTokens();
        instantiatePassengerRestTemplate();
        instantiateDriverRestTemplate();
        instantiateAdminRestTemplate();
    }

    @Test
    @DisplayName("Should create favourite ride if ride exists")
    @Order(1)
    public void ShouldCreateFavouriteRideIfRideExists(){
        LocationDTO location = new LocationDTO("Bulevar oslobodjenja 46", 45.0, 19.833549);
        RouteDTO route = new RouteDTO(location, location);

        PassengerIdEmailDTO passenger = new PassengerIdEmailDTO(1, "passenger1@mail.com");
        PassengerIdEmailDTO passengerSecond = new PassengerIdEmailDTO(2, "passenger2@mail.com");

        Set<RouteDTO> locations = new HashSet<>(Set.of(route));
        Set<PassengerIdEmailDTO> passengers = new HashSet<>(Set.of(passenger, passengerSecond));

        FavoriteRideWithoutIdDTO mockDTO = new FavoriteRideWithoutIdDTO();
        mockDTO.setFavoriteName("fav name");
        mockDTO.setVehicleType("STANDARD");
        mockDTO.setLocations(locations);
        mockDTO.setPassengers(passengers);
        mockDTO.setPetTransport(true);
        mockDTO.setBabyTransport(true);

        ResponseEntity<FavoriteRideDTO> favoriteRideDTOResponseEntity =
                this.restTemplatePassenger.exchange(this.BASE_URL_PATH + "/ride/favorites", HttpMethod.POST,
                        new HttpEntity<>(mockDTO), FavoriteRideDTO.class);
        FavoriteRideDTO favoriteRideDTO = favoriteRideDTOResponseEntity.getBody();

        assertEquals(HttpStatus.OK, favoriteRideDTOResponseEntity.getStatusCode());
        assertNotNull(favoriteRideDTO);
    }

    @Test
    @DisplayName("Should not create favourite ride if ride does not exist")
    public void shouldNotCreateFavouriteRideIfRideDoesNotExist(){

        ResponseEntity<String> response = this.restTemplatePassenger.exchange(this.BASE_URL_PATH + "/ride/favorites",
                HttpMethod.POST,
                null,
                String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("Should not accept ride if ride id is incorrect format")
    public void shouldNotAcceptRideIfRideIdIsIncorrectFormat(){
        ResponseEntity<String> response = this.restTemplatePassenger.exchange(this.BASE_URL_PATH + "/ride/faefea/accept",
                HttpMethod.PUT,
                null,
                String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("Should not accept ride if ride does not exist")
    public void shouldNotAcceptRideIfRideDoesNotExist(){
        ResponseEntity<String> response = this.restTemplateDriver.exchange(this.BASE_URL_PATH + "/ride/99999/accept",
                HttpMethod.PUT,
                null,
                String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DisplayName("Should not accept ride if passenger role")
    public void shouldNotAcceptRideIfPassengerRole(){
        ResponseEntity<String> response = this.restTemplatePassenger.exchange(this.BASE_URL_PATH + "/ride/1/accept",
                HttpMethod.PUT,
                null,
                String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DisplayName("Should not end ride if ride id is incorrect format")
    public void shouldNotEndRideIfRideIdIsIncorrectFormat(){
        ResponseEntity<String> response = this.restTemplateDriver.exchange(this.BASE_URL_PATH + "/ride/faefea/end",
                HttpMethod.PUT,
                null,
                String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("Should not end ride if ride does not exist")
    public void ShouldNotEndRideIfRideDoesNotExist(){
        ResponseEntity<String> response = this.restTemplateDriver.exchange(this.BASE_URL_PATH + "/ride/999999/end",
                HttpMethod.PUT,
                null,
                String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DisplayName("Should not end ride if passenger role")
    public void ShouldNotCancelRideIfPassengerRole(){
        ResponseEntity<String> response = this.restTemplatePassenger.exchange(this.BASE_URL_PATH + "/ride/1/end",
                HttpMethod.PUT,
                null,
                String.class);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    @DisplayName("Should cancel ride if ride exists")
    public void ShouldCancelRideIfRideExists(){
        ResponseEntity<String> response = this.restTemplatePassenger.exchange(this.BASE_URL_PATH + "/ride/1/cancel",
                HttpMethod.POST,
                null,
                String.class);

        assertEquals(HttpStatus.METHOD_NOT_ALLOWED, response.getStatusCode());
    }
}
