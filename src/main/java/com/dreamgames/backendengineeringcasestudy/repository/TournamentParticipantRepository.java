package com.dreamgames.backendengineeringcasestudy.repository;

import com.dreamgames.backendengineeringcasestudy.dto.GroupLeaderboardDTO;
import com.dreamgames.backendengineeringcasestudy.model.TournamentBracket;
import com.dreamgames.backendengineeringcasestudy.model.TournamentParticipant;
import com.dreamgames.backendengineeringcasestudy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TournamentParticipantRepository extends JpaRepository<TournamentParticipant, Long> {
    @Query(
            """
            SELECT new com.dreamgames.backendengineeringcasestudy.dto.GroupLeaderboardDTO(
                u.id, u.username, c.name, tp.team, tp.score
            )
            FROM 
                TournamentParticipant tp
            JOIN 
                tp.user u
            JOIN 
                u.country c
            WHERE 
                tp.tournamentBracket.id = :bracketId
            ORDER BY 
                tp.team ASC, tp.score DESC
            """
    )
    List<GroupLeaderboardDTO> getGroupLeaderboard(@Param("bracketId") Long bracketId);

    List<TournamentParticipant> findByTournamentBracket(TournamentBracket bracket);

    @Query(
            """
            SELECT tp
            FROM TournamentParticipant tp
            WHERE tp.user = :user
              AND tp.tournamentBracket.tournament IN (
                SELECT t
                FROM Tournament t
                WHERE t.startTime <= CURRENT_TIMESTAMP AND CURRENT_TIMESTAMP < t.endTime
              )
              AND (
                SELECT COUNT(participant.id)
                FROM TournamentParticipant participant
                WHERE participant.tournamentBracket = tp.tournamentBracket
              ) < (tp.tournamentBracket.maxTeams * tp.tournamentBracket.participantsPerTeam)
            """
    )
    List<TournamentParticipant> getActiveParticipations(@Param("user") User user); // tournament.isActive() && bracket.notFull()
}
