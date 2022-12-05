package rs.ac.uns.ftn.transport.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

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
