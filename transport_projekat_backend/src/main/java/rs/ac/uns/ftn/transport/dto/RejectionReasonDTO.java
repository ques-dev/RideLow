package rs.ac.uns.ftn.transport.dto;

import javax.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class RejectionReasonDTO {
    @Length(max = 255,message = "{maxLength}")
    @NotBlank(message = "{required}")
    private String reason;
}
