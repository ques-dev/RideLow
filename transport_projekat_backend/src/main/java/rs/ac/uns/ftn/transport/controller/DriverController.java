package rs.ac.uns.ftn.transport.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.transport.dto.DocumentDTO;
import rs.ac.uns.ftn.transport.dto.DriverDTO;
import rs.ac.uns.ftn.transport.mapper.DocumentDTOMapper;
import rs.ac.uns.ftn.transport.model.Document;
import rs.ac.uns.ftn.transport.model.Driver;
import rs.ac.uns.ftn.transport.model.enumerations.DocumentType;
import rs.ac.uns.ftn.transport.service.interfaces.IDocumentService;
import rs.ac.uns.ftn.transport.service.interfaces.IDriverService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value="api/driver")
public class DriverController {

    private final IDriverService driverService;
    private final IDocumentService documentService;

    public DriverController(IDriverService driverService,
                            IDocumentService documentService) {
        this.driverService = driverService;
        this.documentService = documentService;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<DriverDTO> getDriver(@PathVariable Integer id) {
        Driver driver = driverService.findOne(id);

        if (driver == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(new DriverDTO(driver), HttpStatus.OK);
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
}