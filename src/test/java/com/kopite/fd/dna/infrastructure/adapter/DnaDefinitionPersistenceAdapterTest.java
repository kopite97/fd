package com.kopite.fd.dna.infrastructure.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.kopite.fd.dna.domain.model.DnaDefinition;
import com.kopite.fd.dna.infrastructure.entity.DnaDefinitionJpaEntity;
import com.kopite.fd.dna.infrastructure.repository.DnaDefinitionJpaRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DnaDefinitionPersistenceAdapterTest {

    @Mock
    private DnaDefinitionJpaRepository dnaDefinitionJpaRepository;

    @InjectMocks
    private DnaDefinitionPersistenceAdapter dnaDefinitionPersistenceAdapter;

    @Test
    void shouldFindActiveDnaDefinitionsUsingNonDeletedScope() {
        DnaDefinitionJpaEntity dnaDefinitionJpaEntity = org.mockito.Mockito.mock(DnaDefinitionJpaEntity.class);
        DnaDefinition dnaDefinition = new DnaDefinition(1L, "EMOTIONAL", "club_prestige", "Prestige", null, 1);
        when(dnaDefinitionJpaRepository.findByActiveTrueAndIsDeletedFalseOrderByDisplayOrderAsc())
                .thenReturn(List.of(dnaDefinitionJpaEntity));
        when(dnaDefinitionJpaEntity.toDomain()).thenReturn(dnaDefinition);

        List<DnaDefinition> result = dnaDefinitionPersistenceAdapter.findActiveDefinitions();

        assertThat(result).containsExactly(dnaDefinition);
        verify(dnaDefinitionJpaRepository).findByActiveTrueAndIsDeletedFalseOrderByDisplayOrderAsc();
    }
}
