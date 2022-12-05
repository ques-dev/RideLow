package rs.ac.uns.ftn.transport.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "panics")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Panic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    @ToString.Exclude
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currentRideId")
    @ToString.Exclude
    private Ride currentRide;

    @Column(name = "dateTime", columnDefinition = "TIMESTAMP")
    private LocalDateTime dateTime;

    @Column(name = "reason")
    private String reason;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Panic panic = (Panic) o;
        return id != null && Objects.equals(id, panic.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
