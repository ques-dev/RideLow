package rs.ac.uns.ftn.transport.service.interfaces;

import rs.ac.uns.ftn.transport.model.Driver;

public interface IDriverService {
    Driver findOne(Integer id);
    Driver save(Driver driver);
}