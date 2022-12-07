package rs.ac.uns.ftn.transport.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.transport.model.User;
import rs.ac.uns.ftn.transport.repository.UserRepository;
import rs.ac.uns.ftn.transport.repository.VehicleReviewRepository;
import rs.ac.uns.ftn.transport.service.interfaces.IUserService;

@Service
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository){this.userRepository = userRepository;}
    public User save(User user)
    {
        return userRepository.save(user);
    }

    @Override
    public User findOne(Integer id) {
        return userRepository.findById(id).orElseGet(null);
    }

    @Override
    public Page<User> findAll(Pageable page) {
        return userRepository.findAll(page);
    }
}
