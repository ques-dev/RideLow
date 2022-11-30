package ftn.uns.ac.rs.transport.model;

import ftn.uns.ac.rs.transport.model.enumerations.DocumentType;
import jakarta.persistence.*;

@Entity
@Table(name = "Document")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "Name")
    private DocumentType name;

    @Column(name = "Picture")
    private String picture;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn (name = "DriverId")
    private Driver driver;
}
