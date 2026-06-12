package com.kopite.fd.admin.dataimport.controller;

import com.kopite.fd.admin.dataimport.application.result.ImportFootballDnaDataResult;
import com.kopite.fd.admin.dataimport.application.result.SyncFootballDnaDataResult;
import com.kopite.fd.admin.dataimport.application.service.AdminSyncAccessService;
import com.kopite.fd.admin.dataimport.application.service.ImportFootballDnaDataService;
import com.kopite.fd.admin.dataimport.application.service.SyncFootballDnaDataService;
import com.kopite.fd.admin.dataimport.dto.request.SyncFootballDnaDataRequest;
import com.kopite.fd.admin.dataimport.dto.response.ImportFootballDnaDataResponse;
import com.kopite.fd.admin.dataimport.dto.response.SyncFootballDnaDataResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Tag(name = "Admin Football DNA Data", description = "Administrator-only Football DNA Data operations")
@RestController
@RequestMapping("/api/admin/football-dna-data")
@RequiredArgsConstructor
public class AdminFootballDnaDataController {

    private final ImportFootballDnaDataService importFootballDnaDataService;
    private final SyncFootballDnaDataService syncFootballDnaDataService;
    private final AdminSyncAccessService adminSyncAccessService;

    @Operation(summary = "Import Football DNA Data", description = "Imports configured Football DNA source data.")
    @PostMapping("/import")
    public ImportFootballDnaDataResponse importFootballDnaData() {
        ImportFootballDnaDataResult result = importFootballDnaDataService.importData();
        return ImportFootballDnaDataResponse.from(result);
    }

    @Operation(
            summary = "Synchronize Football DNA Data",
            description = "Synchronizes normalized club, club tag, and club DNA score data from configured sources."
    )
    @PostMapping("/sync")
    public SyncFootballDnaDataResponse syncFootballDnaData(
            @Parameter(description = "Temporary administrator sync token")
            @RequestHeader(value = AdminSyncAccessService.ADMIN_SYNC_TOKEN_HEADER, required = false) String adminToken,
            @Valid @RequestBody SyncFootballDnaDataRequest request
    ) {
        if (!adminSyncAccessService.isAllowed(adminToken)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Admin sync access is not allowed.");
        }

        SyncFootballDnaDataResult result = syncFootballDnaDataService.sync(request.toCommand());
        return SyncFootballDnaDataResponse.from(result);
    }
}
