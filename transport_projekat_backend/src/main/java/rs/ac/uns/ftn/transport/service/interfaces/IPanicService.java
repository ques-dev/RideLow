package rs.ac.uns.ftn.transport.service.interfaces;

import org.springframework.data.domain.Page;
import rs.ac.uns.ftn.transport.model.Panic;

import java.util.List;


public abstract class IPanicService {
    public abstract List<Panic> findAll();
}
