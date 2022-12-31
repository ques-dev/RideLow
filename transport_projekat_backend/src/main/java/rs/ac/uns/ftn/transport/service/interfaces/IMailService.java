package rs.ac.uns.ftn.transport.service.interfaces;

import jakarta.mail.MessagingException;

import java.io.UnsupportedEncodingException;

public interface IMailService {
    void sendMail(String recipientEmail, String token) throws MessagingException, UnsupportedEncodingException;
}
