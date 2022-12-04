package rs.ac.uns.ftn.transport.controller;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.transport.dto.*;
import rs.ac.uns.ftn.transport.mapper.DocumentDTOMapper;
import rs.ac.uns.ftn.transport.mapper.DriverDTOMapper;
import rs.ac.uns.ftn.transport.mapper.VehicleDTOMapper;
import rs.ac.uns.ftn.transport.mapper.WorkingHoursDTOMapper;
import rs.ac.uns.ftn.transport.model.*;
import rs.ac.uns.ftn.transport.model.enumerations.DocumentType;
import rs.ac.uns.ftn.transport.service.interfaces.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value="api/driver")
public class DriverController {

    private final IDriverService driverService;
    private final IDocumentService documentService;
    private final IVehicleService vehicleService;
    private final ILocationService locationService;
    private final IWorkingHoursService workingHoursService;


    public DriverController(IDriverService driverService,
                            IDocumentService documentService,
                            IVehicleService vehicleService,
                            ILocationService locationService,
                            IWorkingHoursService workingHoursService) {
        this.driverService = driverService;
        this.documentService = documentService;
        this.vehicleService = vehicleService;
        this.locationService = locationService;
        this.workingHoursService = workingHoursService;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<DriverDTO> getDriver(@PathVariable Integer id) {
        Driver driver = driverService.findOne(id);

        if (driver == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(new DriverDTO(driver), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<DriverPageDTO> getDrivers(Pageable page) {
        Page<Driver> drivers = driverService.findAll(page);

        Set<DriverDTO> driverDTOs = drivers.stream()
                .map(DriverDTOMapper::fromDrivertoDTO)
                .collect(Collectors.toSet());

        return new ResponseEntity<>(new DriverPageDTO(drivers.getTotalElements(), driverDTOs), HttpStatus.OK);
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<DriverDTO> saveDriver(@RequestBody Driver driver) {
        driver = driverService.save(driver);
        return new ResponseEntity<>(new DriverDTO(driver), HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", consumes = "application/json")
    public ResponseEntity<DriverDTO> updateDriver(@PathVariable Integer id, @RequestBody Driver driver) {
        Driver driverToUpdate = driverService.findOne(id);

        driverToUpdate.setName(driver.getName());
        driverToUpdate.setSurname(driver.getSurname());
        driverToUpdate.setProfilePicture(driver.getProfilePicture());
        driverToUpdate.setTelephoneNumber(driver.getTelephoneNumber());
        driverToUpdate.setEmail(driver.getEmail());
        driverToUpdate.setAddress(driver.getAddress());

        driverToUpdate = driverService.save(driverToUpdate);
        return new ResponseEntity<>(new DriverDTO(driverToUpdate), HttpStatus.OK);
    }

    @PostMapping(value = "/{id}/documents", consumes = "application/json")
    public ResponseEntity<DocumentDTO> saveDocument(@PathVariable Integer id, @RequestBody DocumentDTO documentDTO) {
        Driver driver = driverService.findOne(id);

        Document document = new Document(DocumentType.getEnum(documentDTO.getName()), documentDTO.getDocumentImage(), driver);

        document = documentService.save(document);
        return new ResponseEntity<>(new DocumentDTO(document), HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}/documents")
    public ResponseEntity<Set<DocumentDTO>> getDocuments(@PathVariable Integer id) {
        Set<Document> documents = documentService.findAllByDriver_Id(id);

        Set<DocumentDTO> documentDTOs = documents.stream()
                                    .map(DocumentDTOMapper::fromDocumenttoDTO)
                                    .collect(Collectors.toSet());
        return new ResponseEntity<>(documentDTOs, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}/documents")
    @Transactional
    public ResponseEntity<String> deleteDocuments(@PathVariable Integer id) {
        if (documentService.deleteAllByDriver_Id(id) > 0) {
            return new ResponseEntity<>("Driver's documents deleted successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid request", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/{id}/vehicle", consumes = "application/json")
    public ResponseEntity<VehicleDTO> saveVehicle(@PathVariable Integer id, @RequestBody VehicleDTO vehicleDTO) {
        Driver driver = driverService.findOne(id);

        Vehicle vehicle = VehicleDTOMapper.fromDTOtoVehicle(vehicleDTO);
        vehicle.setDriver(driver);

        Location location = vehicle.getCurrentLocation();
        locationService.save(location);

        vehicle = vehicleService.save(vehicle);

        driver.setVehicle(vehicle);
        driverService.save(driver);
        return new ResponseEntity<>(VehicleDTOMapper.fromVehicletoDTO(vehicle), HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}/vehicle")
    public ResponseEntity<VehicleDTO> getVehicle(@PathVariable Integer id) {
        Vehicle vehicle = vehicleService.getVehicleByDriver_Id(id);

        if (vehicle == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(VehicleDTOMapper.fromVehicletoDTO(vehicle), HttpStatus.OK);
    }

    @PutMapping(value = "/{id}/vehicle", consumes = "application/json")
    public ResponseEntity<VehicleDTO> updateVehicle(@PathVariable Integer id, @RequestBody VehicleDTO vehicleDTO) {
        Vehicle oldVehicle = vehicleService.getVehicleByDriver_Id(id);

        if (oldVehicle == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Vehicle newVehicle = VehicleDTOMapper.fromDTOtoVehicle(vehicleDTO);

        oldVehicle.setVehicleType(newVehicle.getVehicleType());
        oldVehicle.setModel(newVehicle.getModel());
        oldVehicle.setLicenseNumber(newVehicle.getLicenseNumber());

        oldVehicle.setCurrentLocation(newVehicle.getCurrentLocation());
        locationService.save(oldVehicle.getCurrentLocation());

        oldVehicle.setPassengerSeats(newVehicle.getPassengerSeats());
        oldVehicle.setBabyTransport(newVehicle.getBabyTransport());
        oldVehicle.setPetTransport(newVehicle.getPetTransport());

        oldVehicle = vehicleService.save(oldVehicle);
        return new ResponseEntity<>(VehicleDTOMapper.fromVehicletoDTO(oldVehicle), HttpStatus.OK);
    }

    @PostMapping(value = "/{id}/working-hours")
    public ResponseEntity<WorkingHoursDTO> saveWorkingHours(@PathVariable Integer id) {
        Driver driver = driverService.findOne(id);

        if (driver == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        WorkingHours workingHours = new WorkingHours(LocalDateTime.now(), LocalDateTime.now(), driver);
        workingHours = workingHoursService.save(workingHours);

        return new ResponseEntity<>(WorkingHoursDTOMapper.fromWorkingHoursToDTO(workingHours), HttpStatus.CREATED);
    }

    @GetMapping(value = "/{driverId}/working-hour/{workingHourId}")
    public ResponseEntity<WorkingHoursDTO> getWorkingHour(@PathVariable Integer driverId, @PathVariable Integer workingHourId) {
        Driver driver = driverService.findOne(driverId);
        WorkingHours workingHours = workingHoursService.findOne(workingHourId);

        if (workingHours == null || !workingHours.getDriver().equals(driver)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(WorkingHoursDTOMapper.fromWorkingHoursToDTO(workingHours), HttpStatus.OK);
    }

    @PutMapping(value = "/{driverId}/working-hour/{workingHourId}")
    public ResponseEntity<WorkingHoursDTO> updateWorkingHour(@PathVariable Integer driverId, @PathVariable Integer workingHourId) {
        Driver driver = driverService.findOne(driverId);
        WorkingHours workingHours = workingHoursService.findOne(workingHourId);

        if (workingHours == null || !workingHours.getDriver().equals(driver)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        workingHours.setEnd(LocalDateTime.now());

        workingHours = workingHoursService.save(workingHours);
        return new ResponseEntity<>(WorkingHoursDTOMapper.fromWorkingHoursToDTO(workingHours), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/working-hours")
    public ResponseEntity<WorkingHoursPageDTO> getWorkingHours(Pageable page,
                                                               @PathVariable Integer id,
                                                               @RequestParam(value = "from", required = false) LocalDateTime from,
                                                               @RequestParam(value = "to", required = false) LocalDateTime to) {
        Driver driver = driverService.findOne(id);

        if (driver == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Page<WorkingHours> workingHours;

        if (from == null && to == null) {
            workingHours = workingHoursService.findAllByDriver_Id(id, page);
        } else if (from != null && to == null) {
            workingHours = workingHoursService.findAllByDriver_IdAndStartIsAfter(id, from, page);
        } else if (from == null) {
            workingHours = workingHoursService.findAllByDriver_IdAndEndIsBefore(id, to, page);
        } else {
            workingHours = workingHoursService.findAllByDriver_IdAndStartIsAfterAndEndIsBefore(id, from, to, page);
        }

        Set<WorkingHoursDTO> workingHoursDTOs = workingHours.stream()
                .map(WorkingHoursDTOMapper::fromWorkingHoursToDTO)
                .collect(Collectors.toSet());

        return new ResponseEntity<>(new WorkingHoursPageDTO(workingHours.getTotalElements(), workingHoursDTOs), HttpStatus.OK);
    }
}