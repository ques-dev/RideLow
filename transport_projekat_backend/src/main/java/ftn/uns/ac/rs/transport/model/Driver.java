package ftn.uns.ac.rs.transport.model;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "Driver")
public class Driver extends User{

    @OneToMany(mappedBy = "driver", fetch = FetchType.LAZY)
    private Set<Document> documents;

    @OneToMany(fetch = FetchType.LAZY)
    private Set<Ride> rides;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VehicleId")
    private Vehicle vehicle;
}
