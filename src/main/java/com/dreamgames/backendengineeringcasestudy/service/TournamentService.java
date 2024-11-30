package com.dreamgames.backendengineeringcasestudy.service;

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

import java.util.List;

@Service
public class TournamentService {
    private final TournamentRepository tournamentRepository;
//    private final UserRepository userRepository;
//    private final TournamentBracketRepository tournamentBracketRepository;
//    private final TournamentParticipantRepository tournamentParticipantRepository;
//    private final Logger logger;
    

    public TournamentService(TournamentRepository tournamentRepository, TournamentBracketRepository tournamentBracketRepository, TournamentParticipantRepository tournamentParticipantRepository, UserRepository userRepository) {
        this.tournamentRepository = tournamentRepository;
//        this.userRepository = userRepository;
//        this.tournamentBracketRepository = tournamentBracketRepository;
//        this.tournamentParticipantRepository = tournamentParticipantRepository;
//        this.logger = LoggerFactory.getLogger(getClass());
    }
    public List<Tournament> getAllTournaments() {
        return tournamentRepository.findAll();
    }

    public Tournament getTournamentById(Long id) { //TODO: write a similar function to other service classes
        return tournamentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tournament not found"));
    }

    public Tournament createTournament(Tournament tournament) {
        return tournamentRepository.save(tournament);
    }

    public void deleteTournament(Long id) {
        tournamentRepository.deleteById(id);
    }

//    @Transactional
//    public TournamentParticipant joinTournament(Long tournamentId, Long userId) {
//        Tournament tournament = getTournamentById(tournamentId);
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
//
//        if (user.getCoins() < 1000){
//            // Throw error
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The specified user doesn't have enough coins");
//        }
//        if (user.getLevel() < 20){
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The specified user isn't at least level 20");
//        }
//
//        TournamentBracket bracket = findOrCreateBracket(tournament, user);
//        TournamentParticipant participant = new TournamentParticipant();
//        participant.setUser(user);
//        participant.setTournamentBracket(bracket);
//        user.setCoins(user.getCoins() - 1000);
//
//        logger.info("User with ID {} joined tournament with ID {}. Coins deducted: 1000, Remaining coins: {}",
//                userId, tournamentId, user.getCoins());
//        return tournamentParticipantRepository.save(participant);
//    }

//    private TournamentBracket findOrCreateBracket(Tournament tournament, User user) {
//        List<TournamentBracket> brackets = tournamentRepository.getEmptyBrackets(tournament.getId());
//        for (TournamentBracket bracket : brackets) {
//            if (tournamentBracketRepository.canJoin(bracket, user)) {
//                return bracket;
//            }
//        }
//        // No suitable brackets, create a new one
//        TournamentBracket newBracket = new TournamentBracket();
//        newBracket.setTournament(tournament);
//        return tournamentBracketRepository.save(newBracket);
//    }
}