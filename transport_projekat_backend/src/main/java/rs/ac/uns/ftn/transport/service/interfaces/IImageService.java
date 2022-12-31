package rs.ac.uns.ftn.transport.service.interfaces;

import org.springframework.http.ResponseEntity;

public interface IImageService {
    ResponseEntity<String> decodeAndValidateImage(String encodedImage);
}
