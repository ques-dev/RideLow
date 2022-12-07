package rs.ac.uns.ftn.transport.service.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rs.ac.uns.ftn.transport.dto.TokenDTO;
import rs.ac.uns.ftn.transport.model.Passenger;
import rs.ac.uns.ftn.transport.model.User;

public interface IUserService {

    User save(User user);
    User findOne(Integer id);
    Page<User> findAll(Pageable page);
    Passenger findByLogin(User user);

    TokenDTO saveToken(User user);
}
