package rs.ac.uns.ftn.transport.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@Entity
@Table(name = "passengers")
@Data
@EqualsAndHashCode(callSuper = true)
public class Passenger extends User{

    @ManyToMany()
    @JoinTable(name = "passenger_Ride", joinColumns = @JoinColumn(name = "PassengerId", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "RideId", referencedColumnName = "id"))
    private Set<Ride> rides;

    @ManyToMany()
    @JoinTable(name = "passenger_FavoriteRoute", joinColumns = @JoinColumn(name = "PassengerId", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "RouteId", referencedColumnName = "id"))
    private Set<Route> favorites;
}
