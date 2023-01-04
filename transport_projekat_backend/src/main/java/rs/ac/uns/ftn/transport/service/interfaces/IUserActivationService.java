package rs.ac.uns.ftn.transport.service.interfaces;

import rs.ac.uns.ftn.transport.model.UserActivation;

public interface IUserActivationService {

    UserActivation findOne(Integer id);
    UserActivation save(UserActivation activation);

}
