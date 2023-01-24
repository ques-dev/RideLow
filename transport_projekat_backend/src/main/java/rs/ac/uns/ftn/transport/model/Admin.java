package rs.ac.uns.ftn.transport.model;

import lombok.*;
import javax.persistence.*;

@Entity
@Table(name = "admins")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "surname", nullable = false)
    private String surname;

    @Column(name = "profilePicture")
    private String profilePicture;
}
