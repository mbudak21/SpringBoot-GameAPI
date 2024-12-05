import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = com.dreamgames.backendengineeringcasestudy.BackendEngineeringCaseStudyApplication.class)

public class CountryControllerTests {
    String url = "http://localhost:8080/api/countries";

    @Test
    void testGetAllCountries() {
        // Just make sure the initial countries are instantiated
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);
        String expectedResponse =
                "[{\"code\":\"FRA\",\"name\":\"France\"}," +
                "{\"code\":\"GER\",\"name\":\"Germany\"}," +
                "{\"code\":\"TR\",\"name\":\"Turkey\"}," +
                "{\"code\":\"UK\",\"name\":\"United Kingdom\"}," +
                "{\"code\":\"US\",\"name\":\"United States\"}]";

        assertEquals(expectedResponse, response, "The initial country values are different from the assumption!");
    }
}
