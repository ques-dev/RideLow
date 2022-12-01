package rs.ac.uns.ftn.transport.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static jakarta.persistence.InheritanceType.JOINED;

//TODO: Mora verovatno da postoji polje da li je aktivan, inace bi mogao da se uloguje. Pitacu i sta znaci da je aktivan.
// Svakako treba da bude samo vozac u mogucnsti da to izmeni
@Entity
@Table(name = "users")
@Inheritance(strategy = JOINED)
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", unique=true, nullable=false)
    private Integer id;

    @Column (name = "name", nullable = false)
    private String name;

    @Column (name = "surname", nullable = false)
    private String surname;

    @Column (name = "profilePicture")
    private String profilePicture;

    @Column (name = "telephoneNumber",nullable = false)
    private String telephoneNumber;

    @Column (name = "email", nullable = false, unique = true)
    private String email;

    @Column (name = "address", nullable = false)
    private String address;

    @Column (name = "password", nullable = false)
    private String password;

    @Column (name = "isActive")
    private Boolean isActive;

    @Column (name = "isBlocked")
    private Boolean isBlocked;

    @OneToOne
    @JoinColumn(name = "userActivation_id", referencedColumnName = "id")
    private UserActivation userActivation;

}
