package com.dreamgames.backendengineeringcasestudy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BackendEngineeringCaseStudyApplicationTests {

//    @Autowired
//    private MockMvc mockMvc;
//
//    @Test
//    void statusEndpoint() throws Exception {
////        mockMvc.perform(get("/status"))
////                .andExpect(status().isOk())
////                .andExpect(content().string("Server is up!"));
//    }
//    @Test
//    void rootEndpoint() throws Exception {
////        mockMvc.perform(get("/"))
////                .andExpect(status().isOk())
////                .andExpect(content().string("Welcome to the SpringBoot Server!"));
//    }
}
