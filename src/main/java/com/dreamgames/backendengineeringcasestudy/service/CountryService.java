package com.dreamgames.backendengineeringcasestudy.service;

import com.dreamgames.backendengineeringcasestudy.model.Country;
import com.dreamgames.backendengineeringcasestudy.repository.CountryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountryService {

    private final CountryRepository countryRepository;

    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    public Country getRandomCountry() {
        return countryRepository.findRandomCountry();
    }
    public String getRandomCountryCode() {
        return getRandomCountry().getCode();
    }
    public List<Country> getAllCountries() {
        return countryRepository.findAll();
    }
}
