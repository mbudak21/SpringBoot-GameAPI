package com.dreamgames.backendengineeringcasestudy.repository;

import com.dreamgames.backendengineeringcasestudy.dto.CountryLeaderboardDTO;
import com.dreamgames.backendengineeringcasestudy.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CountryRepository extends JpaRepository<Country, String> {

    @Query(value = "SELECT * FROM countries ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Country findRandomCountry();

    @Query
    (
       """
       SELECT new com.dreamgames.backendengineeringcasestudy.dto.CountryLeaderboardDTO(c.name, SUM(tp.score))
       FROM TournamentParticipant tp
       JOIN User u ON tp.user.id = u.id
       JOIN Country c ON u.country.code = c.code
       WHERE tp.tournamentBracket.tournament.id = :tournamentId
       GROUP BY c.name
       ORDER BY SUM(tp.score) DESC
       """
    )
    List<CountryLeaderboardDTO> getCountryLeaderboard(@Param("tournamentId") Long tournamentId);


}