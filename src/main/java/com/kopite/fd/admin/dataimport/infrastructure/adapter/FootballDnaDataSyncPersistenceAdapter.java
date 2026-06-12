package com.kopite.fd.admin.dataimport.infrastructure.adapter;

import com.kopite.fd.admin.dataimport.application.result.SyncFootballDnaDataResult.TableSyncResult;
import com.kopite.fd.admin.dataimport.domain.model.DnaDefinitionSyncRef;
import com.kopite.fd.admin.dataimport.domain.model.FootballDnaDataSyncDataset;
import com.kopite.fd.admin.dataimport.domain.model.SyncClubDnaScoreRecord;
import com.kopite.fd.admin.dataimport.domain.model.SyncClubRecord;
import com.kopite.fd.admin.dataimport.domain.model.SyncClubTagRecord;
import com.kopite.fd.admin.dataimport.domain.repository.FootballDnaDataSyncRepository;
import com.kopite.fd.club.infrastructure.entity.ClubDnaScoreJpaEntity;
import com.kopite.fd.club.infrastructure.entity.ClubJpaEntity;
import com.kopite.fd.club.infrastructure.entity.ClubTagJpaEntity;
import com.kopite.fd.club.infrastructure.repository.ClubDnaScoreJpaRepository;
import com.kopite.fd.club.infrastructure.repository.ClubJpaRepository;
import com.kopite.fd.club.infrastructure.repository.ClubTagJpaRepository;
import com.kopite.fd.dna.infrastructure.entity.DnaDefinitionJpaEntity;
import com.kopite.fd.dna.infrastructure.repository.DnaDefinitionJpaRepository;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class FootballDnaDataSyncPersistenceAdapter implements FootballDnaDataSyncRepository {

    private final ClubJpaRepository clubJpaRepository;
    private final ClubTagJpaRepository clubTagJpaRepository;
    private final ClubDnaScoreJpaRepository clubDnaScoreJpaRepository;
    private final DnaDefinitionJpaRepository dnaDefinitionJpaRepository;

    @Override
    public List<DnaDefinitionSyncRef> findActiveDnaDefinitions() {
        return dnaDefinitionJpaRepository.findByActiveTrueAndIsDeletedFalseOrderByDisplayOrderAsc().stream()
                .map(entity -> new DnaDefinitionSyncRef(entity.getId(), entity.getDnaCategory(), entity.getDnaKey()))
                .toList();
    }

    @Override
    public boolean existsClubDnaScoreDataVersion(String dataVersion) {
        return clubDnaScoreJpaRepository.existsByDataVersion(dataVersion);
    }

    @Override
    @Transactional
    public List<TableSyncResult> synchronize(FootballDnaDataSyncDataset dataset) {
        ClubSyncState clubSyncState = synchronizeClubs(dataset.clubs());
        TableSyncResult tagResult = synchronizeClubTags(dataset.clubTags(), clubSyncState.clubsByCode());
        TableSyncResult dnaScoreResult = insertClubDnaScores(dataset.clubDnaScores(), clubSyncState.clubsByCode());

        return List.of(clubSyncState.result(), tagResult, dnaScoreResult);
    }

    private ClubSyncState synchronizeClubs(List<SyncClubRecord> sourceClubs) {
        Set<String> sourceCodes = sourceClubs.stream()
                .map(SyncClubRecord::code)
                .collect(Collectors.toSet());
        Map<String, ClubJpaEntity> existingByCode = clubJpaRepository.findAll().stream()
                .collect(Collectors.toMap(ClubJpaEntity::getCode, Function.identity()));

        List<ClubJpaEntity> entitiesToSave = new ArrayList<>();
        int insertedCount = 0;
        int updatedCount = 0;
        int softDeletedCount = 0;

        for (SyncClubRecord sourceClub : sourceClubs) {
            ClubJpaEntity existingClub = existingByCode.get(sourceClub.code());
            if (existingClub == null) {
                ClubJpaEntity newClub = ClubJpaEntity.createForSync(
                        sourceClub.name(),
                        sourceClub.shortName(),
                        sourceClub.code(),
                        sourceClub.league(),
                        sourceClub.country(),
                        sourceClub.competitionTier(),
                        sourceClub.trendDirection(),
                        sourceClub.beginnerAccessibility(),
                        sourceClub.active()
                );
                entitiesToSave.add(newClub);
                existingByCode.put(sourceClub.code(), newClub);
                insertedCount++;
            } else {
                existingClub.updateFromSync(
                        sourceClub.name(),
                        sourceClub.shortName(),
                        sourceClub.league(),
                        sourceClub.country(),
                        sourceClub.competitionTier(),
                        sourceClub.trendDirection(),
                        sourceClub.beginnerAccessibility(),
                        sourceClub.active()
                );
                entitiesToSave.add(existingClub);
                updatedCount++;
            }
        }

        for (ClubJpaEntity existingClub : existingByCode.values()) {
            if (!sourceCodes.contains(existingClub.getCode()) && !existingClub.isDeleted()) {
                existingClub.markSoftDeleted();
                entitiesToSave.add(existingClub);
                softDeletedCount++;
            }
        }

        List<ClubJpaEntity> savedClubs = clubJpaRepository.saveAll(entitiesToSave);
        Map<String, ClubJpaEntity> syncedClubsByCode = savedClubs.stream()
                .filter(club -> sourceCodes.contains(club.getCode()))
                .collect(Collectors.toMap(ClubJpaEntity::getCode, Function.identity(), (left, right) -> left));

        return new ClubSyncState(
                syncedClubsByCode,
                new TableSyncResult("clubs", insertedCount, updatedCount, 0, softDeletedCount, 0)
        );
    }

    private TableSyncResult synchronizeClubTags(
            List<SyncClubTagRecord> sourceTags,
            Map<String, ClubJpaEntity> clubsByCode
    ) {
        Collection<Long> synchronizedClubIds = clubsByCode.values().stream()
                .map(ClubJpaEntity::getId)
                .toList();
        List<ClubTagJpaEntity> existingTags = synchronizedClubIds.isEmpty()
                ? List.of()
                : clubTagJpaRepository.findByClubIdIn(synchronizedClubIds);

        Map<TagIdentity, ClubTagJpaEntity> existingByIdentity = new HashMap<>();
        for (ClubTagJpaEntity existingTag : existingTags) {
            existingByIdentity.putIfAbsent(new TagIdentity(existingTag.getClubId(), existingTag.getTagName()), existingTag);
        }

        Set<TagIdentity> sourceIdentities = new HashSet<>();
        List<ClubTagJpaEntity> tagsToSave = new ArrayList<>();
        int insertedCount = 0;
        int updatedCount = 0;
        int restoredCount = 0;

        for (SyncClubTagRecord sourceTag : sourceTags) {
            Long clubId = clubsByCode.get(sourceTag.clubCode()).getId();
            TagIdentity identity = new TagIdentity(clubId, sourceTag.tagName());
            sourceIdentities.add(identity);

            ClubTagJpaEntity existingTag = existingByIdentity.get(identity);
            if (existingTag == null) {
                tagsToSave.add(ClubTagJpaEntity.createForSync(
                        clubId,
                        sourceTag.tagName(),
                        sourceTag.tagType(),
                        sourceTag.displayOrder(),
                        sourceTag.active()
                ));
                insertedCount++;
            } else {
                boolean wasDeleted = existingTag.isDeleted();
                existingTag.updateFromSync(sourceTag.tagType(), sourceTag.displayOrder(), sourceTag.active());
                tagsToSave.add(existingTag);
                if (wasDeleted) {
                    restoredCount++;
                } else {
                    updatedCount++;
                }
            }
        }

        int softDeletedCount = 0;
        for (ClubTagJpaEntity existingTag : existingTags) {
            TagIdentity identity = new TagIdentity(existingTag.getClubId(), existingTag.getTagName());
            if (!sourceIdentities.contains(identity) && !existingTag.isDeleted()) {
                existingTag.markSoftDeleted();
                tagsToSave.add(existingTag);
                softDeletedCount++;
            }
        }

        clubTagJpaRepository.saveAll(tagsToSave);

        return new TableSyncResult("club_tags", insertedCount, updatedCount, restoredCount, softDeletedCount, 0);
    }

    private TableSyncResult insertClubDnaScores(
            List<SyncClubDnaScoreRecord> sourceScores,
            Map<String, ClubJpaEntity> clubsByCode
    ) {
        Map<DnaDefinitionIdentity, Long> dnaDefinitionIdsByIdentity =
                dnaDefinitionJpaRepository.findByActiveTrueAndIsDeletedFalseOrderByDisplayOrderAsc().stream()
                        .collect(Collectors.toMap(
                                entity -> new DnaDefinitionIdentity(entity.getDnaCategory(), entity.getDnaKey()),
                                DnaDefinitionJpaEntity::getId
                        ));

        List<ClubDnaScoreJpaEntity> scoreEntities = sourceScores.stream()
                .map(sourceScore -> ClubDnaScoreJpaEntity.createForSync(
                        clubsByCode.get(sourceScore.clubCode()).getId(),
                        dnaDefinitionIdsByIdentity.get(new DnaDefinitionIdentity(
                                sourceScore.dnaCategory(),
                                sourceScore.dnaKey()
                        )),
                        sourceScore.score(),
                        sourceScore.core(),
                        sourceScore.dataVersion()
                ))
                .toList();

        clubDnaScoreJpaRepository.saveAll(scoreEntities);

        return new TableSyncResult("club_dna_scores", scoreEntities.size(), 0, 0, 0, 0);
    }

    private record ClubSyncState(
            Map<String, ClubJpaEntity> clubsByCode,
            TableSyncResult result
    ) {
    }

    private record TagIdentity(
            Long clubId,
            String tagName
    ) {
    }

    private record DnaDefinitionIdentity(
            String dnaCategory,
            String dnaKey
    ) {
    }
}
