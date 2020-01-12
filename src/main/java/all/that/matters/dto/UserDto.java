package all.that.matters.dto;

import all.that.matters.domain.Lifestyle;
import all.that.matters.domain.Role;
import all.that.matters.domain.Sex;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class UserDto {
    private String username;
    private String fullName;
    private String nationalName;
    private String email;
    private Role role;
    private Double age;
    private Sex sex;
    private Double weight;
    private Double height;
    private Lifestyle lifestyle;
    private Double dailyNorm;
}
