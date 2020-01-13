package all.that.matters.services;

import all.that.matters.repo.UserRepo;
import all.that.matters.model.Role;
import all.that.matters.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService implements UserDetailsService {

    private UserRepo userRepo;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(8);
    }

    @Autowired
    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepo.findByUsername(username);
        user.orElseThrow(() -> new UsernameNotFoundException("user not found"));
        return user.get();
    }

    public void addUser(User user) {
        user.setRoles(Collections.singleton(Role.USER));
        user.setActive(true);
        user.setPassword(passwordEncoder().encode(user.getPassword()));
        userRepo.save(user);
    }

    public List<User> findAll() {
        return userRepo.findAll();
    }

    public Optional<User> findById(Long id) {
        return userRepo.findById(id);
    }

    public void save(User user) {
        userRepo.save(user);
    }
}
