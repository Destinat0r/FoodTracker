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
@ToString

public class UserDTO {

    @NotBlank(message = "Username cannot be empty")
    private String username;

    @NotBlank(message = "Password cannot be empty")
    private String password;

    @NotBlank(message = "Name cannot be empty")
    private String fullName;

    @NotBlank(message = "Name cannot be empty")
    private String nationalName;

    @NotBlank(message = "Email cannot be empty")
//    @Email(message = "Email is not correct")
    private String email;


    private Role role;

    @NotNull(message = "Age cannot be null")
    @Min(value = 1, message = "Incorrect age")
    private BigDecimal age;

    private Sex sex;

    @NotNull(message = "Weight cannot be null")
    @Min(value = 1, message = "Incorrect weight")
    private BigDecimal weight;

    @NotNull(message = "Height cannot be null")
    @Min(value = 1, message = "Incorrect height")
    private BigDecimal height;
    private Lifestyle lifestyle;
    private BigDecimal dailyNorm;

}
