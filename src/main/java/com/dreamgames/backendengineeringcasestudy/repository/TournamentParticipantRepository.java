package com.dreamgames.backendengineeringcasestudy.repository;

import com.dreamgames.backendengineeringcasestudy.model.TournamentBracket;
import com.dreamgames.backendengineeringcasestudy.model.TournamentParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface TournamentParticipantRepository extends JpaRepository<TournamentParticipant, Long> {

}
