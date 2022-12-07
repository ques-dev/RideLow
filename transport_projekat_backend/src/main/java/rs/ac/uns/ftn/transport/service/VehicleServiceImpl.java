package rs.ac.uns.ftn.transport.service;

import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.transport.model.Location;
import rs.ac.uns.ftn.transport.model.Vehicle;
import rs.ac.uns.ftn.transport.repository.VehicleRepository;
import rs.ac.uns.ftn.transport.service.interfaces.IVehicleService;

@Service
public class VehicleServiceImpl implements IVehicleService {

    private final VehicleRepository vehicleRepository;

    public VehicleServiceImpl(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public Vehicle save(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    public Vehicle getVehicleByDriver_Id(Integer id) {
        return vehicleRepository.getVehicleByDriver_Id(id);
    }

    public Vehicle getVehicleById(Integer id){return vehicleRepository.findById(id).orElseGet(null);}

    @Override
    public Vehicle changeLocation(Integer id, Location newLocation) {
        Vehicle toChange = vehicleRepository.findById(id).orElseGet(null);
        toChange.setCurrentLocation(newLocation);
        vehicleRepository.save(toChange);
        return toChange;
    }
}