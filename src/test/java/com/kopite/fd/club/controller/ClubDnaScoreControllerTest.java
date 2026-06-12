package com.kopite.fd.club.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.kopite.fd.club.application.result.ClubDnaScoreResult;
import com.kopite.fd.club.application.service.GetClubDnaScoresByClubIdService;
import com.kopite.fd.club.application.service.GetClubDnaScoresByDataVersionService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ClubDnaScoreController.class)
class ClubDnaScoreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GetClubDnaScoresByClubIdService getClubDnaScoresByClubIdService;

    @MockBean
    private GetClubDnaScoresByDataVersionService getClubDnaScoresByDataVersionService;

    @Test
    void shouldGetClubDnaScoresByClubId() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        when(getClubDnaScoresByClubIdService.getClubDnaScoresByClubId(any())).thenReturn(List.of(new ClubDnaScoreResult(
                10L,
                1L,
                100L,
                "EMOTIONAL",
                "fan_culture",
                "Fan Culture",
                1,
                new BigDecimal("4.50"),
                true,
                "club-v1",
                now,
                now
        )));

        mockMvc.perform(get("/api/clubs/1/dna-scores").param("dataVersion", "club-v1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.scores[0].scoreId").value(10))
                .andExpect(jsonPath("$.scores[0].clubId").value(1))
                .andExpect(jsonPath("$.scores[0].dnaDefinitionId").value(100))
                .andExpect(jsonPath("$.scores[0].dnaCategory").value("EMOTIONAL"))
                .andExpect(jsonPath("$.scores[0].dnaKey").value("fan_culture"))
                .andExpect(jsonPath("$.scores[0].score").value(4.50))
                .andExpect(jsonPath("$.scores[0].core").value(true))
                .andExpect(jsonPath("$.scores[0].dataVersion").value("club-v1"));
    }

    @Test
    void shouldGetClubDnaScoresByDataVersion() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        when(getClubDnaScoresByDataVersionService.getClubDnaScoresByDataVersion(any())).thenReturn(List.of(new ClubDnaScoreResult(
                10L,
                1L,
                100L,
                "EMOTIONAL",
                "fan_culture",
                "Fan Culture",
                1,
                new BigDecimal("4.50"),
                true,
                "club-v1",
                now,
                now
        )));

        mockMvc.perform(get("/api/club-dna-scores").param("dataVersion", "club-v1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.scores[0].clubId").value(1))
                .andExpect(jsonPath("$.scores[0].dnaKey").value("fan_culture"));
    }

    @Test
    void shouldReturnBadRequestWhenDataVersionIsMissing() throws Exception {
        mockMvc.perform(get("/api/club-dna-scores"))
                .andExpect(status().isBadRequest());
    }
}
