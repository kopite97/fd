package com.kopite.fd.admin.dataimport.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.kopite.fd.admin.dataimport.application.command.SyncFootballDnaDataCommand;
import com.kopite.fd.admin.dataimport.application.result.ImportFootballDnaDataResult;
import com.kopite.fd.admin.dataimport.application.result.SyncFootballDnaDataResult;
import com.kopite.fd.admin.dataimport.application.service.AdminSyncAccessService;
import com.kopite.fd.admin.dataimport.application.service.ImportFootballDnaDataService;
import com.kopite.fd.admin.dataimport.application.service.SyncFootballDnaDataService;
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

    @MockBean
    private SyncFootballDnaDataService syncFootballDnaDataService;

    @MockBean
    private AdminSyncAccessService adminSyncAccessService;

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

    @Test
    void shouldRejectSyncWhenTemporaryAdminAccessIsMissing() throws Exception {
        when(adminSyncAccessService.isAllowed(null)).thenReturn(false);

        mockMvc.perform(post("/api/admin/football-dna-data/sync")
                        .contentType("application/json")
                        .content("""
                                {
                                  "dataVersion": "club-v1"
                                }
                                """))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldRejectSyncWhenTemporaryAdminAccessIsInvalid() throws Exception {
        when(adminSyncAccessService.isAllowed("invalid")).thenReturn(false);

        mockMvc.perform(post("/api/admin/football-dna-data/sync")
                        .header(AdminSyncAccessService.ADMIN_SYNC_TOKEN_HEADER, "invalid")
                        .contentType("application/json")
                        .content("""
                                {
                                  "dataVersion": "club-v1"
                                }
                                """))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldDelegateSyncWhenTemporaryAdminAccessIsValid() throws Exception {
        when(adminSyncAccessService.isAllowed("valid-token")).thenReturn(true);
        when(syncFootballDnaDataService.sync(new SyncFootballDnaDataCommand("club-v1")))
                .thenReturn(SyncFootballDnaDataResult.succeeded(
                        "club-v1",
                        List.of("clubs", "club-tags", "club-status", "emotional-dna", "playstyle-dna"),
                        List.of(
                                new SyncFootballDnaDataResult.TableSyncResult("clubs", 20, 0, 0, 0, 0),
                                new SyncFootballDnaDataResult.TableSyncResult("club_tags", 80, 0, 0, 0, 0),
                                new SyncFootballDnaDataResult.TableSyncResult("club_dna_scores", 280, 0, 0, 0, 0)
                        )
                ));

        mockMvc.perform(post("/api/admin/football-dna-data/sync")
                        .header(AdminSyncAccessService.ADMIN_SYNC_TOKEN_HEADER, "valid-token")
                        .contentType("application/json")
                        .content("""
                                {
                                  "dataVersion": "club-v1"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.dataVersion").value("club-v1"))
                .andExpect(jsonPath("$.tableResults[0].tableName").value("clubs"))
                .andExpect(jsonPath("$.tableResults[2].insertedCount").value(280));
    }
}
