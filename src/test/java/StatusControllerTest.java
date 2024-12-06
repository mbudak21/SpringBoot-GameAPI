import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = com.dreamgames.backendengineeringcasestudy.BackendEngineeringCaseStudyApplication.class)
@AutoConfigureMockMvc
public class StatusControllerTest {
    @Autowired
    private MockMvc mockMvc;
    private String endpoint = "http://localhost:8080/status";

    @Test
    public void testGetStatus() throws Exception {
        mockMvc.perform(get(endpoint))
                .andExpect(status().isOk())
                .andExpect(content().string("Server is up!"));
    }
}
