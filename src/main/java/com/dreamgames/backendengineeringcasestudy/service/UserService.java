package com.dreamgames.backendengineeringcasestudy.service;

import com.dreamgames.backendengineeringcasestudy.model.Country;
import com.dreamgames.backendengineeringcasestudy.model.TournamentBracket;
import com.dreamgames.backendengineeringcasestudy.model.TournamentParticipant;
import com.dreamgames.backendengineeringcasestudy.model.User;
import com.dreamgames.backendengineeringcasestudy.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;


@Service
public class UserService {

    private static final int DEFAULT_COINS = 5000;
    private static final int DEFAULT_LEVEL = 1;
    private static final int LEVEL_UP_BONUS = 25;

    private final UserRepository userRepository;
    private final CountryService countryService;
    private final TournamentService tournamentService;
    private final TournamentBracketService tournamentBracketService;
    private final Logger logger;


    public UserService(UserRepository userRepository, CountryService countryService, TournamentBracketService tournamentBracketService, TournamentService tournamentService) {
        this.userRepository = userRepository;
        this.countryService = countryService;
        this.tournamentService = tournamentService;
        this.tournamentBracketService = tournamentBracketService;
        this.logger = getLogger();
    }

    private static Logger getLogger() {
        return LoggerFactory.getLogger(Logger.class);
    }

    @Transactional
    public User createUser(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists");
        }
        Country randomCountry = countryService.getRandomCountry();
        if (randomCountry == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Country generation failed");
        }

        logger.info("Trying to create a new user with the following properties: {username: " + username +
                " coins: " + DEFAULT_COINS +
                " level: " + DEFAULT_LEVEL +
                " country:" + randomCountry.getName() +
                "}");
        User user = new User();
        user.setUsername(username);
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

        // Get all the tournament brackets the user is a participant of
        List<TournamentParticipant> activeParticipations = tournamentService.getActiveParticipations(user.getId());
        for (TournamentParticipant participation : activeParticipations) {
            participation.setScore(participation.getScore() + 1);
        }
        return userRepository.save(user);
    }

    @Transactional
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public User getUserById(Long userId) {
        return userRepository.getReferenceById(userId);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}