package com.kopite.fd.club.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.kopite.fd.club.application.command.SaveClubCommand;
import com.kopite.fd.club.application.result.ClubResult;
import com.kopite.fd.club.application.service.SaveClubService;
import com.kopite.fd.club.domain.model.ClubDnaScore;
import com.kopite.fd.club.infrastructure.entity.ClubDnaScoreJpaEntity;
import com.kopite.fd.club.infrastructure.repository.ClubDnaScoreJpaRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ClubDnaScoreApiRuntimeVerificationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SaveClubService saveClubService;

    @Autowired
    private ClubDnaScoreJpaRepository clubDnaScoreJpaRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void shouldExposeClubDnaScoreApisAndOpenApiDocumentThroughRunningApplicationContext() throws Exception {
        String suffix = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String uniqueCode = "DA" + suffix;
        ClubResult savedClub = saveClubService.saveClub(new SaveClubCommand(
                null,
                "Runtime API DNA Club " + uniqueCode,
                "DA" + uniqueCode.substring(uniqueCode.length() - 4),
                uniqueCode,
                "EPL",
                "England",
                "CHALLENGER",
                "STABLE",
                new BigDecimal("2.75"),
                true,
                null,
                null,
                null
        ));
        Long dnaDefinitionId = insertDnaDefinition("EMOTIONAL", "runtime_api_dna_" + suffix, "Runtime API DNA", 1);
        String dataVersion = "club-" + suffix.toLowerCase();
        LocalDateTime now = LocalDateTime.now();
        clubDnaScoreJpaRepository.save(ClubDnaScoreJpaEntity.fromDomain(new ClubDnaScore(
                null,
                savedClub.id(),
                dnaDefinitionId,
                new BigDecimal("4.75"),
                true,
                dataVersion,
                null,
                null,
                null,
                null,
                now,
                now
        )));

        mockMvc.perform(get("/api/clubs/{clubId}/dna-scores", savedClub.id())
                        .param("dataVersion", dataVersion))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.scores[0].clubId").value(savedClub.id()))
                .andExpect(jsonPath("$.scores[0].dnaDefinitionId").value(dnaDefinitionId))
                .andExpect(jsonPath("$.scores[0].dnaCategory").value("EMOTIONAL"))
                .andExpect(jsonPath("$.scores[0].dnaKey").value("runtime_api_dna_" + suffix))
                .andExpect(jsonPath("$.scores[0].score").value(4.75))
                .andExpect(jsonPath("$.scores[0].core").value(true))
                .andExpect(jsonPath("$.scores[0].dataVersion").value(dataVersion));

        mockMvc.perform(get("/api/club-dna-scores").param("dataVersion", dataVersion))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.scores[?(@.clubId == %s)]".formatted(savedClub.id())).exists());

        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString("/api/clubs/{clubId}/dna-scores")))
                .andExpect(content().string(Matchers.containsString("/api/club-dna-scores")));
    }

    private Long insertDnaDefinition(String dnaCategory, String dnaKey, String displayName, Integer displayOrder) {
        Long dnaDefinitionId = Math.abs(UUID.randomUUID().getMostSignificantBits() % 1_000_000_000L) + 300_000_000L;
        jdbcTemplate.update("""
                        INSERT INTO dna_definitions
                            (id, dna_category, dna_key, display_name, description, display_order, is_active, created_at, updated_at, is_deleted)
                        VALUES
                            (?, ?, ?, ?, null, ?, true, now(), now(), false)
                        """,
                dnaDefinitionId,
                dnaCategory,
                dnaKey,
                displayName,
                displayOrder
        );
        return dnaDefinitionId;
    }
}
