package rs.ac.uns.ftn.transport.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Set;

@Entity
@Table(name = "drivers")
@Data
@EqualsAndHashCode(callSuper = true)
public class Driver extends User{

    @OneToMany(mappedBy = "driver", fetch = FetchType.LAZY)
    private Set<Document> documents;

    @OneToMany(fetch = FetchType.LAZY)
    private Set<Ride> rides;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicleId")
    private Vehicle vehicle;
}
