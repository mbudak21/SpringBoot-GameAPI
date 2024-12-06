import com.dreamgames.backendengineeringcasestudy.model.Tournament;
import com.dreamgames.backendengineeringcasestudy.repository.TournamentRepository;
import com.dreamgames.backendengineeringcasestudy.service.TournamentBracketService;
import com.dreamgames.backendengineeringcasestudy.service.TournamentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.*;
import com.dreamgames.backendengineeringcasestudy.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;




@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = com.dreamgames.backendengineeringcasestudy.BackendEngineeringCaseStudyApplication.class)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class TournamentControllerTest {

    private String getTournamentEndpoint = "/api/tournaments";
    private String createTournamentEndpoint = "/api/tournaments/create";


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TournamentRepository tournamentRepository;


    @BeforeEach
    void ensureClearDatabase() throws Exception {
        mockMvc.perform(get(getTournamentEndpoint))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void testCreateAndFetchTournament() throws Exception {
        // 1. Create a tournament using POST
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
//        String startTime = LocalDateTime.now().plusDays(1).toString().format(String.valueOf(formatter));
//        String endTime = LocalDateTime.now().plusDays(2).toString().format(String.valueOf(formatter));
//        String description = "Integration Test Tournament";
//
//        MvcResult createResult = mockMvc.perform(post("/api/tournaments/create")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"startTime\":\"" + startTime + "\",\"endTime\":\"" + endTime + "\",\"description\":\"" + description + "\"}"))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id").exists())
//                .andExpect(jsonPath("$.startTime").value(startTime))
//                .andExpect(jsonPath("$.endTime").value(endTime))
//                .andExpect(jsonPath("$.description").value(description))
//                .andReturn();
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
    }

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
