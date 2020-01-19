package all.that.matters.dto;

import all.that.matters.model.Lifestyle;
import all.that.matters.model.Role;
import all.that.matters.model.Sex;
import lombok.*;
import org.springframework.lang.NonNull;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder


public class UserDTO {

    @NotBlank(message = "Please enter username")
    @Size(min = 2, max = 32, message = "username must be between 2 and 32 characters long")
    private String username;

    @NotBlank(message = "Please enter password")
    @Size(min = 4, max = 32, message = "Password must be between 4 and 32 characters long")
    private String password;

    @NotBlank(message = "Please enter full name")
    @Size(min = 2, max = 32, message = "Full name must be between 2 and 32 characters long")
    private String fullName;

    @NotBlank(message = "Please enter national name")
    @Size(min = 2, max = 32, message = "National name must be between 2 and 32 characters long")
    private String nationalName;

    @NotBlank(message = "Please enter email")
    @Email(message = "Email is not correct")
    private String email;

    private Role role;

    @NotEmpty(message = "Please enter age")
    @Min(value = 18, message = "Sorry, you should be at least 18 years old to use our service")
    @Max(value = 120, message = "Sorry, age upper than 200 is not supported.")
    private BigDecimal age;

    private Sex sex;

    @NotEmpty(message = "Please enter weight")
    @Min(value = 30, message = "Weight should be at least 30")
    @Max(value = 200, message = "Sorry, weight bigger than 200 is not supported.")
    private BigDecimal weight;

    @NotEmpty(message = "Please enter weight")
    @Min(value = 1, message = "Height should be at bigger than 0")
    @Max(value = 300, message = "Sorry, height upper than 300 is no supported")
    private BigDecimal height;
    private Lifestyle lifestyle;
    private BigDecimal dailyNorm;

}
