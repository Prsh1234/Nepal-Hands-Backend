package com.example.nepalhandsbackend.config;

import com.example.nepalhandsbackend.model.User;
import com.example.nepalhandsbackend.repository.UserRepository;
import com.example.nepalhandsbackend.states.Role;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Set;

@Configuration
public class AdminInitializer {

    @Bean
    CommandLineRunner createAdmin(UserRepository userRepository) {
        return args -> {
            if (userRepository.findByEmail("admin@nepalHands.com").isEmpty()) {

                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

                User admin = new User();
                admin.setFirstName("System");
                admin.setLastName("Admin");
                admin.setEmail("admin@nepalHands.com");
                admin.setPassword(encoder.encode("Admin@123"));
                admin.setRoles(Set.of(Role.ROLE_ADMIN));  // ✅ correct

                userRepository.save(admin);

                System.out.println("✅ Default Admin created");
            }
        };
    }
}