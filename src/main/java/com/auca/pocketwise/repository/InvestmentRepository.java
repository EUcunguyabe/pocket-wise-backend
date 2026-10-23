package com.auca.pocketwise.repository;

import com.auca.pocketwise.model.Investment;
import com.auca.pocketwise.model.SavingsGoal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface InvestmentRepository extends JpaRepository<Investment, UUID> {

    @Query("SELECT i FROM Investment i WHERE i.savingsGoal.saver.username = :username ORDER BY i.investedDate DESC")
    List<Investment> findBySaverUsername(@Param("username") String username);

    boolean existsBySavingsGoal(SavingsGoal savingsGoal);
}
