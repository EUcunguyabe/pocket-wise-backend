package com.auca.pocketwise.service;

import com.auca.pocketwise.model.SavingsGoalStatus;
import com.auca.pocketwise.repository.CoachRepository;
import com.auca.pocketwise.repository.InvestmentRepository;
import com.auca.pocketwise.repository.SaverRepository;
import com.auca.pocketwise.repository.SavingsCircleRepository;
import com.auca.pocketwise.repository.SavingsGoalRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class DashboardService {

    private final SaverRepository saverRepository;
    private final CoachRepository coachRepository;
    private final SavingsCircleRepository savingsCircleRepository;
    private final SavingsGoalRepository savingsGoalRepository;
    private final InvestmentRepository investmentRepository;

    public DashboardService(SaverRepository saverRepository,
                            CoachRepository coachRepository,
                            SavingsCircleRepository savingsCircleRepository,
                            SavingsGoalRepository savingsGoalRepository,
                            InvestmentRepository investmentRepository) {
        this.saverRepository = saverRepository;
        this.coachRepository = coachRepository;
        this.savingsCircleRepository = savingsCircleRepository;
        this.savingsGoalRepository = savingsGoalRepository;
        this.investmentRepository = investmentRepository;
    }

    public Map<String, Long> summary() {
        Map<String, Long> summary = new LinkedHashMap<>();
        summary.put("savers", saverRepository.count());
        summary.put("coaches", coachRepository.count());
        summary.put("freeSavingsCircles", (long) savingsCircleRepository.findFree().size());
        summary.put("goalsDueToday", savingsGoalRepository.countByTargetDate(LocalDate.now()));
        summary.put("draft", savingsGoalRepository.countByStatus(SavingsGoalStatus.DRAFT));
        summary.put("investments", investmentRepository.count());
        return summary;
    }
}
