package org.example.nosql;

import org.example.nosql.repository.UserRepository;
import org.example.nosql.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class NosqlDbmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(NosqlDbmsApplication.class, args);
    }

    @Bean
    CommandLineRunner init(UserRepository repo, UserService userService) {
        return args -> {
            System.out.println("Програма запущена.");
        };
    }
}
