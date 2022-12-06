package rs.ac.uns.ftn.transport.service;

import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.transport.model.Panic;
import rs.ac.uns.ftn.transport.repository.PanicRepository;
import rs.ac.uns.ftn.transport.service.interfaces.IPanicService;

import java.util.List;

@Service
public class PanicServiceImpl implements IPanicService {

    private final PanicRepository panicRepository;

    public PanicServiceImpl(PanicRepository panicRepository){this.panicRepository = panicRepository;}

    public List<Panic> findAll(){return panicRepository.findAll();}

    @Override
    public Panic save(Panic panic) {
        return panicRepository.save(panic);
    }
}
