package rs.ac.uns.ftn.transport.model;

import jakarta.persistence.*;
import lombok.Data;

//TODO: Model nije jasan sto se tice recenzija (vozac i vozilo odvojeno). Pitacu asistenta za ovo. Do tada ostaje prazno
@Entity
@Table(name = "reviews")
@Data
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
}
