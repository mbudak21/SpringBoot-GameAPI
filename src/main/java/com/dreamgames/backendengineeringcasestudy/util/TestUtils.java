package com.dreamgames.backendengineeringcasestudy.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import com.jayway.jsonpath.JsonPath;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@Component
public class TestUtils {

    private final String createUserEndpoint = "/api/users/create";
    private final String createTournamentEndpoint = "/api/tournaments/create";

    public Long createUser(MockMvc mockMvc, String username) throws Exception {
        String createUserUrl = createUserEndpoint + "?username=" + username;
        MvcResult createUserResult = mockMvc.perform(post(createUserUrl))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value(username))
                .andReturn();

        String responseContent = createUserResult.getResponse().getContentAsString();
        return JsonPath.parse(responseContent).read("$.id", Long.class);
    }

    public Long createTournament(MockMvc mockMvc, LocalDateTime startTime, LocalDateTime endTime, String description) throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        String start = startTime.format(formatter);
        String end = endTime.format(formatter);
        // Step 1: Perform the POST request to create a tournament
        MvcResult createResult = mockMvc.perform(post(createTournamentEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"startTime\":\"" + start + "\",\"endTime\":\"" + end + "\",\"description\":\"" + description + "\"}"))
                .andExpect(status().isCreated()) // Ensure the response status is 201 Created
                .andExpect(jsonPath("$.id").exists()) // Ensure the response includes the ID
                .andExpect(jsonPath("$.startTime").value(start)) // Validate the startTime
                .andExpect(jsonPath("$.endTime").value(end)) // Validate the endTime
                .andExpect(jsonPath("$.description").value(description)) // Validate the description
                .andExpect(jsonPath("$.isActive").value((startTime.isEqual(LocalDateTime.now()) || startTime.isBefore(LocalDateTime.now())) && endTime.isAfter(LocalDateTime.now())))
                .andReturn();

        // Step 2: Extract and return the tournament ID from the response
        String responseContent = createResult.getResponse().getContentAsString();
        return JsonPath.parse(responseContent).read("$.id", Long.class);
    }
}
