package rs.ac.uns.ftn.transport.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.ac.uns.ftn.transport.model.Document;
import rs.ac.uns.ftn.transport.model.enumerations.DocumentType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentDTO {
    private Integer id;
    private String name;
    private String documentImage;
    private int driverId;

    public DocumentDTO(Document document) {
        this.id = document.getId();
        this.name = DocumentType.getString(document.getName());
        this.documentImage = document.getDocumentImage();
        this.driverId = document.getDriver().getId();
    }
}