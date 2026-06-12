package com.kopite.fd.admin.dataimport.domain.repository;

import com.kopite.fd.admin.dataimport.application.result.SyncFootballDnaDataResult.TableSyncResult;
import com.kopite.fd.admin.dataimport.domain.model.DnaDefinitionSyncRef;
import com.kopite.fd.admin.dataimport.domain.model.FootballDnaDataSyncDataset;
import java.util.List;

public interface FootballDnaDataSyncRepository {

    List<DnaDefinitionSyncRef> findActiveDnaDefinitions();

    boolean existsClubDnaScoreDataVersion(String dataVersion);

    List<TableSyncResult> synchronize(FootballDnaDataSyncDataset dataset);
}
