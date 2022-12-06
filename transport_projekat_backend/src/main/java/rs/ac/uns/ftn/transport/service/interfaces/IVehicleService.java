package rs.ac.uns.ftn.transport.service.interfaces;

import rs.ac.uns.ftn.transport.model.Vehicle;

public interface IVehicleService {
    Vehicle save(Vehicle vehicle);
    Vehicle getVehicleByDriver_Id(Integer id);

    Vehicle getVehicleById(Integer id);
}
