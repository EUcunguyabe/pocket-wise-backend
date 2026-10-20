package com.auca.pocketwise.repository;

import com.auca.pocketwise.model.Coach;
import com.auca.pocketwise.model.SavingsGoal;
import com.auca.pocketwise.model.SavingsGoalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface SavingsGoalRepository extends JpaRepository<SavingsGoal, UUID> {

    List<SavingsGoal> findByStatusOrderByTargetDateAsc(SavingsGoalStatus status);

    List<SavingsGoal> findByTargetDateBetweenOrderByTargetDateAsc(LocalDate start, LocalDate end);

    boolean existsByCoachAndTargetDateAndStatusNot(Coach coach, LocalDate targetDate, SavingsGoalStatus status);

    long countByStatus(SavingsGoalStatus status);

    long countByTargetDate(LocalDate targetDate);

    @Query("SELECT g.status, COUNT(g) FROM SavingsGoal g GROUP BY g.status")
    List<Object[]> countGroupedByStatus();

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE SavingsGoal g SET g.status = :abandoned WHERE g.coach.id = :coachId AND g.targetDate = :date AND g.status <> :achieved")
    int abandonByCoachAndDate(@Param("coachId") UUID coachId,
                              @Param("date") LocalDate date,
                              @Param("abandoned") SavingsGoalStatus abandoned,
                              @Param("achieved") SavingsGoalStatus achieved);
}
