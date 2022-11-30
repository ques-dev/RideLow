package rs.ac.uns.ftn.transport.model;

import jakarta.persistence.*;
import lombok.Data;

//TODO: Pitacu kako je zamisljeno da se lokacija pamti - da li postoji neki kao "repo" svih mogucih lokacija i
// da li ima smisla da ima id, i sta bi trebao da bude kljuc
@Entity
@Table(name = "locations")
@Data
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "address")
    private String address;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;
}
