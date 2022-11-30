package ftn.uns.ac.rs.transport.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Route")
public class Route {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "StartLocationId")
    private Location startLocation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EndLocationId")
    private Location endLocation;

    @Column(name = "DistanceInKm")
    private  Double distanceInKm;
}
