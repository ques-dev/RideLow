package rs.ac.uns.ftn.transport.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

@Entity
@Table(name = "driver_edit_requests")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DriverEditRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", unique=true, nullable=false)
    private Integer id;

    @Column (name = "driverId", nullable = false)
    @NotNull(message = "{required}")
    private Integer driverId;

    @Column (name = "name")
    @Length(max = 100, message = "{maxLength}")
    private String name;

    @Column (name = "surname")
    @Length(max = 100, message = "{maxLength}")
    private String surname;

    @Column (name = "profilePicture")
    @Lob
    private String profilePicture;

    @Column (name = "telephoneNumber")
    @Length(max = 18, message = "{maxLength}")
    private String telephoneNumber;

    @Column (name = "email")
    @Email(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "{format}")
    @Length(max = 100, message = "{maxLength}")
    private String email;

    @Column (name = "address")
    @Length(max = 100, message = "{maxLength}")
    private String address;

    @Column (name = "reviewed")
    private Boolean reviewed;

    @Column (name = "approved")
    private Boolean approved;

    @Column (name = "dateOfCreation")
    private LocalDateTime dateOfCreation;
}
