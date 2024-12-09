package com.dreamgames.backendengineeringcasestudy.dto;

public class CountryLeaderboardDTO {
    private final String countryName;
    private final Long totalTournamentScore;

    public CountryLeaderboardDTO(String countryName, Long totalTournamentScore) {
        this.countryName = countryName;
        this.totalTournamentScore = totalTournamentScore;
    }

    public String getCountryName() {
        return countryName;
    }

    public Long getTotalTournamentScore() {
        return totalTournamentScore;
    }
}
