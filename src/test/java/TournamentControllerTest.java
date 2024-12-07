import com.dreamgames.backendengineeringcasestudy.repository.TournamentRepository;
import com.dreamgames.backendengineeringcasestudy.repository.UserRepository;
import com.dreamgames.backendengineeringcasestudy.util.TestUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;




@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = com.dreamgames.backendengineeringcasestudy.BackendEngineeringCaseStudyApplication.class)
@AutoConfigureMockMvc
public class TournamentControllerTest {

    private final String getTournamentEndpoint = "/api/tournaments";
    private final String createTournamentEndpoint = "/api/tournaments/create";
    private final String joinTournamentEndpoint = "/enter";


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TournamentRepository tournamentRepository;

    @Autowired
    private TestUtils testUtils;
    @Autowired
    private UserRepository userRepository;


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
        // Delete all the entry's in all the databases
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
    void testJoinTournament() throws Exception {

    }
//    @Test
//    void testCreateAndFetchTournament() throws Exception {
//        // 1. Create a tournament using POST
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
//        String startTime = LocalDateTime.now().plusDays(1).format(formatter);
//        String endTime = LocalDateTime.now().plusDays(2).format(formatter);
//        String description = "Integration Test Tournament";
//        // NOTE: datetime supplied is of the format :  2024-12-08T17:09:38.3407076
//        // while the datetime gotten is of the format: 2024-12-08T17:09:38
//        // Not sure why these match, they don't match if .format is not applied.
//
//
//
//        // Extract the created tournament ID
//        String responseContent = createResult.getResponse().getContentAsString();
//        Long tournamentId = JsonPath.parse(responseContent).read("$.id", Long.class);
//
//        // 2. Fetch the tournament by ID using GET
//        mockMvc.perform(get("/api/tournaments/" + tournamentId))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(tournamentId))
//                .andExpect(jsonPath("$.startTime").value(startTime))
//                .andExpect(jsonPath("$.endTime").value(endTime))
//                .andExpect(jsonPath("$.description").value(description));
//
//        // 3. Verify data in the database directly
//        Tournament tournament = tournamentRepository.findById(tournamentId).orElseThrow();
//        Assertions.assertEquals(description, tournament.getDescription());
//        Assertions.assertEquals(LocalDateTime.parse(startTime), tournament.getStartTime());
//        Assertions.assertEquals(LocalDateTime.parse(endTime), tournament.getEndTime());
//
//        // 4. Delete the tournament using the repository, because there are no endpoints for tournament deletion
//        tournamentRepository.deleteById(tournamentId);
//        Optional<Tournament> tournament1 = tournamentRepository.findById(tournamentId);
//        Assertions.assertFalse(tournament1.isPresent());
//    }



//    @Test
//    void testGetTournamentById() throws Exception {
//        Tournament tournament = new Tournament(1L, "Tournament A", true);
//        Mockito.when(tournamentService.getTournamentById(1L)).thenReturn(tournament);
//
//        mockMvc.perform(get("/api/tournaments/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1))
//                .andExpect(jsonPath("$.name").value("Tournament A"))
//                .andExpect(jsonPath("$.isActive").value(true));
//    }
//
//    @Test
//    void testCreateTournament() throws Exception {
//        Tournament tournament = new Tournament(null, "Tournament A", true);
//        Tournament createdTournament = new Tournament(1L, "Tournament A", true);
//        Mockito.when(tournamentService.createTournament(Mockito.any(Tournament.class))).thenReturn(createdTournament);
//
//        mockMvc.perform(post("/api/tournaments/create")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"name\":\"Tournament A\",\"isActive\":true}"))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id").value(1))
//                .andExpect(jsonPath("$.name").value("Tournament A"))
//                .andExpect(jsonPath("$.isActive").value(true));
//    }
//
//    @Test
//    void testEnterTournament() throws Exception {
//        TournamentParticipant participant = new TournamentParticipant();
//        TournamentBracket bracket = new TournamentBracket(1L, "Bracket 1");
//        participant.setTournamentBracket(bracket);
//
//        List<GroupLeaderboardDTO> leaderboard = List.of(new GroupLeaderboardDTO("Group 1", 100));
//        Mockito.when(tournamentService.joinTournament(1L, 1L)).thenReturn(participant);
//        Mockito.when(tournamentBracketService.getGroupLeaderboardByBracket(bracket)).thenReturn(leaderboard);
//
//        mockMvc.perform(post("/api/tournaments/1/enter")
//                        .param("userId", "1"))
//                .andExpect(status().isOk())
//                .andExpect(content().json("[{\"groupName\":\"Group 1\",\"points\":100}]"));
//    }
//
//    @Test
//    void testGetBracketsWithoutIndex() throws Exception {
//        List<TournamentBracket> brackets = List.of(
//                new TournamentBracket(1L, "Bracket 1"),
//                new TournamentBracket(2L, "Bracket 2")
//        );
//        Mockito.when(tournamentBracketService.getBrackets(1L)).thenReturn(brackets);
//
//        mockMvc.perform(get("/api/tournaments/1/brackets"))
//                .andExpect(status().isOk())
//                .andExpect(content().json("[{\"id\":1,\"name\":\"Bracket 1\"}," +
//                        "{\"id\":2,\"name\":\"Bracket 2\"}]"));
//    }
//
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
