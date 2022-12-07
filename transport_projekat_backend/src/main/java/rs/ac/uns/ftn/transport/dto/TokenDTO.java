package rs.ac.uns.ftn.transport.dto;

import lombok.Data;
import rs.ac.uns.ftn.transport.model.User;

@Data
public class TokenDTO {
    private Integer id;
    private User user;
    String accessToken;
    String refreshToken;

    public TokenDTO(User user, String s, String s1) {
        this.user = user;
        this.accessToken = s;
        this.refreshToken = s1;
        id = 1;
    }
}
