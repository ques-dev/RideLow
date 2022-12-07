package rs.ac.uns.ftn.transport.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.transport.dto.MessageDTO;
import rs.ac.uns.ftn.transport.dto.TokenDTO;
import rs.ac.uns.ftn.transport.mapper.MessageDTOMapper;
import rs.ac.uns.ftn.transport.model.Message;
import rs.ac.uns.ftn.transport.model.Passenger;
import rs.ac.uns.ftn.transport.model.User;
import rs.ac.uns.ftn.transport.repository.MessageRepository;
import rs.ac.uns.ftn.transport.repository.UserRepository;
import rs.ac.uns.ftn.transport.service.interfaces.IUserService;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;

    private final MessageRepository messageRepository;

    public UserServiceImpl(UserRepository userRepository, MessageRepository messageRepository){
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
    }
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

    @Override
    public Passenger findByLogin(User user) {
        return (Passenger) userRepository.findByLogin(user.getEmail(), user.getPassword());
    }

    @Override
    public TokenDTO saveToken(User user) {
        return new TokenDTO(user, "1", "1");
    }

    @Override
    public Set<MessageDTO> findMessagesOfUser(Integer id) {
        Optional<User> userO = userRepository.findById(id);
        if(!userO.isPresent()){
            return null;
        }
        User user = userO.get();
        Set<Message> messages = messageRepository.findBySender(user);
        Set<MessageDTO> messageDTOS = messages.stream().map(MessageDTOMapper::fromMessagetoDTO).collect(Collectors.toSet());
        return messageDTOS;
    }

    @Override
    public Message SaveMessage(Message message) {
        return messageRepository.save(message);
    }

    @Override
    public void blockUser(Integer id) {
        User user = findOne(id);
        user.setIsBlocked(true);
        save(user);
    }
}
