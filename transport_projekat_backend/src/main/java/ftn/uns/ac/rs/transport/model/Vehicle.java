package ftn.uns.ac.rs.transport.model;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "Vehicle")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(mappedBy = "vehicle", fetch = FetchType.LAZY)
    private Driver driver;

    @Column(name = "Model")
    private String model;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VehicleType")
    private VehicleType vehicleType;

    @Column(name = "LicencePlate")
    private String licencePlate;

    @Column(name = "NumberOfSeats")
    private Integer numberOfSeats;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CurrentLocation")
    private Location currentLocation;

    @Column(name = "TransportsBabies")
    private Boolean transportsBabies;

    @Column(name = "TransportsPets")
    private Boolean transportsPets;

    @OneToMany(mappedBy = "vehicle",fetch = FetchType.LAZY)
    Set<Review> reviews;
}
