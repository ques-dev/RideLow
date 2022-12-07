package rs.ac.uns.ftn.transport.service.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rs.ac.uns.ftn.transport.dto.MessageDTO;
import rs.ac.uns.ftn.transport.dto.NotePageDTO;
import rs.ac.uns.ftn.transport.dto.TokenDTO;
import rs.ac.uns.ftn.transport.model.Message;
import rs.ac.uns.ftn.transport.model.Note;
import rs.ac.uns.ftn.transport.model.Passenger;
import rs.ac.uns.ftn.transport.model.User;

import java.util.Set;

public interface IUserService {

    User save(User user);
    User findOne(Integer id);
    Page<User> findAll(Pageable page);
    Passenger findByLogin(User user);
    TokenDTO saveToken(User user);
    Set<MessageDTO> findMessagesOfUser(Integer id);

    Message SaveMessage(Message message);

    void blockUser(Integer id);

    void unblockUser(Integer id);

    Note saveNote(Integer id, Note note);

    NotePageDTO findNotes(Integer id, Pageable page);
}
