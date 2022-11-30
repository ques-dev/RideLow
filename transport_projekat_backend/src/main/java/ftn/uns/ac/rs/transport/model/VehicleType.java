package ftn.uns.ac.rs.transport.model;

import jakarta.persistence.*;

@Entity
@Table(name = "VehicleType")
public class VehicleType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "Name")
    private String name;

    @Column(name = "PricePerKm")
    private Double pricePerKm;
}
