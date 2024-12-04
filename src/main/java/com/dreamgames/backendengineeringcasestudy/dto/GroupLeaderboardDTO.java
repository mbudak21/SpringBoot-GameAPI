package com.dreamgames.backendengineeringcasestudy.dto;

public class GroupLeaderboardDTO {
    private Long userId;
    private String username;
    private String country;
    private Integer team;
    private Integer tournamentScore;

    public GroupLeaderboardDTO(Long userId, String username, String country, Integer team, Integer tournamentScore) {
        this.userId = userId;
        this.username = username;
        this.country = country;
        this.team = team;
        this.tournamentScore = tournamentScore;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getTeam() {
        return team;
    }

    public void setTeam(Integer team) {
        this.team = team;
    }

    public Integer getTournamentScore() {
        return tournamentScore;
    }

    public void setTournamentScore(Integer tournamentScore) {
        this.tournamentScore = tournamentScore;
    }
}
