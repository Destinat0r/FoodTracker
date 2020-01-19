package all.that.matters.model;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@ToString

@Entity
@Table(name = "users", uniqueConstraints={@UniqueConstraint(columnNames={"username"})})
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @NotNull(message = "username cant be null")
    @Column(name = "username", nullable = false)
    private String username;

    @NotNull(message = "username cant be null")
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

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "owner")
    @JoinColumn(name = "biometrics_id")
    private Biometrics biometrics;

    @Column(name = "active", nullable = false)
    private boolean active;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    private Set<Role> roles;

    @Override public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Override public String getPassword() {
        return null;
    }

    @Override public String getUsername() {
        return null;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getNationalName() {
        return nationalName;
    }

    public void setNationalName(String nationalName) {
        this.nationalName = nationalName;
    }

    public Biometrics getBiometrics() {
        return biometrics;
    }

    public void setBiometrics(Biometrics biometrics) {
        this.biometrics = biometrics;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
