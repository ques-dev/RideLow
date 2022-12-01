package rs.ac.uns.ftn.transport.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.ac.uns.ftn.transport.model.enumerations.DocumentType;
import jakarta.persistence.*;

@Entity
@Table(name = "documents")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "name")
    private DocumentType name;

    @Column(name = "documentImage")
    private String documentImage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn (name = "driverId")
    private Driver driver;

    public Document(DocumentType name, String documentImage, Driver driver) {
        this.name = name;
        this.documentImage = documentImage;
        this.driver = driver;
    }
}
