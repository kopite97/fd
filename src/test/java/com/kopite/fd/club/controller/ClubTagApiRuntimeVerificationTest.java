package com.kopite.fd.club.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.kopite.fd.club.application.command.SaveClubCommand;
import com.kopite.fd.club.application.result.ClubResult;
import com.kopite.fd.club.application.service.SaveClubService;
import com.kopite.fd.club.domain.model.ClubTag;
import com.kopite.fd.club.infrastructure.entity.ClubTagJpaEntity;
import com.kopite.fd.club.infrastructure.repository.ClubTagJpaRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ClubTagApiRuntimeVerificationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SaveClubService saveClubService;

    @Autowired
    private ClubTagJpaRepository clubTagJpaRepository;

    @Test
    void shouldExposeClubTagApisAndOpenApiDocumentThroughRunningApplicationContext() throws Exception {
        String uniqueCode = "TA" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        ClubResult savedClub = saveClubService.saveClub(new SaveClubCommand(
                null,
                "Runtime API Tag Club " + uniqueCode,
                "TA" + uniqueCode.substring(uniqueCode.length() - 4),
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
        LocalDateTime now = LocalDateTime.now();
        clubTagJpaRepository.save(ClubTagJpaEntity.fromDomain(new ClubTag(
                null, savedClub.id(), "Runtime API tag", "STYLE", 1, true, now, now
        )));

        mockMvc.perform(get("/api/clubs/{clubId}/tags", savedClub.id()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tags[0].clubId").value(savedClub.id()))
                .andExpect(jsonPath("$.tags[0].tagName").value("Runtime API tag"));

        mockMvc.perform(get("/api/club-tags"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tags[?(@.clubId == %s)]".formatted(savedClub.id())).exists());

        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("/api/clubs/{clubId}/tags")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("/api/club-tags")));
    }
}
