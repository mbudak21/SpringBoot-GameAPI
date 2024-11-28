package com.dreamgames.backendengineeringcasestudy.service;

import com.dreamgames.backendengineeringcasestudy.model.Country;
import com.dreamgames.backendengineeringcasestudy.repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountryService {

    @Autowired
    private CountryRepository countryRepository;

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
