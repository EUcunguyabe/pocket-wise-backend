package com.auca.pocketwise.repository;

import com.auca.pocketwise.model.Saver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface SaverRepository extends JpaRepository<Saver, UUID> {

    List<Saver> findByLastNameIgnoreCaseOrderByFirstNameAsc(String lastName);

    @Query("SELECT DISTINCT g.saver FROM SavingsGoal g WHERE g.coach.id = :coachId")
    List<Saver> findSaversOfCoach(@Param("coachId") UUID coachId);
}
