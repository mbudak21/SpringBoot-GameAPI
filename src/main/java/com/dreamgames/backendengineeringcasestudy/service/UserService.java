package com.dreamgames.backendengineeringcasestudy.service;

import com.dreamgames.backendengineeringcasestudy.model.User;
import com.dreamgames.backendengineeringcasestudy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private static final List<String> COUNTRIES = Arrays.asList(
            "Turkey", "United States", "United Kingdom", "France", "Germany"
    );

    public User createUser() {
        User user = new User();
        user.setCoins(5000);
        user.setLevel(1);
        user.setCountry(COUNTRIES.get(new Random().nextInt(COUNTRIES.size())));
        return userRepository.save(user);
    }

    public User updateLevel(Long userId) {
        // Fetch the user from the database
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        // Update the user's level and coins
        user.setLevel(user.getLevel() + 1);
        user.setCoins(user.getCoins() + 25);

        // Save and return the updated user
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}