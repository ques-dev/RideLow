package rs.ac.uns.ftn.transport.dto;

import lombok.Data;

@Data
public class UserDTO {

    private Integer id;
    private String name;
    private String surname;
    private String profilePicture;
    private String telephoneNumber;
    private String email;
    private String address;
}
