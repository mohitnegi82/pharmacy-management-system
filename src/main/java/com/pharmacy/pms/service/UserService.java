package com.pharmacy.pms.service;

import com.pharmacy.pms.model.User;
import com.pharmacy.pms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    /**
     * Creates a new login account. Only reachable from the Admin-only /users page, so only
     * an admin can grant someone the ability to log in — the password is BCrypt-hashed
     * before it's stored, never kept in plain text.
     */
    public User createUser(String username, String rawPassword, String fullName, String email, com.pharmacy.pms.model.Role role) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Username already exists: " + username);
        }
        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(rawPassword))
                .fullName(fullName)
                .email(email)
                .role(role)
                .active(true)
                .build();
        return userRepository.save(user);
    }

    public void deactivate(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
        user.setActive(false);
        userRepository.save(user);
    }

    public void activate(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
        user.setActive(true);
        userRepository.save(user);
    }
}
