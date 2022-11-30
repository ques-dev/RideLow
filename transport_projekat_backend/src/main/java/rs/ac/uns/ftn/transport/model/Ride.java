package rs.ac.uns.ftn.transport.model;


import jakarta.persistence.*;
import lombok.Data;

//TODO: uraditi
@Entity
@Table(name = "rides")
@Data
public class Ride {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
}
