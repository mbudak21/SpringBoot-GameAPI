package com.dreamgames.backendengineeringcasestudy.controller;

import com.dreamgames.backendengineeringcasestudy.dto.CountryLeaderboardDTO;
import com.dreamgames.backendengineeringcasestudy.model.Country;
import com.dreamgames.backendengineeringcasestudy.service.CountryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/countries")
public class CountryController {
    private final CountryService countryService;
    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping
    public List<Country> getAllCountries() {
        return countryService.getAllCountries();
    }

    @GetMapping("/{tournamentId}")
    public List<CountryLeaderboardDTO> getCountryLeaderboard(@PathVariable Long tournamentId) {
        return countryService.getCountryLeaderboard(tournamentId);
    }

}
