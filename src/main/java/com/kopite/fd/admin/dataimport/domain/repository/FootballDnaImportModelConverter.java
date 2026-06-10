package com.kopite.fd.admin.dataimport.domain.repository;

import com.kopite.fd.admin.dataimport.application.model.ImportedTargetData;
import com.kopite.fd.admin.dataimport.application.model.ParsedImportPayload;
import com.kopite.fd.admin.dataimport.domain.type.ImportSourceType;

public interface FootballDnaImportModelConverter {

    boolean supports(ImportSourceType sourceType);

    ImportedTargetData convert(ParsedImportPayload payload);
}
