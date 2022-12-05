package rs.ac.uns.ftn.transport.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import rs.ac.uns.ftn.transport.model.enumerations.RideStatus;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "rides")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Ride {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "StartTime", columnDefinition = "TIMESTAMP")
    private LocalDateTime startTime;

    @Column (name = "EndTime", columnDefinition = "TIMESTAMP")
    private LocalDateTime endTime;

    @Column (name = "TotalPrice")
    private Double totalCost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn (name = "DriverId")
    @ToString.Exclude
    private Driver driver;

    @ManyToMany(cascade = {CascadeType.MERGE})
    @ToString.Exclude
    private Set<Passenger> passengers;

    @ManyToMany(cascade = {CascadeType.PERSIST})
    @JoinTable(name = "Ride_route", joinColumns = @JoinColumn(name = "RideId", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "RouteId", referencedColumnName = "id"))
    @ToString.Exclude
    private Set<Route> locations;

    @Column(name = "EstimatedTimeInMinutes")
    private Integer estimatedTimeInMinutes;

    @OneToMany(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<Review> reviews;

    @Enumerated(EnumType.STRING)
    @Column(name = "Status")
    private RideStatus status;

    @OneToOne(mappedBy = "ride", fetch = FetchType.LAZY)
    @ToString.Exclude
    private Rejection rejection;

    @Column(name = "IsPanicPressed")
    private Boolean isPanicPressed;

    @Column(name = "TransportsBaby")
    private Boolean babyTransport;

    @Column(name = "TransportsPet")
    private Boolean petTransport;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "VehicleType")
    @ToString.Exclude
    private VehicleType vehicleType;
    

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Ride ride = (Ride) o;
        return id != null && Objects.equals(id, ride.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
