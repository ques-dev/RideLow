package rs.ac.uns.ftn.transport.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.text.SimpleDateFormat;
import java.util.Objects;

//TODO:Pitacu za zivotni ve sta predstavlja

@Entity
@Table(name = "userActivations")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserActivation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(mappedBy = "userActivation")
    private User user;

    @Column(name = "dateCreated")
    private SimpleDateFormat dateCreated;

    @Column(name = "validUntil")
    private SimpleDateFormat validUntil;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserActivation that = (UserActivation) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
