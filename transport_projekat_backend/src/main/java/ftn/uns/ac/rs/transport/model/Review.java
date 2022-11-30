package ftn.uns.ac.rs.transport.model;

import jakarta.persistence.*;

//TODO: Model nije jasan sto se tice recenzija (vozac i vozilo odvojeno). Pitacu asistenta za ovo. Do tada ostaje prazno
@Entity
@Table(name = "Review")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
}
