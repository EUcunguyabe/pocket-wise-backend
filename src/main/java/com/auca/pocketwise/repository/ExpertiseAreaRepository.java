package com.auca.pocketwise.repository;

import com.auca.pocketwise.model.ExpertiseArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ExpertiseAreaRepository extends JpaRepository<ExpertiseArea, UUID> {

    @Query("SELECT e FROM ExpertiseArea e WHERE e.coaches IS EMPTY")
    List<ExpertiseArea> findUnused();
}
