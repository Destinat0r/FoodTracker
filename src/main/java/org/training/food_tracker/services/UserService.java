package org.training.food_tracker.services;

import org.training.food_tracker.dto.UserDTO;
import org.training.food_tracker.model.Biometrics;
import org.training.food_tracker.repo.UserExistsException;
import org.training.food_tracker.repo.UserNotFoundException;
import org.training.food_tracker.repo.UserRepo;
import org.training.food_tracker.model.Role;
import org.training.food_tracker.model.User;
import org.training.food_tracker.utils.ContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
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

    public void create(User user) throws UserExistsException {
        user.setRole(Role.USER);
        user.setActive(true);
        user.setPassword(passwordEncoder().encode(user.getPassword()));
        try {
            userRepo.save(user);
        } catch (Exception e) {
            throw new UserExistsException("User with username " + user.getUsername() + " already exists!");
        }
    }


    public List<User> findAll() {
        return userRepo.findAll();
    }

    public Optional<User> findById(Long id) {
        return userRepo.findById(id);
    }

    public UserDTO getUserDTOByUsername(String username) throws UserNotFoundException {
        return userToUserDTO(findByUsername(username).orElseThrow(UserNotFoundException::new));
    }

    public User userDTOtoUser(UserDTO userDTO) {
        return User.builder()
               .username(userDTO.getUsername())
               .fullName(userDTO.getFullName())
               .nationalName(userDTO.getNationalName())
               .email(userDTO.getEmail())
               .role(Role.USER)
               .password(userDTO.getPassword())
               .build();
    }

    public Optional<User> findByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    public UserDTO getCurrentUserDTO() {
        return userToUserDTO(ContextUtils.getPrincipal());
    }

    public UserDTO getUserDTOById(Long id) throws UserNotFoundException {
        Optional<User> optionalUser = findById(id);
        return userToUserDTO(optionalUser.orElseThrow(UserNotFoundException::new));
    }

    private UserDTO userToUserDTO(User user) {
        Biometrics biometrics = user.getBiometrics();
        return UserDTO.builder()
                       .username(user.getUsername())
                       .fullName(user.getFullName())
                       .nationalName(user.getNationalName())
                       .email(user.getEmail())
                       .age(biometrics.getAge())
                       .sex(biometrics.getSex())
                       .weight(biometrics.getWeight())
                       .height(biometrics.getHeight())
                       .lifestyle(biometrics.getLifestyle())
                       .dailyNorm(biometrics.getDailyNorm())
                       .build();
    }

    public void save(User user) {
        userRepo.save(user);
    }
}
