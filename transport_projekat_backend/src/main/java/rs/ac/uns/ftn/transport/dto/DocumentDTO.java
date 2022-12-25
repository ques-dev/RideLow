package rs.ac.uns.ftn.transport.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentDTO {
    private Integer id;

    @Length(max = 100)
    @NotBlank
    private String name;

    @NotBlank
    private String documentImage;

    private int driverId;
}
