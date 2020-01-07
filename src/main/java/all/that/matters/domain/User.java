package all.that.matters.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Collection;
import java.util.List;
import java.util.Set;

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Data

@Entity
@Table(name = "users", uniqueConstraints={@UniqueConstraint(columnNames={"username"})})
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "Username cannot be empty")
    @Column(name = "username", nullable = false)
    private String username;

    @NotBlank(message = "Password cannot be empty")
    @Column(name = "password", nullable = false)
    private String password;

    @Email(message = "Email is not correct")
    @NotBlank(message = "Email cannot be empty")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Name cannot be empty")
    @Column(name = "fullName", nullable = false)
    private String fullName;

    @NotBlank(message = "National name cannot be empty")
    @Column(name = "national_name", nullable = false)
    private String nationalName;

    @OneToOne
    @JoinColumn(name = "biometrics_id")
    private Biometrics biometrics;

    @Column(name = "active", nullable = false)
    private boolean active;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    private Set<Role> roles;

//    @ElementCollection(targetClass = Food.class, fetch = FetchType.EAGER)
//    @CollectionTable(name = "food_owners", joinColumns = @JoinColumn(name = "owners_id"))
//    private List<Food> foods;

    @Override public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Override public boolean isAccountNonExpired() {
        return true;
    }

    @Override public boolean isAccountNonLocked() {
        return true;
    }

    @Override public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override public boolean isEnabled() {
        return isActive();
    }
}
