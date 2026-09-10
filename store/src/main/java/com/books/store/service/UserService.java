package com.books.store.service;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.books.store.entity.User;
import com.books.store.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
    public class UserService {

        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;

        public UserService(UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
            this.userRepository = userRepository;
            this.passwordEncoder = passwordEncoder;
        }

        public User register(User user) {

            if (userRepository.findByEmail(user.getEmail()).isPresent()) {
                throw new IllegalArgumentException("Email already registered");
            }

            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRole("USER");

            return userRepository.save(user);
        }
    }

