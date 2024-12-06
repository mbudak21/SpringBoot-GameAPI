package com.dreamgames.backendengineeringcasestudy.service;

import com.dreamgames.backendengineeringcasestudy.dto.GroupLeaderboardDTO;
import com.dreamgames.backendengineeringcasestudy.model.Tournament;
import com.dreamgames.backendengineeringcasestudy.model.TournamentBracket;
import com.dreamgames.backendengineeringcasestudy.model.TournamentParticipant;
import com.dreamgames.backendengineeringcasestudy.model.User;
import com.dreamgames.backendengineeringcasestudy.repository.TournamentBracketRepository;
import com.dreamgames.backendengineeringcasestudy.repository.TournamentParticipantRepository;
import com.dreamgames.backendengineeringcasestudy.repository.TournamentRepository;
import com.dreamgames.backendengineeringcasestudy.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class TournamentService {
    private final Integer MINIMUM_LEVEL_REQUIRED = 20;
    private final Integer MINIMUM_COINS_REQUIRED = 3000;
    private final Integer ENROLLMENT_COST = 1000;

    private final TournamentRepository tournamentRepository;
    private final UserRepository userRepository;
    private final TournamentBracketRepository tournamentBracketRepository;
    private final TournamentParticipantRepository tournamentParticipantRepository;
    private final Logger logger;

    public TournamentService(TournamentRepository tournamentRepository, TournamentBracketRepository tournamentBracketRepository, TournamentParticipantRepository tournamentParticipantRepository, UserRepository userRepository) {
        this.tournamentRepository = tournamentRepository;
        this.userRepository = userRepository;
        this.tournamentBracketRepository = tournamentBracketRepository;
        this.tournamentParticipantRepository = tournamentParticipantRepository;
        this.logger = LoggerFactory.getLogger(Logger.class);
    }


    public List<Tournament> getAllTournaments(Boolean isActive) { // NOTE: The filtering logic could be moved to the repository class for performance improvement
        List<Tournament> tournaments = tournamentRepository.findAll();

        // Filter based on the dynamically calculated `isActive` field
        if (isActive != null) {
            LocalDateTime now = LocalDateTime.now();
            return tournaments.stream()
                    .filter(tournament -> isActive.equals(
                            (now.isAfter(tournament.getStartTime()) || now.isEqual(tournament.getStartTime()))
                                    && now.isBefore(tournament.getEndTime())))
                    .toList();
        }
        return tournaments; // Return all tournaments if no filtering is needed
    }

    public Tournament getTournamentById(Long id) { //TODO: add similar exception handling to other service classes
        return tournamentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tournament not found"));
    }

    public Tournament createTournament(Tournament tournament) {
        return tournamentRepository.save(tournament);
    }

    public void deleteTournament(Long id) {
        tournamentRepository.deleteById(id);
    }

    @Transactional
    public TournamentParticipant joinTournament(Long tournamentId, Long userId) {
        logger.info("Received request, UserID: {} is trying to join TournamentID: {}", userId, tournamentId);
        Tournament tournament = getTournamentById(tournamentId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (user.getCoins() < MINIMUM_COINS_REQUIRED) {
            throw new ResponseStatusException(HttpStatus.PAYMENT_REQUIRED, "Insufficient coins. User has " + user.getCoins() + " coins.");
        }
        if (user.getLevel() < MINIMUM_LEVEL_REQUIRED) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User must be level " + MINIMUM_LEVEL_REQUIRED + " or above to join a tournament. User is currently level " + user.getLevel());
        }
        if (!tournament.getIsActive()){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "This tournament is already over");
        }

        List<TournamentBracket> brackets = tournamentBracketRepository.getEmptyBrackets(tournament);
        logger.info("Found {} empty brackets for tournamentID: {}", brackets.size(), tournamentId);

        for (TournamentBracket bracket : brackets) {
            if (tournamentBracketRepository.isUserInBracket(bracket.getId(), user.getId())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User already in this tournament");
            }
            List<TournamentParticipant> participantList = tournamentParticipantRepository.findByTournamentBracket(bracket);
            List<User> userList = participantList.stream().map(TournamentParticipant::getUser).toList();

            // Ensure matching sizes
            if (participantList.size() != userList.size()) {
                throw new IllegalStateException("Participant list size does not match user list size, this should never happen.");
            }

            Map<Integer, Integer> validTeamSlots = createMapping(bracket.getMaxTeams(), bracket.getParticipantsPerTeam()); // {1:5, 2:5}
            logger.info("Valid Team Slots: {}", validTeamSlots);

            for (int i = 0; i < participantList.size(); i++) {
                Integer participantTeam = participantList.get(i).getTeam();
                logger.info("team: {}", participantTeam);
                validTeamSlots.put(participantTeam, validTeamSlots.getOrDefault(participantTeam, 0) - 1);
                if (user.getCountry() == userList.get(participantTeam).getCountry()) {
                    validTeamSlots.put(participantTeam, -1); // Country constraint violated, set to -1
                    logger.info("User can't join team {}, due to UserID being from {}", participantTeam, user.getCountry());
                }
            }

            // Check any joinable team
            for (int i = 0; i < validTeamSlots.size(); i++) {
                if (validTeamSlots.get(i) > 0){
                    logger.info("Found a joinable team, enrolling the user.");
                    // Found empty and valid team, enroll the user
                    logger.info("User with ID {} found an empty bracket in tournament with ID {} on bracketID {}", userId, tournamentId, bracket.getId());
                    TournamentParticipant newParticipant = new TournamentParticipant();
                    newParticipant.setTournamentBracket(brackets.get(i));
                    newParticipant.setTeam(validTeamSlots.get(i));
                    newParticipant.setUser(userList.get(validTeamSlots.get(i)));
                    logger.info("User with ID {} joined tournament with ID {} on bracketID {}", userId, tournamentId, bracket.getId());

                    return tournamentParticipantRepository.save(newParticipant);
                }
            }
        }

        // Found no valid teams AND/OR no empty brackets, create a new bracket
        TournamentBracket newBracket = new TournamentBracket();
        newBracket.setTournament(tournament);
        tournamentBracketRepository.save(newBracket);

        // Create the new participant
        TournamentParticipant newParticipant = new TournamentParticipant();
        newParticipant.setTournamentBracket(newBracket);
        newParticipant.setUser(user);

        logger.info("User with ID {} joined tournament with ID {} on bracketID {}", userId, tournamentId, newBracket.getId());
        return tournamentParticipantRepository.save(newParticipant);
    }

    public List<GroupLeaderboardDTO> getTournamentBracketDTO(Long tournamentId, Integer bracket_index) {
        List<TournamentBracket> brackets = tournamentBracketRepository.findByTournamentId(tournamentId);
        System.out.println(brackets);

        if (brackets.isEmpty()) {
            throw new IllegalArgumentException("No brackets found for tournament ID: " + tournamentId);
        }
        // Check if the bracket_index is valid
        if (bracket_index == null || bracket_index <= 0 || bracket_index > brackets.size()) {
            throw new IndexOutOfBoundsException("Invalid bracket index: " + bracket_index + ". Must be between 1 and " + brackets.size());
        }

        try {
            Long bracketId = brackets.get(bracket_index - 1).getId(); // convert to 0 based indexing
            return tournamentParticipantRepository.getGroupLeaderboard(bracketId);

        } catch (Exception e) {
            //logger.error("Failed to retrieve leaderboard for tournament ID: " + tournamentId + ", bracket index: " + bracket_index, e);
            throw new RuntimeException("An error occurred while retrieving the leaderboard", e);
        }
    }

    public List<TournamentParticipant> getActiveParticipations(Long userID){
        // Returns all active participations of the user
        return tournamentParticipantRepository.getActiveParticipations(userRepository.getReferenceById(userID));
    }

    // Creates a mapping to m of size n.
    private static Map<Integer, Integer> createMapping(int n, int m) {
        Map<Integer, Integer> result = new HashMap<>();
        for (int i = 1; i <= n; i++) {
            result.put(i, m);
        }
        return result;
    }
}