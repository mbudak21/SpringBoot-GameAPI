import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = com.dreamgames.backendengineeringcasestudy.BackendEngineeringCaseStudyApplication.class)
public class StatusControllerTests {
    String url = "http://localhost:8080/status";

    @Test
    void testGetStatusEndpoint() {
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);

        assertEquals("Server is up!", response, "The response from /status endpoint should be 'Server is up!'");
    }

    @Test
    void testStatusEndpointWithPost() {
        RestTemplate restTemplate = new RestTemplate();

        // Create an HttpEntity object with any necessary headers or body (optional for this test)
        HttpEntity<String> requestEntity = new HttpEntity<>(null);

        // Try to send a POST request
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);

            // Assert the response behavior, e.g., status code 405 for Method Not Allowed
            assertEquals(HttpStatus.METHOD_NOT_ALLOWED, response.getStatusCode(),
                    "POST request to /status endpoint should return 405 Method Not Allowed");
        } catch (HttpClientErrorException e) {
            // If a client error exception occurs, check the status code
            assertEquals(HttpStatus.METHOD_NOT_ALLOWED, e.getStatusCode(),
                    "POST request to /status endpoint should result in 405 Method Not Allowed");
        }
    }

}