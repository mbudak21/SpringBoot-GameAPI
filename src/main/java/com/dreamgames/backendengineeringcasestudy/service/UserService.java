package com.dreamgames.backendengineeringcasestudy.service;

import com.dreamgames.backendengineeringcasestudy.model.Country;
import com.dreamgames.backendengineeringcasestudy.model.User;
import com.dreamgames.backendengineeringcasestudy.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Service
public class UserService {

    private static final int DEFAULT_COINS = 5000;
    private static final int DEFAULT_LEVEL = 1;
    private static final int LEVEL_UP_BONUS = 25;

    private final UserRepository userRepository;
    private final CountryService countryService;

    public UserService(UserRepository userRepository, CountryService countryService) {
        this.userRepository = userRepository;
        this.countryService = countryService;
    }

    @Transactional
    public User createUser() {
        Country randomCountry = countryService.getRandomCountry();
        if (randomCountry == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Country generation failed");
        }

        User user = new User();
        user.setCoins(DEFAULT_COINS);
        user.setLevel(DEFAULT_LEVEL);
        user.setCountry(randomCountry);

        return userRepository.save(user);
    }

    @Transactional
    public User updateLevel(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with ID " + userId + " not found"));

        user.setLevel(user.getLevel() + 1);
        user.setCoins(user.getCoins() + LEVEL_UP_BONUS);

        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long userId) {
        return userRepository.getReferenceById(userId);
    }
}