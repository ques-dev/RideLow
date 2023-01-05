package rs.ac.uns.ftn.transport.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "favorites")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteRide {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="FavoriteName")
    private String favoriteName;

    @ManyToMany()
    @JoinTable(name = "Favorite_route", joinColumns = @JoinColumn(name = "FavoriteId", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "RouteId", referencedColumnName = "id"))
    @ToString.Exclude
    private Set<Location> locations;

    @ManyToMany()
    @ToString.Exclude
    private Set<Passenger> passengers;

    @Column(name = "TransportsBaby")
    private Boolean babyTransport;

    @Column(name = "TransportsPet")
    private Boolean petTransport;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VehicleType")
    @ToString.Exclude
    private VehicleType vehicleType;

}
