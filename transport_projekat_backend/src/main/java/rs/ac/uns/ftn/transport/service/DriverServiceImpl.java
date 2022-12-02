package rs.ac.uns.ftn.transport.service;

import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.transport.model.Driver;
import rs.ac.uns.ftn.transport.repository.DriverRepository;
import rs.ac.uns.ftn.transport.service.interfaces.IDriverService;

@Service
public class DriverServiceImpl implements IDriverService {

    private final DriverRepository driverRepository;

    public DriverServiceImpl(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    public Driver findOne(Integer id) {
        return driverRepository.findById(id).orElseGet(null);
    }

    public Driver save(Driver driver) {
        return driverRepository.save(driver);
    }
}
