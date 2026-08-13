package com.elisa.petadoption.repository;

import com.elisa.petadoption.entity.Shelter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShelterRepository extends JpaRepository<Shelter, Long> {
}
