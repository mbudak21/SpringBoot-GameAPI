import com.dreamgames.backendengineeringcasestudy.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.event.annotation.BeforeTestExecution;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.util.AssertionErrors.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = com.dreamgames.backendengineeringcasestudy.BackendEngineeringCaseStudyApplication.class)
public class UserControllerTests {
    String url = "http://localhost:8080/api/users";

    @Test
    void testNoUsersPresent() {
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);
        String expectedResponse = "[]";

        assertEquals(expectedResponse, response, "The DB is not empty!");
    }

    @Test
    void testCreateUser() {
        RestTemplate restTemplate = new RestTemplate();
        String createUserUrl = url + "/create";
        String listUsersUrl = url + "/users";

        // Include the 'username' as a query parameter
        String username = "testUser";
        String fullUrl = createUserUrl + "?username=" + username;

        // Send POST request with the username query parameter
        ResponseEntity<User> createResponse = restTemplate.exchange(fullUrl, HttpMethod.POST, null, User.class);

        // Verify the response status is 201 Created
        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode(), "User creation failed!");

//        // Retrieve the list of users
//        String rawResponse = String.valueOf(restTemplate.getForObject(listUsersUrl, Long.class));
//        assertEquals(1, 2, rawResponse);
//        System.out.println("Response: " + rawResponse);
//
//        ResponseEntity<String> listResponse = restTemplate.getForEntity(listUsersUrl, String.class);
//        System.out.println(listResponse.getBody());
//
//        // Verify the response contains the new user
//        assertNotNull(listResponse.getBody(), "Response body is null!");
//        assertTrue(Objects.requireNonNull(listResponse.getBody()).contains(username), "The new user is not in the list!");
    }
}
