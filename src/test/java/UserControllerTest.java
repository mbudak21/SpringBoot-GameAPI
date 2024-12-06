import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import com.dreamgames.backendengineeringcasestudy.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = com.dreamgames.backendengineeringcasestudy.BackendEngineeringCaseStudyApplication.class)
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final String testUserName = "testUser";

    @BeforeEach
    void ensureClearDatabase() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void testCreateGetAndDeleteUser() throws Exception {
        // 1. Create a user
        String createUserUrl = "/api/users/create?username=" + testUserName;

        MvcResult createUserResult = mockMvc.perform(post(createUserUrl))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value(testUserName))
                .andReturn();

        // Extract user ID from the response
        String responseContent = createUserResult.getResponse().getContentAsString();
        User createdUser = new ObjectMapper().readValue(responseContent, User.class);
        Long userId = createdUser.getId();

        // 2. Get the user's data
        mockMvc.perform(get("/api/users/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.username").value(testUserName));

        // 3. Delete the user
        mockMvc.perform(delete("/api/users/" + userId))
                .andExpect(status().isNoContent());

        // Verify the user is deleted
        mockMvc.perform(get("/api/users/" + userId))
                .andExpect(status().isNotFound());
    }
    @Test
    void testCreateUserWithDuplicateNickname() throws Exception {
        // 1. Create a user
        String createUserUrl = "/api/users/create?username=" + testUserName;

        MvcResult createUserResult = mockMvc.perform(post(createUserUrl))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value(testUserName))
                .andReturn();

        // Extract user ID from the response
        String responseContent = createUserResult.getResponse().getContentAsString();
        User createdUser = new ObjectMapper().readValue(responseContent, User.class);
        Long userId = createdUser.getId();

        // 2. Attempt to create another user with the same nickname
        mockMvc.perform(post(createUserUrl))
                .andExpect(status().isBadRequest()) // Expecting a 400 Bad Request or any other error status your application uses
                .andExpect(jsonPath("$.error").exists()) // Assuming error details are returned in the response
                .andExpect(jsonPath("$.error").value("Username already exists")); // Adjust the expected error message if applicable

        // 3. Delete the user
        mockMvc.perform(delete("/api/users/" + userId))
                .andExpect(status().isNoContent());

        // Verify the user is deleted
        mockMvc.perform(get("/api/users/" + userId))
                .andExpect(status().isNotFound());
    }
}