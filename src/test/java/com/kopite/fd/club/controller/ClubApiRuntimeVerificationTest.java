package com.kopite.fd.club.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
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
class ClubApiRuntimeVerificationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void shouldExposeClubApisAndOpenApiDocumentThroughRunningApplicationContext() throws Exception {
        String suffix = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Long activeClubId = insertClub("APIA" + suffix, true, false);
        Long inactiveClubId = insertClub("APII" + suffix, false, false);
        Long deletedClubId = insertClub("APID" + suffix, true, true);

        mockMvc.perform(get("/api/clubs").param("page", "0").param("size", "100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clubs[?(@.clubId == %s)]".formatted(activeClubId)).exists())
                .andExpect(jsonPath("$.clubs[?(@.clubId == %s)]".formatted(inactiveClubId)).doesNotExist())
                .andExpect(jsonPath("$.clubs[?(@.clubId == %s)]".formatted(deletedClubId)).doesNotExist())
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(100));

        mockMvc.perform(get("/api/clubs/{clubId}", activeClubId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clubId").value(activeClubId))
                .andExpect(jsonPath("$.competitionTier").value("CHALLENGER"))
                .andExpect(jsonPath("$.beginnerAccessibility").value(3.25));

        mockMvc.perform(get("/api/clubs/{clubId}", inactiveClubId))
                .andExpect(status().isNotFound());

        mockMvc.perform(get("/api/clubs/{clubId}", deletedClubId))
                .andExpect(status().isNotFound());

        mockMvc.perform(get("/api/clubs/{clubId}", 999999999L))
                .andExpect(status().isNotFound());

        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString("/api/clubs")))
                .andExpect(content().string(Matchers.containsString("/api/clubs/{clubId}")));
    }

    private Long insertClub(String code, boolean active, boolean deleted) {
        jdbcTemplate.update("""
                        insert into clubs (
                            name,
                            short_name,
                            code,
                            league,
                            country,
                            competition_tier,
                            trend_direction,
                            beginner_accessibility,
                            is_active,
                            logo_url,
                            primary_color,
                            secondary_color,
                            created_at,
                            updated_at,
                            is_deleted
                        ) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, now(), now(), ?)
                        """,
                "Runtime API Club " + code,
                code.substring(0, 6),
                code,
                "EPL",
                "England",
                "CHALLENGER",
                "STABLE",
                new BigDecimal("3.25"),
                active,
                null,
                "#111111",
                "#FFFFFF",
                deleted
        );
        return jdbcTemplate.queryForObject("select id from clubs where code = ?", Long.class, code);
    }
}
