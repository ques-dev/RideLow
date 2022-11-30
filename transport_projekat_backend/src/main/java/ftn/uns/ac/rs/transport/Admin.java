package ftn.uns.ac.rs.transport;

import jakarta.persistence.*;

@Entity
@Table(name = "Admin")
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "Username", unique = true)
    private String username;

    @Column(name = "Password", nullable = false)
    private String password;

    @Column(name = "Name", nullable = false)
    private String name;

    @Column(name = "Surname", nullable = false)
    private String surname;

    @Column(name = "ProfilePictureUrl")
    private String profilePictureUrl;
}
