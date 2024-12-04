package com.dreamgames.backendengineeringcasestudy.service;

import com.dreamgames.backendengineeringcasestudy.dto.GroupLeaderboardDTO;
import com.dreamgames.backendengineeringcasestudy.model.TournamentBracket;
import com.dreamgames.backendengineeringcasestudy.model.TournamentParticipant;
import com.dreamgames.backendengineeringcasestudy.model.User;
import com.dreamgames.backendengineeringcasestudy.repository.TournamentBracketRepository;
import com.dreamgames.backendengineeringcasestudy.repository.TournamentParticipantRepository;
import com.dreamgames.backendengineeringcasestudy.repository.TournamentRepository;
import com.dreamgames.backendengineeringcasestudy.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TournamentBracketService {
    private final TournamentRepository tournamentRepository;
    private final UserRepository userRepository;
    private final TournamentBracketRepository tournamentBracketRepository;
    private final TournamentParticipantRepository tournamentParticipantRepository;

    public TournamentBracketService(TournamentRepository tournamentRepository, UserRepository userRepository, TournamentBracketRepository tournamentBracketRepository, TournamentParticipantRepository tournamentParticipantRepository) {
        this.tournamentRepository = tournamentRepository;
        this.userRepository = userRepository;
        this.tournamentBracketRepository = tournamentBracketRepository;
        this.tournamentParticipantRepository = tournamentParticipantRepository;
    }

    public List<TournamentBracket> getBrackets(Long tournamentId) {
        return tournamentBracketRepository.findByTournamentId(tournamentId);
    }
    public List<GroupLeaderboardDTO> getGroupLeaderboardByBracket(TournamentBracket bracket) {
        return tournamentParticipantRepository.getGroupLeaderboard(bracket.getId());
    }
}
