package org.example.nosql.repository;

import org.example.nosql.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    List<User> findByNameContainingIgnoreCase(String name);
    Optional<User> findByEmail(String email);
    List<User> findByInterestsIn(List<String> interests);
}
