package rs.ac.uns.ftn.transport.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "vehicleTypes")
@Data
public class VehicleType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "pricePerKm")
    private Double pricePerKm;
}
