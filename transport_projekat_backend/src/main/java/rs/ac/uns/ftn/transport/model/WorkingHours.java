package rs.ac.uns.ftn.transport.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.text.SimpleDateFormat;
import java.util.Objects;

//TODO: Pitacu da li vozac ima fiksno radno vreme, i da li se generise svaki dan posebno. Isto cu ga pitati da li ima smisla
// da radno vreme ima id

@Entity
@Table(name = "workingHours")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class WorkingHours {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "shiftStart")
    private SimpleDateFormat shiftStart;

    @Column(name = "shiftEnd")
    private SimpleDateFormat shiftEnd;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driverId")
    @ToString.Exclude
    private Driver driver;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        WorkingHours that = (WorkingHours) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
