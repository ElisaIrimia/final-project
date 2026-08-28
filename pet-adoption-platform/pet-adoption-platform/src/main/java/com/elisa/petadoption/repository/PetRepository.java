package com.elisa.petadoption.repository;

import com.elisa.petadoption.entity.Pet;
import com.elisa.petadoption.entity.PetStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PetRepository extends JpaRepository<Pet, Long> {
    List<Pet> findByStatus(PetStatus status);

    @Query("""
            select p from Pet p
            where (:query is null or lower(p.name) like lower(concat('%', :query, '%'))
                or lower(p.species) like lower(concat('%', :query, '%'))
                or lower(p.breed) like lower(concat('%', :query, '%')))
            and (:status is null or p.status = :status)
            order by p.name asc
            """)
    List<Pet> search(@Param("query") String query, @Param("status") PetStatus status);
}
