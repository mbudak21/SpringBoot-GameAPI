package com.dreamgames.backendengineeringcasestudy.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tournament_participants", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"tournament_bracket_id", "user_id"})
})
public class TournamentParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne //(fetch = FetchType.LAZY)
    @JoinColumn(name = "tournament_bracket_id", nullable = false)
    private TournamentBracket tournamentBracket;

    @ManyToOne //(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "score", nullable = false)
    private Integer score = 0;

    @Column(name = "team", nullable = false)
    private Integer team = 0;

    @Column(name = "reward_claimed", nullable = false)
    private Boolean rewardClaimed = false;

    // Getters and Setters
    public TournamentBracket getTournamentBracket() {
        return tournamentBracket;
    }

    public void setTournamentBracket(TournamentBracket tournamentBracket) {
        this.tournamentBracket = tournamentBracket;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getTeam() {
        return team;
    }

    public void setTeam(Integer team) {
        this.team = team;
    }

    public Boolean getRewardClaimed() {
        return rewardClaimed;
    }

    public void setRewardClaimed(Boolean rewardClaimed) {
        this.rewardClaimed = rewardClaimed;
    }
}