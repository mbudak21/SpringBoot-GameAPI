package com.dreamgames.backendengineeringcasestudy.service;

import com.dreamgames.backendengineeringcasestudy.dto.GroupLeaderboardDTO;
import com.dreamgames.backendengineeringcasestudy.model.Tournament;
import com.dreamgames.backendengineeringcasestudy.model.TournamentBracket;
import com.dreamgames.backendengineeringcasestudy.model.TournamentParticipant;
import com.dreamgames.backendengineeringcasestudy.model.User;
import com.dreamgames.backendengineeringcasestudy.repository.TournamentBracketRepository;
import com.dreamgames.backendengineeringcasestudy.repository.TournamentParticipantRepository;
import com.dreamgames.backendengineeringcasestudy.repository.TournamentRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TournamentService {
    public static final Integer MINIMUM_LEVEL_REQUIRED = 20;
    public static final Integer MINIMUM_COINS_REQUIRED = 3000;
    public static final Integer ENROLLMENT_COST = 1000;
    public static final Integer FIRST_PLACE_COIN_AWARD = 10000;
    public static final Integer SECOND_PLACE_COIN_AWARD = 5000;

    private final TournamentRepository tournamentRepository;
    private final TournamentBracketRepository tournamentBracketRepository;
    private final TournamentParticipantRepository tournamentParticipantRepository;
    private final Logger logger;
    private final UserService userService;

    public TournamentService(TournamentRepository tournamentRepository, TournamentBracketRepository tournamentBracketRepository, TournamentParticipantRepository tournamentParticipantRepository, UserService userService) {
        this.tournamentRepository = tournamentRepository;
        this.tournamentBracketRepository = tournamentBracketRepository;
        this.tournamentParticipantRepository = tournamentParticipantRepository;
        this.logger = LoggerFactory.getLogger(Logger.class);
        this.userService = userService;
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
        Tournament tournament = this.getTournamentById(tournamentId);
        User user = userService.getUserById(userId);

        if (user.getCoins() < MINIMUM_COINS_REQUIRED) {
            throw new ResponseStatusException(HttpStatus.PAYMENT_REQUIRED, "Insufficient coins. User has " + user.getCoins() + " coins.");
        }
        else if (user.getLevel() < MINIMUM_LEVEL_REQUIRED) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User must be level " + MINIMUM_LEVEL_REQUIRED + " or above to join a tournament. User is currently level " + user.getLevel());
        }
        else if (!tournament.getIsActive()){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "This tournament is not active");
        }
        else if (tournamentParticipantRepository.UserHasUnclaimedTournaments(userService.getUserById(userId))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User has an unclaimed tournament reward. Claim the reward to join other tournaments.");
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

            Map<Integer, Integer> validTeamSlots = createMapping(bracket.getMaxTeams(), bracket.getParticipantsPerTeam()); // {0:5, 1:5}
            logger.info("Valid Team Slots: {}", validTeamSlots);



            for (int i = 0; i < participantList.size(); i++) {
                TournamentParticipant tournamentParticipant = participantList.get(i);
                User participantsUser = userList.get(i);
                Integer participantsTeam = tournamentParticipant.getTeam();
                logger.info("team: {}", participantsTeam);
                validTeamSlots.put(participantsTeam, validTeamSlots.get(participantsTeam) - 1);
                if (user.getCountry() == participantsUser.getCountry()) {
                    validTeamSlots.put(participantsTeam, -1); // Country constraint violated, set to -1
                    logger.info("User can't join team {}, due to UserID being from {}", participantsTeam, user.getCountry());
                }
            }
            logger.info("Valid Team Slots after calculation: {}", validTeamSlots);

            // Check any joinable team
            for (int i = 0; i < validTeamSlots.size(); i++) {
                if (validTeamSlots.get(i) > 0){
                    logger.info("Found a joinable team, team {}, enrolling the user.", i);
                    // Found empty and valid team, enroll the user
                    logger.info("User with ID {} found an empty bracket in tournament with ID {} on bracketID {}", userId, tournamentId, bracket.getId());
                    TournamentParticipant newParticipant = new TournamentParticipant();
                    newParticipant.setTournamentBracket(bracket);
                    newParticipant.setTeam(i);
                    newParticipant.setUser(user);
                    user.setCoins(user.getCoins() - ENROLLMENT_COST);
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

    public List<GroupLeaderboardDTO> getTournamentBracketDTO(Long bracket_id) {
        // Add error handling?
        return tournamentParticipantRepository.getGroupLeaderboard(bracket_id);
    }

    public List<GroupLeaderboardDTO> getTournamentBracketDTO(Long tournamentId, Integer bracket_index) {
        List<TournamentBracket> brackets = tournamentBracketRepository.findByTournamentId(tournamentId);

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
        return tournamentParticipantRepository.getActiveParticipations(userService.getUserById(userID));
    }

    @Transactional
    public void claimReward(Long userId){
        User user = userService.getUserById(userId);
        // Get participation from an unclaimed reward
        TournamentParticipant tp = tournamentParticipantRepository.getUnclaimedParticipation(user);
        if (tp.getTournamentBracket().getTournament().getIsActive()){
            // Tournament is still active, can't get the reward
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Can't claim reward since the tournament hasn't ended. Tournament ID: "  + tp.getTournamentBracket().getTournament().getId());
        }
        else{
            // Tournament has ended, so the user can claim
            Integer rank = getUserRankInTeam(userId);
            logger.info("User ID: {} is at rank {}", userId, rank);
            if (rank == 1 && tp.getScore() != 0){
                user.setCoins(user.getCoins() + FIRST_PLACE_COIN_AWARD);
            }
            else if (rank == 2 && tp.getScore() != 0){
                user.setCoins(user.getCoins() + SECOND_PLACE_COIN_AWARD);
            }
            tp.setRewardClaimed(true);
            tournamentParticipantRepository.save(tp);
        }
    }

    public Integer getUserRankInTeam(Long userId) {
        // Assumes dto is sorted by team first, then by tournamentScore descending
        TournamentParticipant tp = tournamentParticipantRepository.getUnclaimedParticipation(userService.getUserById(userId));
        List<GroupLeaderboardDTO> dto = getTournamentBracketDTO(tp.getTournamentBracket().getId());

        int currentTeam = -1; // Placeholder for current team
        int currentRank = 0; // Rank within the team
        int sameRankCount = 0; // Tracks the number of users sharing the same rank
        Integer previousScore = null; // Previous user's score in the same team

        for (int i = 0; i < dto.size(); i++) {
            GroupLeaderboardDTO elem = dto.get(i);

            // Check if we're in a new team
            // Update previous score
            if (!elem.getTeam().equals(currentTeam)) {
                currentTeam = elem.getTeam();
                currentRank = 1; // Reset rank for the new team
                sameRankCount = 0; // Reset tie counter
            } else {
                // Check if the current score is the same as the previous score
                if (elem.getTournamentScore().equals(previousScore)) {
                    sameRankCount++; // Increment tie counter
                } else {
                    // Update rank by adding tie counter and reset the counter for a new score
                    currentRank += sameRankCount + 1;
                    sameRankCount = 0; // Reset tie counter
                }
            }
            previousScore = elem.getTournamentScore(); // Set first user's score as reference

            // Check if the current user matches the given userId
            if (elem.getUserId().equals(userId)) {
                return currentRank; // Return the rank of the user within their team
            }
        }

        // Return -1 if the user is not found in the leaderboard
        return -1;
    }


    // Creates a mapping to m of size n.
    private static Map<Integer, Integer> createMapping(int n, int m) {
        Map<Integer, Integer> result = new HashMap<>();
        for (int i = 0; i < n; i++) {
            result.put(i, m);
        }
        return result;
    }
}