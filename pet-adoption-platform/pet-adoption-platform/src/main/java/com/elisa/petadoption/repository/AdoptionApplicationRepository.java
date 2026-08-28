package com.elisa.petadoption.repository;

import com.elisa.petadoption.entity.AdoptionApplication;
import com.elisa.petadoption.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdoptionApplicationRepository extends JpaRepository<AdoptionApplication, Long> {
    List<AdoptionApplication> findByApplicantOrderByCreatedAtDesc(AppUser applicant);
}
