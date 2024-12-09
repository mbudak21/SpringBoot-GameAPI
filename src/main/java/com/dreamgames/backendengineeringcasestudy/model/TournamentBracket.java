package com.dreamgames.backendengineeringcasestudy.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "tournament_bracket")
public class TournamentBracket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne //(fetch = FetchType.LAZY)
    @JoinColumn(name = "tournament_id", nullable = false)
    @JsonIgnore
    private Tournament tournament;

    @Column(name = "max_teams", nullable = false)
    private Integer maxTeams = 2;

    @Column(name = "participants_per_team", nullable = false)
    private Integer participantsPerTeam = 5;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    public Integer getMaxTeams() {
        return maxTeams;
    }

    public void setMaxTeams(Integer maxTeams) {
        this.maxTeams = maxTeams;
    }

    public Integer getParticipantsPerTeam() {
        return participantsPerTeam;
    }

    public void setParticipantsPerTeam(Integer participantsPerTeam) {
        this.participantsPerTeam = participantsPerTeam;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
