package com.kopite.fd.admin.dataimport.domain.repository;

import com.kopite.fd.admin.dataimport.application.model.AcquiredImportPayload;
import com.kopite.fd.admin.dataimport.application.model.ParsedImportPayload;
import com.kopite.fd.admin.dataimport.domain.type.ImportSourceType;

public interface FootballDnaPayloadParser {

    boolean supports(ImportSourceType sourceType);

    ParsedImportPayload parse(AcquiredImportPayload payload);
}
