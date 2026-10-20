package com.auca.pocketwise.repository;

import com.auca.pocketwise.model.SavingsCircle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SavingsCircleRepository extends JpaRepository<SavingsCircle, UUID> {

    Optional<SavingsCircle> findByCircleNumber(int circleNumber);

    @Query("SELECT l FROM SavingsCircle l WHERE NOT EXISTS (SELECT p FROM Coach p WHERE p.savingsCircle = l) ORDER BY l.circleNumber")
    List<SavingsCircle> findFree();
}
