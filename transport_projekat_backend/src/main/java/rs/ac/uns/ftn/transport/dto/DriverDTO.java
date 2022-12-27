package rs.ac.uns.ftn.transport.dto;

import jakarta.persistence.Basic;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverDTO {
    private Integer id;

    @NotBlank
    @Length(max = 100)
    private String name;

    @NotBlank
    @Length(max = 100)
    private String surname;

    private String profilePicture;

    @Length(max = 18)
    private String telephoneNumber;

    @NotBlank
    @Email(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")
    @Length(max = 100)
    private String email;

    @NotBlank
    @Length(max = 100)
    private String address;
}
