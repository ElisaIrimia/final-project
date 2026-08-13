package com.elisa.petadoption.service;

import com.elisa.petadoption.entity.PetStatus;
import com.elisa.petadoption.repository.PetRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class PetServiceTest {
    @Mock
    private PetRepository petRepository;

    @InjectMocks
    private PetService petService;

    @Test
    void searchNormalizesBlankQueryBeforeCallingRepository() {
        when(petRepository.search(null, PetStatus.AVAILABLE)).thenReturn(List.of());

        var result = petService.search("   ", PetStatus.AVAILABLE);

        assertThat(result).isEmpty();
        verify(petRepository).search(null, PetStatus.AVAILABLE);
    }
}
