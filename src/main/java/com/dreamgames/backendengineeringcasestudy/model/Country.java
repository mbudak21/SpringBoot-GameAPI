package com.dreamgames.backendengineeringcasestudy.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "countries")
public class Country {

    @Id
    @Column(name = "code", length = 3, columnDefinition = "CHAR(3)", nullable = false, unique = true)
    private String code; // ISO 3166-1 alpha-3 standard

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    // Default constructor
    public Country() {
    }

    // Constructor with fields
    public Country(String code, String name) {
        this.code = code;
        this.name = name;
    }

    // Getters and setters
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // toString method (optional, for debugging purposes)
    @Override
    public String toString() {
        return "Country{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}