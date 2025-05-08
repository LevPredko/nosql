package org.example.nosql.service;

import lombok.RequiredArgsConstructor;
import org.example.nosql.model.User;
import org.example.nosql.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

    // Проста логіка рекомендацій
    public List<User> recommendByInterests(String id) {
        return userRepository.findById(id)
                .map(user -> userRepository.findByInterestsIn(user.getInterests()))
                .orElse(Collections.emptyList());
    }

    // Метод для пошуку користувача за email
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);  // Використовуємо метод з репозиторію
    }
}
