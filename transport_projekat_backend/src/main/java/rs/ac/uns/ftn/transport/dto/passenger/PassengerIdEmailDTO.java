package rs.ac.uns.ftn.transport.dto.passenger;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PassengerIdEmailDTO {

    @Min(value=1,message="{id.notValid}")
    private Integer id;

    @Email(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "{format}")
    @Length(max = 255, message = "{maxLength}")
    @NotBlank(message = "{required}")
    private String email;
}
