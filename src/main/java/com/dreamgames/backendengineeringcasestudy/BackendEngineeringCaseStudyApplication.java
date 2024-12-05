package com.dreamgames.backendengineeringcasestudy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication()
public class BackendEngineeringCaseStudyApplication {

    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(BackendEngineeringCaseStudyApplication.class, args);
        System.out.println("**********************************************************************");
        System.out.println("************* Server is started. Listening port 8080 ... *************");
        System.out.println("**********************************************************************");
    }

}
