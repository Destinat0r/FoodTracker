package org.training.food.tracker.services.defaults;

import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.training.food.tracker.model.Biometrics;
import org.training.food.tracker.model.Role;
import org.training.food.tracker.model.Sex;
import org.training.food.tracker.model.User;
import org.training.food.tracker.repo.UserRepo;
import org.training.food.tracker.repo.exceptions.UserExistsException;
import org.training.food.tracker.repo.exceptions.UserNotFoundException;
import org.training.food.tracker.services.UserService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Slf4j
@Log4j2

@Service
public class UserServiceDefault implements UserService, UserDetailsService {

    private UserRepo userRepo;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(8);
    }

    @Autowired
    public UserServiceDefault(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepo.findByUsername(username);
        user.orElseThrow(() -> new UsernameNotFoundException("user not found"));
        return user.get();
    }

    public User create(User user) throws UserExistsException {
        log.debug("Setting role of the user");
        user.setRole(Role.USER);

        log.debug("Encoding the password and setting it to user");
        user.setPassword(passwordEncoder().encode(user.getPassword()));
        try {
            log.debug("Saving user");
            user = userRepo.save(user);
        } catch (Exception e) {
            log.error("Creation of user " + user + " has failed", e);
            throw new UserExistsException("User with username " + user.getUsername() + " already exists!");
        }
        return user;
    }

    public User findById(Long id) throws UserNotFoundException {
        return userRepo.findById(id).orElseThrow(UserNotFoundException::new);
    }

    @Transactional
    @Override public User findByUsername(String username) throws UserNotFoundException {
        return userRepo.findByUsername(username).orElseThrow(UserNotFoundException::new);
    }

    public List<User> findAll() {
        return userRepo.findAll();
    }

    @Override public int update(User user) {
        return userRepo.update(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole().toString(),
                user.getDailyNormCalories()
        );
    }

    /**
     * Total energy expenditure calculation using Harris–Benedict equation
     * @return daily norm of calories
     */
    public BigDecimal calculateDailyNormCalories(Biometrics biometrics) {
        if (biometrics.getSex() == Sex.FEMALE) {
            return (new BigDecimal(655.1)
                            .add(new BigDecimal(9.563).multiply(biometrics.getWeight()))
                            .add(new BigDecimal(1.850).multiply(biometrics.getHeight()))
                            .subtract(new BigDecimal(4.676).multiply(biometrics.getAge())))
                           .multiply(biometrics.getLifestyle().getCoefficient())
                           .setScale(2, BigDecimal.ROUND_HALF_DOWN);
        }
        return (new BigDecimal(66.5)
                        .add(new BigDecimal(13.75).multiply(biometrics.getWeight()))
                        .add(new BigDecimal(5.003).multiply(biometrics.getHeight()))
                        .subtract(new BigDecimal(6.755).multiply(biometrics.getAge())))
                       .multiply(biometrics.getLifestyle().getCoefficient())
                       .setScale(2, BigDecimal.ROUND_HALF_DOWN);
    }
}
