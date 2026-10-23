package com.auca.pocketwise.service;

import com.auca.pocketwise.model.Investment;
import com.auca.pocketwise.model.SavingsGoal;
import com.auca.pocketwise.model.SavingsGoalStatus;
import com.auca.pocketwise.repository.InvestmentRepository;
import com.auca.pocketwise.repository.SavingsGoalRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class InvestmentService {

    private final InvestmentRepository investmentRepository;
    private final SavingsGoalRepository savingsGoalRepository;

    public InvestmentService(InvestmentRepository investmentRepository, SavingsGoalRepository savingsGoalRepository) {
        this.investmentRepository = investmentRepository;
        this.savingsGoalRepository = savingsGoalRepository;
    }

    public String save(UUID savingsGoalId, Investment investment) {
        Optional<SavingsGoal> goal = savingsGoalRepository.findById(savingsGoalId);
        if (goal.isEmpty()) {
            return "The savings goal with that id does not exist";
        }
        if (goal.get().getStatus() == SavingsGoalStatus.ACHIEVED) {
            investment.setInvestedDate(LocalDate.now());
            investment.setSavingsGoal(goal.get());
            investmentRepository.save(investment);
            return "Investment saved successfully";
        } else {
            return "Investments are only for ACHIEVED savings goals";
        }
    }

    public List<Investment> findBySaverUsername(String username) {
        return investmentRepository.findBySaverUsername(username);
    }

    public String delete(UUID id) {
        if (!investmentRepository.existsById(id)) {
            return "The investment with that id does not exist";
        }
        investmentRepository.deleteById(id);
        return "Investment deleted successfully";
    }
}
