package com.kopite.fd.admin.dataimport.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.kopite.fd.admin.dataimport.application.result.ImportFootballDnaDataResult;
import com.kopite.fd.admin.dataimport.application.service.ImportFootballDnaDataService;
import com.kopite.fd.admin.dataimport.domain.type.ImportSourceType;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AdminFootballDnaDataController.class)
class AdminFootballDnaDataControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ImportFootballDnaDataService importFootballDnaDataService;

    @Test
    void shouldDelegateManualImport() throws Exception {
        when(importFootballDnaDataService.importData()).thenReturn(new ImportFootballDnaDataResult(
                true,
                "src/main/resources/data/import.md",
                256L,
                2,
                LocalDateTime.of(2026, 6, 10, 10, 0),
                List.of(
                        new ImportFootballDnaDataResult.TargetImportResult(
                                "clubs",
                                ImportSourceType.CSV,
                                "https://example.com/clubs.csv",
                                ImportFootballDnaDataResult.TargetImportStatus.SUCCEEDED,
                                20,
                                null
                        )
                )
        ));

        mockMvc.perform(post("/api/admin/football-dna-data/import"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.artifactPath").value("src/main/resources/data/import.md"))
                .andExpect(jsonPath("$.byteSize").value(256))
                .andExpect(jsonPath("$.processedTargetCount").value(2))
                .andExpect(jsonPath("$.targetResults[0].targetKey").value("clubs"))
                .andExpect(jsonPath("$.targetResults[0].status").value("SUCCEEDED"));
    }
}
