package com.dreamgames.backendengineeringcasestudy.controller;

import com.dreamgames.backendengineeringcasestudy.dto.GroupLeaderboardDTO;
import com.dreamgames.backendengineeringcasestudy.model.Tournament;
import com.dreamgames.backendengineeringcasestudy.model.TournamentBracket;
import com.dreamgames.backendengineeringcasestudy.model.TournamentParticipant;
import com.dreamgames.backendengineeringcasestudy.service.TournamentBracketService;
import com.dreamgames.backendengineeringcasestudy.service.TournamentService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/api/tournaments")
public class TournamentController {
    private final TournamentService tournamentService;
    private final TournamentBracketService tournamentBracketService;
    public TournamentController(TournamentService tournamentService, TournamentBracketService tournamentBracketService) {
        this.tournamentService = tournamentService;
        this.tournamentBracketService = tournamentBracketService;
    }

    @GetMapping("all")
    public List<Tournament> getAllTournaments(@RequestParam(required = false) Boolean isActive) {
        return tournamentService.getAllTournaments(isActive);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Tournament> getTournamentById(@PathVariable Long id) {
        Tournament tournament = tournamentService.getTournamentById(id);
        return ResponseEntity.ok(tournament);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createTournament(@Valid @RequestBody Tournament tournament) {
        Tournament createdTournament = tournamentService.createTournament(tournament);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTournament);
    }

    @PostMapping("/{tournamentId}/enter")
    public ResponseEntity<List<GroupLeaderboardDTO>> enterTournament(@PathVariable Long tournamentId, @RequestParam Long userId) {
        TournamentParticipant tournamentParticipant = tournamentService.joinTournament(tournamentId, userId);
        TournamentBracket bracket = tournamentParticipant.getTournamentBracket();
        return ResponseEntity.ok(tournamentBracketService.getGroupLeaderboardByBracket(bracket));
    }
    @GetMapping("/{tournamentId}/brackets")
    public ResponseEntity<?> getBrackets(@PathVariable Long tournamentId, @RequestParam(required = false) Integer bracket_index) {
        if (bracket_index == null) { // Return all brackets in a compact format
            List<TournamentBracket> brackets = tournamentBracketService.getBrackets(tournamentId);
            if (brackets.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No brackets found for tournament ID: " + tournamentId);
            }
            return ResponseEntity.ok(brackets);
        }
        else { // Return the DTO of the bracket
            return ResponseEntity.ok(tournamentService.getTournamentBracketDTO(tournamentId, bracket_index));
        }
    }





//    @PostMapping("/{tournamentId}/claim-reward")
//    public ResponseEntity<User> claimReward(
//            @PathVariable Long tournamentId,
//            @RequestParam Long userId) {
//        User updatedUser = tournamentService.claimReward(tournamentId, userId);
//        return ResponseEntity.ok(updatedUser);
//    }

}
