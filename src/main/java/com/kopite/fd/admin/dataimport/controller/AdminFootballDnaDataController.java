package com.kopite.fd.admin.dataimport.controller;

import com.kopite.fd.admin.dataimport.application.result.ImportFootballDnaDataResult;
import com.kopite.fd.admin.dataimport.application.service.ImportFootballDnaDataService;
import com.kopite.fd.admin.dataimport.dto.response.ImportFootballDnaDataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/football-dna-data")
@RequiredArgsConstructor
public class AdminFootballDnaDataController {

    private final ImportFootballDnaDataService importFootballDnaDataService;

    @PostMapping("/import")
    public ImportFootballDnaDataResponse importFootballDnaData() {
        ImportFootballDnaDataResult result = importFootballDnaDataService.importData();
        return ImportFootballDnaDataResponse.from(result);
    }
}
