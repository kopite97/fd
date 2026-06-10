package com.kopite.fd.admin.dataimport.domain.repository;

import com.kopite.fd.admin.dataimport.application.model.ImportTarget;
import java.util.List;

public interface FootballDnaImportTargetProvider {

    List<ImportTarget> getRequiredTargets();
}
