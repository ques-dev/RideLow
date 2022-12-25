package rs.ac.uns.ftn.transport.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.validator.constraints.Length;

import java.util.Objects;

import static jakarta.persistence.InheritanceType.JOINED;

@Entity
@Table(name = "users")
@Inheritance(strategy = JOINED)
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", unique=true, nullable=false)
    private Integer id;

    @Column (name = "name", nullable = false)
    @Length(max = 100)
    @NotBlank
    private String name;

    @Column (name = "surname", nullable = false)
    @Length(max = 100)
    @NotBlank
    private String surname;

    @Column (name = "profilePicture")
    private String profilePicture;

    @Column (name = "telephoneNumber")
    @Length(max = 18)
    private String telephoneNumber;

    @Column (name = "email", nullable = false, unique = true)
    @Email(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")
    @NotBlank
    private String email;

    @Column (name = "address", nullable = false)
    @Length(max = 100)
    @NotBlank
    private String address;

    @Column (name = "password", nullable = false)
    @NotBlank
    private String password;

    @Column (name = "isActivated")
    private Boolean isActivated;

    @Column (name = "isBlocked")
    private Boolean isBlocked;

    @OneToOne
    @JoinColumn(name = "userActivation_id", referencedColumnName = "id")
    private UserActivation userActivation;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
