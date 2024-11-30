package com.dreamgames.backendengineeringcasestudy.repository;

import com.dreamgames.backendengineeringcasestudy.model.Tournament;
import com.dreamgames.backendengineeringcasestudy.model.TournamentBracket;
import com.dreamgames.backendengineeringcasestudy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TournamentBracketRepository extends JpaRepository<TournamentBracket, Long> {
    //boolean canJoin(TournamentBracket bracket, User user);
}
