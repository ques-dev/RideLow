package ftn.uns.ac.rs.transport.model;


import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "Passenger")
public class Passenger extends User{

    @ManyToMany()
    @JoinTable(name = "Passenger_Ride", joinColumns = @JoinColumn(name = "PassengerId", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "RideId", referencedColumnName = "id"))
    private Set<Ride> rides;

    @ManyToMany()
    @JoinTable(name = "Passenger_FavoriteRoute", joinColumns = @JoinColumn(name = "PassengerId", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "RouteId", referencedColumnName = "id"))
    private Set<Route> favorites;
}
