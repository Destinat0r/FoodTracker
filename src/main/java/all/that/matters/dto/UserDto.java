package all.that.matters.dto;

import all.that.matters.model.Lifestyle;
import all.that.matters.model.Role;
import all.that.matters.model.Sex;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class UserDTO {
    private String username;
    private String fullName;
    private String nationalName;
    private String email;
    private Role role;
    private BigDecimal age;
    private Sex sex;
    private BigDecimal weight;
    private BigDecimal height;
    private Lifestyle lifestyle;
    private BigDecimal dailyNorm;
}
