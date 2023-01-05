package rs.ac.uns.ftn.transport.dto.panic;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class PanicReasonDTO {

    @Length(max = 255,message = "{maxLength}")
    @NotBlank(message = "{required}")
    private String reason;
}
