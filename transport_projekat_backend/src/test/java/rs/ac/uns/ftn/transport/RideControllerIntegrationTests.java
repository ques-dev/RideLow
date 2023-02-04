package rs.ac.uns.ftn.transport;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
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
import rs.ac.uns.ftn.transport.dto.passenger.PassengerIdEmailDTO;
import rs.ac.uns.ftn.transport.dto.ride.FavoriteRideDTO;
import rs.ac.uns.ftn.transport.dto.ride.FavoriteRideWithoutIdDTO;
import rs.ac.uns.ftn.transport.dto.ride.RideCreatedDTO;
import rs.ac.uns.ftn.transport.dto.ride.RideCreationDTO;
import rs.ac.uns.ftn.transport.model.enumerations.RideStatus;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RideControllerIntegrationTests {
    @Autowired
    private TestRestTemplate restTemplatePlain;
    @Autowired
    private MessageSource messageSource;
    private TestRestTemplate restTemplatePassenger,restTemplateDriver,restTemplateAdmin;
    private String passengerJwtToken,driverJwtToken,adminJwtToken;
    private final String BASE_URL_PATH = "http://localhost:8080/api";

    private void instantiatePassengerRestTemplate(){
        RestTemplateBuilder builder = new RestTemplateBuilder(rt-> rt.getInterceptors().add((request, body, execution) -> {
        request.getHeaders().add("Authorization", "Bearer "+ this.passengerJwtToken);
        return execution.execute(request, body);
        }));
        this.restTemplatePassenger = new TestRestTemplate(builder);
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
    @DisplayName("Should not find active ride for driver that doesn't have one")
    public void shouldNotGetActiveRideForDriverIfNone() {
        ResponseEntity<String> responseEntity = this.restTemplateDriver.exchange(this.BASE_URL_PATH + "/ride/driver/1/active",
                HttpMethod.GET,
                null,
                String.class);

        String errorMessage = responseEntity.getBody();
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(errorMessage,messageSource.getMessage("activeRide.notFound", null, Locale.getDefault()));
    }

    @Test
    @DisplayName("Should not find active ride for driver without passed JWT")
    public void shouldNotGetActiveRideForDriverIfUnauthorized() {
        ResponseEntity<String> responseEntity = this.restTemplatePlain.exchange(this.BASE_URL_PATH + "/ride/driver/1/active",
                HttpMethod.GET,
                null,
                String.class);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Should not find active ride for driver if JWT role is not correct")
    public void shouldNotGetActiveRideForDriverIfNotDriver() {
        ResponseEntity<String> responseEntity = this.restTemplatePassenger.exchange(this.BASE_URL_PATH + "/ride/driver/1/active",
                HttpMethod.GET,
                null,
                String.class);
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Should not find active ride for driver if passed id is in incorrect format")
    public void shouldNotGetActiveRideForDriverIfIdIsNotNumber() {
        ResponseEntity<String> responseEntity = this.restTemplateDriver.exchange(this.BASE_URL_PATH + "/ride/driver/abc/active",
                HttpMethod.GET,
                null,
                String.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Should not find active ride for driver if he does not exist")
    public void shouldNotGetActiveRideForDriverIfHeDoesNotExist() {
        ResponseEntity<String> responseEntity = this.restTemplateDriver.exchange(this.BASE_URL_PATH + "/ride/driver/20/active",
                HttpMethod.GET,
                null,
                String.class);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }


    @Test
    @DisplayName("Should not find active ride for passenger that doesn't have one")
    public void shouldNotGetActiveRideForPassengerIfNone() {
        ResponseEntity<String> responseEntity = this.restTemplatePassenger.exchange(this.BASE_URL_PATH + "/ride/passenger/2/active",
                HttpMethod.GET,
                null,
                String.class);

        String errorMessage = responseEntity.getBody();
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(errorMessage,messageSource.getMessage("activeRide.notFound", null, Locale.getDefault()));
    }

    @Test
    @DisplayName("Should not find active ride for passenger without passed JWT")
    public void shouldNotGetActiveRideForPassengerIfUnauthorized() {
        ResponseEntity<String> responseEntity = this.restTemplatePlain.exchange(this.BASE_URL_PATH + "/ride/passenger/1/active",
                HttpMethod.GET,
                null,
                String.class);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Should not find active ride for passenger if JWT role is not correct")
    public void shouldNotGetActiveRideForPassengerIfNotPassenger() {
        ResponseEntity<String> responseEntity = this.restTemplateDriver.exchange(this.BASE_URL_PATH + "/ride/passenger/1/active",
                HttpMethod.GET,
                null,
                String.class);
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Should not find active ride for passenger if passed id is in incorrect format")
    public void shouldNotGetActiveRideForPassengerIfIdIsNotNumber() {
        ResponseEntity<String> responseEntity = this.restTemplatePassenger.exchange(this.BASE_URL_PATH + "/ride/passenger/abc/active",
                HttpMethod.GET,
                null,
                String.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Should not find active ride for passenger if he does not exist")
    public void shouldNotGetActiveRideForPassengerIfHeDoesNotExist() {
        ResponseEntity<String> responseEntity = this.restTemplatePassenger.exchange(this.BASE_URL_PATH + "/ride/passenger/20/active",
                HttpMethod.GET,
                null,
                String.class);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }


    @Test
    @DisplayName("Should find an active ride for both passenger and chosen driver if it exists")
    public void shouldGetActiveRideForPassengerAndDriverIfItExists() {
        LocationDTO location = new LocationDTO("Bulevar oslobodjenja 46",45.267136,19.833549);
        RouteDTO route = new RouteDTO(location,location);
        PassengerIdEmailDTO passenger = new PassengerIdEmailDTO(1,"passenger1@mail.com");
        Set<RouteDTO> locations = new HashSet<>();
        locations.add(route);
        Set<PassengerIdEmailDTO> passengers = new HashSet<>();
        passengers.add(passenger);
        RideCreationDTO rideOrder = new RideCreationDTO(locations,
                                                        passengers,
                                                        "STANDARD",
                                                        true,
                                                        true,
                                                        null);

        ResponseEntity<RideCreatedDTO> mockRide = this.restTemplatePassenger.exchange(this.BASE_URL_PATH + "/ride",
                HttpMethod.POST,new HttpEntity<>(rideOrder),RideCreatedDTO.class);
        int createdRideId = mockRide.getBody().getId();
        int chosenDriverId = mockRide.getBody().getDriver().getId();
        this.restTemplateDriver.exchange(this.BASE_URL_PATH + "/ride/" + createdRideId + "/accept",
                HttpMethod.PUT,
                null,
                RideCreatedDTO.class);
        this.restTemplateDriver.exchange(this.BASE_URL_PATH + "/ride/" + createdRideId + "/start",
                HttpMethod.PUT,
                null,
                RideCreatedDTO.class);
        ResponseEntity<RideCreatedDTO> getPassengerActiveRide = this.restTemplatePassenger.exchange(this.BASE_URL_PATH + "/ride/passenger/1/active",
                HttpMethod.GET,
                null,
                RideCreatedDTO.class);
        assertEquals(HttpStatus.OK, getPassengerActiveRide.getStatusCode());
        assertEquals(RideStatus.ACTIVE,getPassengerActiveRide.getBody().getStatus());
        ResponseEntity<RideCreatedDTO> getDriverActiveRide = this.restTemplateDriver.exchange(this.BASE_URL_PATH + "/ride/driver/" + chosenDriverId + "/active",
                HttpMethod.GET,
                null,
                RideCreatedDTO.class);
        assertEquals(HttpStatus.OK, getDriverActiveRide.getStatusCode());
        assertEquals(RideStatus.ACTIVE,getDriverActiveRide.getBody().getStatus());
    }

    @Test
    @DisplayName("Should not get ride details if unauthorized")
    public void shouldNotGetRideDetailsIfUnauthorized() {
        ResponseEntity<String> responseEntity = this.restTemplatePlain.exchange(this.BASE_URL_PATH + "/ride/1",
                HttpMethod.GET,
                null,
                String.class);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Should not get ride details if the ride ID is not a valid number")
    public void shouldNotGetRideDetailsIfIdIsNotNumber() {
        ResponseEntity<String> responseEntity = this.restTemplateDriver.exchange(this.BASE_URL_PATH + "/ride/abc",
                HttpMethod.GET,
                null,
                String.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Should not get ride details if ride does not exist")
    public void shouldNotGetRideDetailsIfDoesntExist() {
        ResponseEntity<String> responseEntity = this.restTemplateDriver.exchange(this.BASE_URL_PATH + "/ride/20",
                HttpMethod.GET,
                null,
                String.class);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Should get ride details if ride does exist")
    public void shouldGetRideDetailsIfExists() {
        ResponseEntity<String> getRideDetailsDriver = this.restTemplateDriver.exchange(this.BASE_URL_PATH + "/ride/1",
                HttpMethod.GET,
                null,
                String.class);
        assertEquals(HttpStatus.OK, getRideDetailsDriver.getStatusCode());
        ResponseEntity<String> getRideDetailsPassenger = this.restTemplatePassenger.exchange(this.BASE_URL_PATH + "/ride/1",
                HttpMethod.GET,
                null,
                String.class);
        assertEquals(HttpStatus.OK, getRideDetailsPassenger.getStatusCode());
        ResponseEntity<String> getRideDetailsAdmin = this.restTemplateAdmin.exchange(this.BASE_URL_PATH + "/ride/1",
                HttpMethod.GET,
                null,
                String.class);
        assertEquals(HttpStatus.OK, getRideDetailsAdmin.getStatusCode());
    }

    @Test
    @DisplayName("Should not get favorite locations if unauthorized")
    public void shouldNotGetFavoriteLocationsIfUnauthorized() {
        ResponseEntity<String> responseEntity = this.restTemplatePlain.exchange(this.BASE_URL_PATH + "/ride/favorites",
                HttpMethod.GET,
                null,
                String.class);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Should not get favorite locations if logged user's role not passenger")
    public void shouldNotGetFavoriteLocationsIfNotPassenger() {
        ResponseEntity<String> responseEntity = this.restTemplateDriver.exchange(this.BASE_URL_PATH + "/ride/favorites",
                HttpMethod.GET,
                null,
                String.class);
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Should get passengers favorite rides")
    public void shouldGetPassengersFavoriteLocations() {
        ResponseEntity<Set<FavoriteRideDTO>> getFavoriteRides = this.restTemplatePassenger.exchange(this.BASE_URL_PATH + "/ride/favorites",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>(){});
        assertEquals(HttpStatus.OK, getFavoriteRides.getStatusCode());
        assertEquals(0, getFavoriteRides.getBody().size());

        LocationDTO location = new LocationDTO("Bulevar oslobodjenja 46",45.267136,19.833549);
        RouteDTO route = new RouteDTO(location,location);
        PassengerIdEmailDTO passenger = new PassengerIdEmailDTO(1,"passenger1@mail.com");
        Set<RouteDTO> locations = new HashSet<>();
        locations.add(route);
        Set<PassengerIdEmailDTO> passengers = new HashSet<>();
        passengers.add(passenger);
        FavoriteRideWithoutIdDTO favoriteRide = new FavoriteRideWithoutIdDTO(
                "Home - work",
                locations,
                passengers,
                true,
                true,
                "STANDARD");

        this.restTemplatePassenger.exchange(this.BASE_URL_PATH + "/ride/favorites",
                HttpMethod.POST,new HttpEntity<>(favoriteRide),FavoriteRideDTO.class);

        getFavoriteRides = this.restTemplatePassenger.exchange(this.BASE_URL_PATH + "/ride/favorites",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>(){});
        assertEquals(HttpStatus.OK, getFavoriteRides.getStatusCode());
        assertEquals(1, getFavoriteRides.getBody().size());
    }

    @Test
    @DisplayName("Should not create ride if user is unauthorized")
    public void shouldNotCreateRideIfUnauthorized() {
        ResponseEntity<String> responseEntity = this.restTemplatePlain.exchange(this.BASE_URL_PATH + "/ride",
                HttpMethod.POST,
                null,
                String.class);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Should not create ride if user's role is not passenger")
    public void shouldNotCreateRideIfForbidden() {
        ResponseEntity<String> responseEntity = this.restTemplateDriver.exchange(this.BASE_URL_PATH + "/ride",
                HttpMethod.POST,
                null,
                String.class);
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Should not create ride if locations field format is not valid")
    public void shouldNotCreateRideIfLocationsFormatInvalid() {
        LocationDTO location = new LocationDTO("Bulevar oslobodjenja 46",-91.0,19.833549);
        RouteDTO route = new RouteDTO(location,location);
        PassengerIdEmailDTO passenger = new PassengerIdEmailDTO(1,"passenger1@mail.com");
        Set<RouteDTO> locations = new HashSet<>();
        locations.add(route);
        Set<PassengerIdEmailDTO> passengers = new HashSet<>();
        passengers.add(passenger);
        RideCreationDTO rideOrder = new RideCreationDTO(locations,
                passengers,
                "STANDARD",
                true,
                true,
                null);

        ResponseEntity<RideCreatedDTO> rideOrderRequest = this.restTemplatePassenger.exchange(this.BASE_URL_PATH + "/ride",
                HttpMethod.POST,new HttpEntity<>(rideOrder),RideCreatedDTO.class);
        assertEquals(HttpStatus.BAD_REQUEST,rideOrderRequest.getStatusCode());
        locations.remove(route);
        rideOrderRequest = this.restTemplatePassenger.exchange(this.BASE_URL_PATH + "/ride",
                HttpMethod.POST,new HttpEntity<>(rideOrder),RideCreatedDTO.class);
        assertEquals(HttpStatus.BAD_REQUEST,rideOrderRequest.getStatusCode());
        location.setLatitude(90.1);
        rideOrderRequest = this.restTemplatePassenger.exchange(this.BASE_URL_PATH + "/ride",
                HttpMethod.POST,new HttpEntity<>(rideOrder),RideCreatedDTO.class);
        assertEquals(HttpStatus.BAD_REQUEST,rideOrderRequest.getStatusCode());
        location.setLatitude(19.0);
        location.setLongitude(-180.1);
        rideOrderRequest = this.restTemplatePassenger.exchange(this.BASE_URL_PATH + "/ride",
                HttpMethod.POST,new HttpEntity<>(rideOrder),RideCreatedDTO.class);
        assertEquals(HttpStatus.BAD_REQUEST,rideOrderRequest.getStatusCode());
        location.setLongitude(180.1);
        rideOrderRequest = this.restTemplatePassenger.exchange(this.BASE_URL_PATH + "/ride",
                HttpMethod.POST,new HttpEntity<>(rideOrder),RideCreatedDTO.class);
        assertEquals(HttpStatus.BAD_REQUEST,rideOrderRequest.getStatusCode());
        location.setLongitude(null);
        rideOrderRequest = this.restTemplatePassenger.exchange(this.BASE_URL_PATH + "/ride",
                HttpMethod.POST,new HttpEntity<>(rideOrder),RideCreatedDTO.class);
        assertEquals(HttpStatus.BAD_REQUEST,rideOrderRequest.getStatusCode());
        location.setLongitude(45.0);
        location.setLatitude(null);
        rideOrderRequest = this.restTemplatePassenger.exchange(this.BASE_URL_PATH + "/ride",
                HttpMethod.POST,new HttpEntity<>(rideOrder),RideCreatedDTO.class);
        assertEquals(HttpStatus.BAD_REQUEST,rideOrderRequest.getStatusCode());
        location.setLatitude(45.0);
        location.setAddress("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" +
                "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" +
                "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" +
                "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" +
                "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        rideOrderRequest = this.restTemplatePassenger.exchange(this.BASE_URL_PATH + "/ride",
                HttpMethod.POST,new HttpEntity<>(rideOrder),RideCreatedDTO.class);
        assertEquals(HttpStatus.BAD_REQUEST,rideOrderRequest.getStatusCode());
    }

    @Test
    @DisplayName("Should not create ride if passengers field format is not valid")
    public void shouldNotCreateRideIfPassengerFormatInvalid() {
        LocationDTO location = new LocationDTO("Bulevar oslobodjenja 46",45.0,19.833549);
        RouteDTO route = new RouteDTO(location,location);
        PassengerIdEmailDTO passenger = new PassengerIdEmailDTO(1,"passenger1@mail.com");
        Set<RouteDTO> locations = new HashSet<>();
        locations.add(route);
        Set<PassengerIdEmailDTO> passengers = new HashSet<>();
        RideCreationDTO rideOrder = new RideCreationDTO(locations,
                passengers,
                "STANDARD",
                true,
                true,
                null);

        ResponseEntity<RideCreatedDTO> rideOrderRequest = this.restTemplatePassenger.exchange(this.BASE_URL_PATH + "/ride",
                HttpMethod.POST,new HttpEntity<>(rideOrder),RideCreatedDTO.class);
        assertEquals(HttpStatus.BAD_REQUEST,rideOrderRequest.getStatusCode());

        passengers.add(passenger);
        passenger.setId(-1);
        rideOrderRequest = this.restTemplatePassenger.exchange(this.BASE_URL_PATH + "/ride",
                HttpMethod.POST,new HttpEntity<>(rideOrder),RideCreatedDTO.class);
        assertEquals(HttpStatus.BAD_REQUEST,rideOrderRequest.getStatusCode());

        passenger.setId(1);
        passenger.setEmail("");
        rideOrderRequest = this.restTemplatePassenger.exchange(this.BASE_URL_PATH + "/ride",
                HttpMethod.POST,new HttpEntity<>(rideOrder),RideCreatedDTO.class);
        assertEquals(HttpStatus.BAD_REQUEST,rideOrderRequest.getStatusCode());

        passenger.setEmail("invalidformat.com");
        rideOrderRequest = this.restTemplatePassenger.exchange(this.BASE_URL_PATH + "/ride",
                HttpMethod.POST,new HttpEntity<>(rideOrder),RideCreatedDTO.class);
        assertEquals(HttpStatus.BAD_REQUEST,rideOrderRequest.getStatusCode());

        passenger.setEmail("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" +
                "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" +
                "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" +
                "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" +
                "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        rideOrderRequest = this.restTemplatePassenger.exchange(this.BASE_URL_PATH + "/ride",
                HttpMethod.POST,new HttpEntity<>(rideOrder),RideCreatedDTO.class);
        assertEquals(HttpStatus.BAD_REQUEST,rideOrderRequest.getStatusCode());
    }
}
