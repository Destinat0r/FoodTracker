package all.that.matters.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@NoArgsConstructor
@Data

@Entity
@Table(name = "users")
public class User {

    @Id
    private Long id;
    private String login;
    private String password;
    private Role role;
}
