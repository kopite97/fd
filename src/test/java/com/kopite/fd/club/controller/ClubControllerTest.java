package com.kopite.fd.club.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.kopite.fd.club.application.result.ClubPageResult;
import com.kopite.fd.club.application.result.ClubResult;
import com.kopite.fd.club.application.service.GetPublicClubByIdService;
import com.kopite.fd.club.application.service.GetPublicClubsService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ClubController.class)
class ClubControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GetPublicClubsService getPublicClubsService;

    @MockitoBean
    private GetPublicClubByIdService getPublicClubByIdService;

    @Test
    void shouldGetPublicClubs() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        when(getPublicClubsService.getPublicClubs(any())).thenReturn(new ClubPageResult(List.of(new ClubResult(
                1L,
                "Arsenal",
                "AFC",
                "ARS",
                "EPL",
                "England",
                "TITLE_CONTENDER",
                "RISING",
                new BigDecimal("4.50"),
                true,
                "https://example.com/ars.png",
                "#EF0107",
                "#FFFFFF",
                now,
                now
        )), 0, 20, 1L, 1));

        mockMvc.perform(get("/api/clubs").param("page", "0").param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clubs[0].clubId").value(1))
                .andExpect(jsonPath("$.clubs[0].code").value("ARS"))
                .andExpect(jsonPath("$.clubs[0].name").value("Arsenal"))
                .andExpect(jsonPath("$.clubs[0].competitionTier").doesNotExist())
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(20))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1));
    }

    @Test
    void shouldGetPublicClubDetail() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        when(getPublicClubByIdService.getPublicClubById(any())).thenReturn(Optional.of(new ClubResult(
                1L,
                "Arsenal",
                "AFC",
                "ARS",
                "EPL",
                "England",
                "TITLE_CONTENDER",
                "RISING",
                new BigDecimal("4.50"),
                true,
                "https://example.com/ars.png",
                "#EF0107",
                "#FFFFFF",
                now,
                now
        )));

        mockMvc.perform(get("/api/clubs/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clubId").value(1))
                .andExpect(jsonPath("$.code").value("ARS"))
                .andExpect(jsonPath("$.competitionTier").value("TITLE_CONTENDER"))
                .andExpect(jsonPath("$.trendDirection").value("RISING"))
                .andExpect(jsonPath("$.beginnerAccessibility").value(4.50))
                .andExpect(jsonPath("$.active").doesNotExist())
                .andExpect(jsonPath("$.createdAt").doesNotExist())
                .andExpect(jsonPath("$.deleted").doesNotExist());
    }

    @Test
    void shouldReturnNotFoundWhenClubIsNotPubliclyVisible() throws Exception {
        when(getPublicClubByIdService.getPublicClubById(any())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/clubs/1"))
                .andExpect(status().isNotFound());
    }
}
