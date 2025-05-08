package org.example.nosql.service;

import lombok.RequiredArgsConstructor;
import org.example.nosql.model.User;
import org.example.nosql.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.LinkedHashMap;

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

    public List<User> recommendByInterests(String id) {
        return userRepository.findById(id)
                .map(user -> userRepository.findByInterestsIn(user.getInterests()))
                .orElse(Collections.emptyList());
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> findByInterest(String interest) {
        if (interest == null || interest.trim().length() < 2) {
            throw new IllegalArgumentException("Інтерес має містити щонайменше 2 символи.");
        }

        return userRepository.findAll().stream()
                .filter(user -> user.getInterests().stream()
                        .anyMatch(i -> i.equalsIgnoreCase(interest)))
                .collect(Collectors.toList());
    }



    public Map<String, Long> getTopInterests(int topN) {
        List<User> users = userRepository.findAll();
        return users.stream()
                .flatMap(u -> u.getInterests().stream())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(topN)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));
    }
}
