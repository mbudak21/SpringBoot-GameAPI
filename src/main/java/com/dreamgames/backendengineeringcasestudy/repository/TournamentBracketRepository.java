package com.dreamgames.backendengineeringcasestudy.repository;

import com.dreamgames.backendengineeringcasestudy.model.Tournament;
import com.dreamgames.backendengineeringcasestudy.model.TournamentBracket;
import com.dreamgames.backendengineeringcasestudy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TournamentBracketRepository extends JpaRepository<TournamentBracket, Long> {

    @Query(
            """
            SELECT 
                tb
            FROM 
                TournamentBracket tb
            LEFT JOIN 
                TournamentParticipant tp ON tb.id = tp.tournamentBracket.id
            WHERE 
                tb.tournament = :tournament
            GROUP BY 
                tb.id, tb.maxTeams, tb.participantsPerTeam
            HAVING 
                COUNT(tp.id) < (tb.maxTeams * tb.participantsPerTeam)
            """
    )
    List<TournamentBracket> getEmptyBrackets(@Param("tournament") Tournament tournament);

    List<TournamentBracket> findByTournamentId(Long tournamentId);

    @Query(
            """
            SELECT CASE WHEN COUNT(tp.id) >= (tb.maxTeams * tb.participantsPerTeam) THEN true ELSE false END
            FROM TournamentParticipant tp
            JOIN tp.tournamentBracket tb
            WHERE tb.id = :bracketID
            """
    )
    Boolean isBracketFull(@Param("bracketID") Long bracketID);

    @Query(
            """
            SELECT CASE WHEN
                (t.startTime <= CURRENT_TIMESTAMP AND t.endTime > CURRENT_TIMESTAMP)
                AND
                (COUNT(tp.id) >= (tb.maxTeams * tb.participantsPerTeam))
            THEN true ELSE false END
            FROM TournamentBracket tb
            JOIN tb.tournament t
            LEFT JOIN TournamentParticipant tp ON tp.tournamentBracket.id = tb.id
            WHERE tb.id = :bracketID
            GROUP BY tb.id, t.startTime, t.endTime, tb.maxTeams, tb.participantsPerTeam
            """
    )
    Boolean isBracketBeingPlayed(@Param("bracketID") Long bracketID);


    //boolean canJoin(TournamentBracket bracket, User user);
}
