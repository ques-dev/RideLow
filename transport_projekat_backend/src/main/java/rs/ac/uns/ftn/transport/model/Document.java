package rs.ac.uns.ftn.transport.model;

import lombok.Data;
import rs.ac.uns.ftn.transport.model.enumerations.DocumentType;
import jakarta.persistence.*;

@Entity
@Table(name = "documents")
@Data
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "name")
    private DocumentType name;

    @Column(name = "picture")
    private String picture;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn (name = "driverId")
    private Driver driver;
}
