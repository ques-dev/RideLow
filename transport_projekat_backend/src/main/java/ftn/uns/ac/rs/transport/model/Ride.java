package ftn.uns.ac.rs.transport.model;


import jakarta.persistence.*;

@Entity
@Table(name = "Ride")
public class Ride {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
}
