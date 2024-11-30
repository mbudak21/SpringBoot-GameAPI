package com.dreamgames.backendengineeringcasestudy.controller;

import com.dreamgames.backendengineeringcasestudy.model.Tournament;
import com.dreamgames.backendengineeringcasestudy.model.User;
import com.dreamgames.backendengineeringcasestudy.repository.TournamentRepository;
import com.dreamgames.backendengineeringcasestudy.service.TournamentService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/tournaments")
public class TournamentController {
    private final TournamentService tournamentService;
    public TournamentController(TournamentService tournamentService) {
        this.tournamentService = tournamentService;
    }

    @GetMapping("all")
    public List<Tournament> getAllTournaments() {
        return tournamentService.getAllTournaments();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tournament> getTournamentById(@PathVariable Long id) {
        Tournament tournament = tournamentService.getTournamentById(id);
        return ResponseEntity.ok(tournament);
    }

//    @PostMapping("/create")
//    public ResponseEntity<Tournament> createTournament(@RequestBody Tournament tournament) {
//        Tournament createdTournament = tournamentService.createTournament(tournament);
//        return ResponseEntity.status(HttpStatus.CREATED).body(createdTournament);
//    }



//    @PostMapping("/{tournamentId}/enter")
//    public ResponseEntity<GroupLeaderboard> enterTournament(
//            @PathVariable Long tournamentId,
//            @RequestParam Long userId) {
//        GroupLeaderboard leaderboard = tournamentService.enterTournament(tournamentId, userId);
//        return ResponseEntity.ok(leaderboard);
//    }

//    @PostMapping("/{tournamentId}/claim-reward")
//    public ResponseEntity<User> claimReward(
//            @PathVariable Long tournamentId,
//            @RequestParam Long userId) {
//        User updatedUser = tournamentService.claimReward(tournamentId, userId);
//        return ResponseEntity.ok(updatedUser);
//    }

}
