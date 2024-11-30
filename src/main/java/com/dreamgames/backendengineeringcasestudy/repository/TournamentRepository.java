package com.dreamgames.backendengineeringcasestudy.repository;

import com.dreamgames.backendengineeringcasestudy.model.Tournament;
import com.dreamgames.backendengineeringcasestudy.model.TournamentBracket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Long> {
//    @Query("SELECT t FROM Tournament t WHERE :now BETWEEN t.startTime AND t.endTime")
//    List<Tournament> findActiveTournaments(@Param("now") LocalDateTime now);

    //@Query("A")
    //List<TournamentBracket> getEmptyBrackets(Long tournamentId);
}