package rs.ac.uns.ftn.transport.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Objects;

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

    @Column(name = "shiftStart", columnDefinition = "TIMESTAMP")
    private LocalDateTime start;
    @Column(name = "shiftEnd", columnDefinition = "TIMESTAMP")
    private LocalDateTime end;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driverId")
    @ToString.Exclude
    private Driver driver;

    public WorkingHours(LocalDateTime start, LocalDateTime end, Driver driver) {
        this.start = start;
        this.end = end;
        this.driver = driver;
    }

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