package com.kopite.fd.admin.dataimport.domain.repository;

import com.kopite.fd.admin.dataimport.application.model.AcquiredImportPayload;
import com.kopite.fd.admin.dataimport.application.model.ImportTarget;
import com.kopite.fd.admin.dataimport.domain.type.ImportSourceType;

public interface FootballDnaSourceAcquirer {

    boolean supports(ImportSourceType sourceType);

    AcquiredImportPayload acquire(ImportTarget target);
}
