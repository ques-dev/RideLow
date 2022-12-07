package rs.ac.uns.ftn.transport.service.interfaces;

import rs.ac.uns.ftn.transport.model.User;

public interface IUserService {

    User save(User user);
    User findOne(Integer id);

}
