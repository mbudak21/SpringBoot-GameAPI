package com.dreamgames.backendengineeringcasestudy.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class RootController {

    @GetMapping("/")
    public String root() {
        return "Welcome to the SpringBoot Server!";
    }
    // TODO: Return a string that has information about the server api and endpoints.
}
