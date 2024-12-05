import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = com.dreamgames.backendengineeringcasestudy.BackendEngineeringCaseStudyApplication.class)
class SanityCheck {

    @Test
    void testAddition() {
        int result = 1 + 1;
        assertEquals(2, result, "1 + 1 should equal 2");
    }
}