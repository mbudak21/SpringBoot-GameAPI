import com.dreamgames.backendengineeringcasestudy.repository.UserRepository;
import com.dreamgames.backendengineeringcasestudy.util.TestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = {com.dreamgames.backendengineeringcasestudy.BackendEngineeringCaseStudyApplication.class, })
@AutoConfigureMockMvc
public class UserControllerTest {

    private final String getUsersEndpoint = "/api/users";
    private final String createUserEndpoint = "/api/users/create";
    private final String UserLevelUpEndpoint = "/levelUp";


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestUtils testUtils;

    @BeforeEach
    void ensureClearDatabase() throws Exception {
        mockMvc.perform(get(getUsersEndpoint))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
        // Create some users
        testUtils.createUser(mockMvc, "TestUser1");
        testUtils.createUser(mockMvc, "TestUser2");
        testUtils.createUser(mockMvc, "TestUser3");
    }

    @Test
    void testDataInitialization() throws Exception {
        mockMvc.perform(get(getUsersEndpoint))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));
    }
    
    @Test
    void testCreateDuplicateUser() throws Exception {
        String createUserUrl = createUserEndpoint + "?username=" + "TestUser1";
        MvcResult createUserResult = mockMvc.perform(post(createUserUrl))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Username already exists"))
                .andReturn();
    }

    @Test
    void testUserLevelUp() throws Exception {
        Long uid = testUtils.createUser(mockMvc, "LevelUpTestUser");
        for (int i = 1; i <= 30; i++) {
            mockMvc.perform(patch(getUsersEndpoint + "/" + uid + UserLevelUpEndpoint))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(uid))
                    .andExpect(jsonPath("$.username").value("LevelUpTestUser"))
                    .andExpect(jsonPath("$.level").value(i+1))
                    .andExpect(jsonPath("$.coins").value(5000+25*i));
        }
    }

    @Test
    void testNonexistentUserLevelUp() throws Exception {
        mockMvc.perform(patch(getUsersEndpoint + "/" + Integer.MAX_VALUE + UserLevelUpEndpoint))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(containsString("User with ID " + Integer.MAX_VALUE + " not found")));
    }

    @AfterEach
    void clearDatabase() throws Exception {
        userRepository.deleteAll();
    }

//    @Test
//    void testCreateGetAndDeleteUser() throws Exception {
//        // 1. Create a user
//        String createUserUrl = "/api/users/create?username=" + testUserName;
//
//        MvcResult createUserResult = mockMvc.perform(post(createUserUrl))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.username").value(testUserName))
//                .andReturn();
//
//        // Extract user ID from the response
//        String responseContent = createUserResult.getResponse().getContentAsString();
//        User createdUser = new ObjectMapper().readValue(responseContent, User.class);
//        Long userId = createdUser.getId();
//
//        // 2. Get the user's data
//        mockMvc.perform(get("/api/users/" + userId))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(userId))
//                .andExpect(jsonPath("$.username").value(testUserName));
//
//        // 3. Delete the user
//        mockMvc.perform(delete("/api/users/" + userId))
//                .andExpect(status().isNoContent());
//
//        // Verify the user is deleted
//        mockMvc.perform(get("/api/users/" + userId))
//                .andExpect(status().isNotFound());
//    }
//    @Test
//    void testCreateUserWithDuplicateNickname() throws Exception {
//        // 1. Create a user
//        String createUserUrl = "/api/users/create?username=" + testUserName;
//
//        MvcResult createUserResult = mockMvc.perform(post(createUserUrl))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.username").value(testUserName))
//                .andReturn();
//
//        // Extract user ID from the response
//        String responseContent = createUserResult.getResponse().getContentAsString();
//        User createdUser = new ObjectMapper().readValue(responseContent, User.class);
//        Long userId = createdUser.getId();
//
//        // 2. Attempt to create another user with the same nickname
//        mockMvc.perform(post(createUserUrl))
//                .andExpect(status().isBadRequest()) // Expecting a 400 Bad Request or any other error status your application uses
//                .andExpect(jsonPath("$.error").exists()) // Assuming error details are returned in the response
//                .andExpect(jsonPath("$.error").value("Username already exists")); // Adjust the expected error message if applicable
//
//        // 3. Delete the user
//        mockMvc.perform(delete("/api/users/" + userId))
//                .andExpect(status().isNoContent());
//
//        // Verify the user is deleted
//        mockMvc.perform(get("/api/users/" + userId))
//                .andExpect(status().isNotFound());
//    }
}