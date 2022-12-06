package rs.ac.uns.ftn.transport.service;

import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.transport.model.UserActivation;
import rs.ac.uns.ftn.transport.repository.UserActivationRepository;
import rs.ac.uns.ftn.transport.service.interfaces.IUserActivationService;

@Service
public class UserActivationService implements IUserActivationService {

    private final UserActivationRepository userActivationRepository;

    public UserActivationService(UserActivationRepository userActivationRepository) {
        this.userActivationRepository = userActivationRepository;
    }

    @Override
    public UserActivation findOne(Integer id) {
        return userActivationRepository.findById(id).orElseGet(null);
    }
}
