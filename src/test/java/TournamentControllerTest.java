import com.dreamgames.backendengineeringcasestudy.dto.GroupLeaderboardDTO;
import com.dreamgames.backendengineeringcasestudy.model.TournamentBracket;
import com.dreamgames.backendengineeringcasestudy.model.TournamentParticipant;
import com.dreamgames.backendengineeringcasestudy.model.User;
import com.dreamgames.backendengineeringcasestudy.repository.TournamentBracketRepository;
import com.dreamgames.backendengineeringcasestudy.repository.TournamentParticipantRepository;
import com.dreamgames.backendengineeringcasestudy.repository.TournamentRepository;
import com.dreamgames.backendengineeringcasestudy.repository.UserRepository;
import com.dreamgames.backendengineeringcasestudy.util.TestUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;




@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = com.dreamgames.backendengineeringcasestudy.BackendEngineeringCaseStudyApplication.class)
@AutoConfigureMockMvc
public class TournamentControllerTest {

    private final String getUsersEndpoint = "/api/users";
    private final String UserLevelUpEndpoint = "levelUp";
    private final String getTournamentEndpoint = "/api/tournaments";
    private final String createTournamentEndpoint = "/api/tournaments/create";
    private final String joinTournamentEndpoint = "/enter";


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestUtils testUtils;
    @Autowired
    private TournamentRepository tournamentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TournamentBracketRepository tournamentBracketRepository;
    @Autowired
    private TournamentParticipantRepository tournamentParticipantRepository;


    @BeforeEach
    void setupTestData() throws Exception {
        // Make sure there is no other data
        mockMvc.perform(get(getTournamentEndpoint))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        testUtils.createTournament(mockMvc, LocalDateTime.now().minusDays(2), LocalDateTime.now().minusDays(1), "Already finished");
        testUtils.createTournament(mockMvc, LocalDateTime.now(), LocalDateTime.now().plusDays(1), "Currently open to join");
        testUtils.createTournament(mockMvc, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2), "Will be open to join in the future");

        for (int i = 0; i < 100; i++) {
            testUtils.createUser(mockMvc, "testuser" + i);
        }
    }

    @AfterEach
    void clearDatabase() throws Exception {
        // Reset DB to a clean state
        tournamentParticipantRepository.deleteAll();
        tournamentBracketRepository.deleteAll();
        tournamentRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void testDataInitialization() throws Exception {
        mockMvc.perform(get(getTournamentEndpoint))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3)); // Check number of tournaments
    }

    @Test
    void testGetTournamentByID() throws Exception {
        // Step 1: Create a new tournament
        LocalDateTime startTime = LocalDateTime.now().minusDays(1);
        LocalDateTime endTime = LocalDateTime.now().plusDays(2);
        String description = "testGetTournamentByID Tournament";
        Long id = testUtils.createTournament(mockMvc, startTime, endTime, description);

        // Step 2: Retrieve the tournament by its ID using GET
        mockMvc.perform(get(getTournamentEndpoint + "/" + id))
                .andExpect(status().isOk()) // Ensure the request is successful
                .andExpect(jsonPath("$.id").value(id)) // Validate the ID
                .andExpect(jsonPath("$.startTime").value(startTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))) // Validate startTime
                .andExpect(jsonPath("$.endTime").value(endTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))) // Validate endTime
                .andExpect(jsonPath("$.description").value(description)) // Validate description
                .andExpect(jsonPath("$.isActive").value((startTime.isEqual(LocalDateTime.now()) || startTime.isBefore(LocalDateTime.now())) && endTime.isAfter(LocalDateTime.now())));
    }

    @Test
    void testJoinTournamentWithNoLevel() throws Exception {
        Long uid = testUtils.createUser(mockMvc, "noLevelTestUser");
        Long tid = testUtils.createTournament(mockMvc, LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(1), "noLevelTestTournament");
        mockMvc.perform(post("/api/tournaments/" + tid + "/enter?userId=" + uid)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden()) // Check for 403 status
                .andExpect(jsonPath("$.error").value("User must be level 20 or above to join a tournament. User is currently level 1")); // Check error message

        // Add 1 level, try again
        mockMvc.perform(patch(getUsersEndpoint + "/" + uid + "/" + UserLevelUpEndpoint))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(uid))
                .andExpect(jsonPath("$.username").value("noLevelTestUser"))
                .andExpect(jsonPath("$.level").value(2));

        mockMvc.perform(post("/api/tournaments/" + tid + "/enter?userId=" + uid)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden()) // Check for 403 status
                .andExpect(jsonPath("$.error").value("User must be level 20 or above to join a tournament. User is currently level 2")); // Check error message


    }

    @Test
    void testJoinTournamentInactive() throws Exception {
        Long uid = testUtils.createUser(mockMvc, "InactiveTournamentTestUser");

        // Level up to 20
        for (int i = 1; i <= 20; i++) {
            mockMvc.perform(patch(getUsersEndpoint + "/" + uid + "/" + UserLevelUpEndpoint));
        }

        // 1. Past inactive tournament
        Long tid1 = testUtils.createTournament(mockMvc, LocalDateTime.now().minusDays(2), LocalDateTime.now().minusDays(1), "Past Test Tournament");
        String url1 = getTournamentEndpoint + "/" + tid1 + "/" + joinTournamentEndpoint +  "?userId=" + uid;

        mockMvc.perform(post(url1))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error").value("This tournament is already over"));


        // 2. Future inactive tournament
        Long tid2 = testUtils.createTournament(mockMvc, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2), "Past Test Tournament");
        String url2 = getTournamentEndpoint + "/" + tid2 + "/" + joinTournamentEndpoint +  "?userId=" + uid;

        mockMvc.perform(post(url2))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error").value("This tournament is already over"));
    }


    @Test
    void testJoinTournamentTwice() throws Exception {
        Long uid = testUtils.createUser(mockMvc, "JoinTournamentTwice");

        // Level up to 20
        for (int i = 1; i <= 20; i++) {
            mockMvc.perform(patch(getUsersEndpoint + "/" + uid + "/" + UserLevelUpEndpoint));
        }

        Long tid = testUtils.createTournament(mockMvc, LocalDateTime.now().minusDays(2), LocalDateTime.now().plusDays(1), "Active Test Tournament");
        String url = getTournamentEndpoint + "/" + tid + "/" + joinTournamentEndpoint +  "?userId=" + uid;

        mockMvc.perform(post(url))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error").value("This tournament is already over"));

    }

//    @Test
//    void testGetBracketsWithIndex() throws Exception {
//        TournamentBracketDTO bracketDTO = new TournamentBracketDTO(1, "Bracket 1", List.of());
//        Mockito.when(tournamentService.getTournamentBracketDTO(1L, 0)).thenReturn(bracketDTO);
//
//        mockMvc.perform(get("/api/tournaments/1/brackets")
//                        .param("bracket_index", "0"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.bracketIndex").value(1))
//                .andExpect(jsonPath("$.name").value("Bracket 1"));
//    }
//
//    @Test
//    void testGetBracketsNotFound() throws Exception {
//        Mockito.when(tournamentBracketService.getBrackets(1L)).thenReturn(List.of());
//
//        mockMvc.perform(get("/api/tournaments/1/brackets"))
//                .andExpect(status().isNotFound())
//                .andExpect(content().string("No brackets found for tournament ID: 1"));
//    }
}
