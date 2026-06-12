package com.kopite.fd.club.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.kopite.fd.club.application.result.ClubTagResult;
import com.kopite.fd.club.application.service.GetAllClubTagsService;
import com.kopite.fd.club.application.service.GetClubTagsByClubIdService;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ClubTagController.class)
class ClubTagControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GetClubTagsByClubIdService getClubTagsByClubIdService;

    @MockBean
    private GetAllClubTagsService getAllClubTagsService;

    @Test
    void shouldGetClubTagsByClubId() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        when(getClubTagsByClubIdService.getClubTagsByClubId(any())).thenReturn(List.of(new ClubTagResult(
                10L,
                1L,
                "High pressing",
                "STYLE",
                1,
                true,
                now,
                now
        )));

        mockMvc.perform(get("/api/clubs/1/tags"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tags[0].tagId").value(10))
                .andExpect(jsonPath("$.tags[0].clubId").value(1))
                .andExpect(jsonPath("$.tags[0].tagName").value("High pressing"))
                .andExpect(jsonPath("$.tags[0].tagType").value("STYLE"))
                .andExpect(jsonPath("$.tags[0].displayOrder").value(1));
    }

    @Test
    void shouldGetAllClubTags() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        when(getAllClubTagsService.getAllClubTags()).thenReturn(List.of(
                new ClubTagResult(10L, 1L, "High pressing", "STYLE", 1, true, now, now),
                new ClubTagResult(20L, 2L, "European nights", "HISTORY", 1, true, now, now)
        ));

        mockMvc.perform(get("/api/club-tags"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tags[0].clubId").value(1))
                .andExpect(jsonPath("$.tags[1].clubId").value(2));
    }
}
