package ftn.uns.ac.rs.transport.model;

import jakarta.persistence.*;
import static jakarta.persistence.InheritanceType.JOINED;

//TODO: Mora verovatno da postoji polje da li je aktivan, inace bi mogao da se uloguje. Pitacu i sta znaci da je aktivan.
// Svakako treba da bude samo vozac u mogucnsti da to izmeni
@Entity
@Table(name = "User")
@Inheritance(strategy = JOINED)
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column (name = "Name", nullable = false)
    private String name;

    @Column (name = "Surname",nullable = false)
    private String surname;

    @Column (name = "ProfilePictureUrl")
    private String profilePictureUrl;

    @Column (name = "PhoneNumber",nullable = false)
    private String phoneNumber;

    @Column (name = "Email", nullable = false, unique = true)
    private String email;

    @Column (name = "Address", nullable = false)
    private String address;

    @Column (name = "Password",nullable = false)
    private String password;

    @Column (name = "IsActive")
    private Boolean isActive;

    @Column (name = "IsBlocked")
    private Boolean isBlocked;

}
