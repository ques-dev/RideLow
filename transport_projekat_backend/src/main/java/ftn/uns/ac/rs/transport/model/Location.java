package ftn.uns.ac.rs.transport.model;

import jakarta.persistence.*;

//TODO: Pitacu kako je zamisljeno da se lokacija pamti - da li postoji neki kao "repo" svih mogucih lokacija i
// da li ima smisla da ima id, i sta bi trebao da bude kljuc
@Entity
@Table(name = "Location")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "Address")
    private String address;

    @Column(name = "Latitude")
    private Double latitude;

    @Column(name = "Longitude")
    private Double longitude;

}
