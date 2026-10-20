package com.auca.pocketwise.repository;

import com.auca.pocketwise.model.Coach;
import com.auca.pocketwise.model.SavingsCircle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface CoachRepository extends JpaRepository<Coach, UUID> {

    @Query("SELECT DISTINCT c FROM Coach c JOIN c.expertiseAreas e WHERE LOWER(e.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Coach> findByExpertiseAreaName(@Param("name") String name);

    boolean existsBySavingsCircle(SavingsCircle savingsCircle);
}
