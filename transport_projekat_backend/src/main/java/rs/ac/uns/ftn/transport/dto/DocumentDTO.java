package rs.ac.uns.ftn.transport.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentDTO {
    private Integer id;
    private String name;
    private String documentImage;
    private int driverId;
}
