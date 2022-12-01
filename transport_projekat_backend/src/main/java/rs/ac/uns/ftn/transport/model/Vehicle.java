package rs.ac.uns.ftn.transport.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "vehicles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(mappedBy = "vehicle", fetch = FetchType.LAZY)
    private Driver driver;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicleType")
    private VehicleType vehicleType;

    @Column(name = "model")
    private String model;

    @Column(name = "licenseNumber")
    private String licenseNumber;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currentLocation")
    private Location currentLocation;

    @Column(name = "passengerSeats")
    private Integer passengerSeats;

    @Column(name = "babyTransport")
    private Boolean babyTransport;

    @Column(name = "petTransport")
    private Boolean petTransport;

    // TODO: vratiti kada review bude spreman
//    @OneToMany(mappedBy = "vehicle",fetch = FetchType.LAZY)
//    Set<Review> reviews;
}
