package com.auca.pocketwise.service;

import com.auca.pocketwise.model.Coach;
import com.auca.pocketwise.model.Saver;
import com.auca.pocketwise.model.SavingsGoal;
import com.auca.pocketwise.model.SavingsGoalStatus;
import com.auca.pocketwise.repository.CoachRepository;
import com.auca.pocketwise.repository.InvestmentRepository;
import com.auca.pocketwise.repository.SaverRepository;
import com.auca.pocketwise.repository.SavingsGoalRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SavingsGoalService {

    private final SavingsGoalRepository savingsGoalRepository;
    private final SaverRepository saverRepository;
    private final CoachRepository coachRepository;
    private final InvestmentRepository investmentRepository;

    public SavingsGoalService(SavingsGoalRepository savingsGoalRepository,
                              SaverRepository saverRepository,
                              CoachRepository coachRepository,
                              InvestmentRepository investmentRepository) {
        this.savingsGoalRepository = savingsGoalRepository;
        this.saverRepository = saverRepository;
        this.coachRepository = coachRepository;
        this.investmentRepository = investmentRepository;
    }

    public String save(SavingsGoal savingsGoal) {
        if (savingsGoal.getTargetDate() != null && savingsGoal.getTargetDate().isBefore(LocalDate.now())) {
            return "Savings goal date cannot be in the past";
        }
        String linkMessage = linkSaverAndCoach(savingsGoal);
        if (linkMessage != null) {
            return linkMessage;
        }
        if (savingsGoal.getCoach() != null && savingsGoal.getTargetDate() != null
                && savingsGoalRepository.existsByCoachAndTargetDateAndStatusNot(
                savingsGoal.getCoach(), savingsGoal.getTargetDate(), SavingsGoalStatus.ABANDONED)) {
            return "Coach already has a savings goal due on that date";
        }
        savingsGoal.setStatus(SavingsGoalStatus.DRAFT);
        savingsGoalRepository.save(savingsGoal);
        return "Savings goal saved successfully";
    }

    public List<SavingsGoal> findAll() {
        return savingsGoalRepository.findAll();
    }

    public List<SavingsGoal> findByStatus(SavingsGoalStatus status) {
        return savingsGoalRepository.findByStatusOrderByTargetDateAsc(status);
    }

    public List<SavingsGoal> findBetween(LocalDate start, LocalDate end) {
        return savingsGoalRepository.findByTargetDateBetweenOrderByTargetDateAsc(start, end);
    }

    public List<Object[]> countByStatus() {
        return savingsGoalRepository.countGroupedByStatus();
    }

    public Page<SavingsGoal> findPage(Pageable pageable) {
        return savingsGoalRepository.findAll(pageable);
    }

    @Transactional
    public String cancelDay(UUID coachId, LocalDate date) {
        int updated = savingsGoalRepository.abandonByCoachAndDate(
                coachId, date, SavingsGoalStatus.ABANDONED, SavingsGoalStatus.ACHIEVED);
        return updated + " savings goals cancelled";
    }

    public Optional<SavingsGoal> findById(UUID id) {
        return savingsGoalRepository.findById(id);
    }

    public Optional<SavingsGoal> update(UUID id, SavingsGoal incoming) {
        Optional<SavingsGoal> existing = savingsGoalRepository.findById(id);
        if (existing.isEmpty()) {
            return Optional.empty();
        }
        SavingsGoal goal = existing.get();
        goal.setTargetDate(incoming.getTargetDate());
        goal.setTitle(incoming.getTitle());
        goal.setTargetAmount(incoming.getTargetAmount());
        // Status changes only through updateStatus. The value sent here is ignored.
        if (incoming.getSaver() != null) {
            goal.setSaver(incoming.getSaver());
        }
        if (incoming.getCoach() != null) {
            goal.setCoach(incoming.getCoach());
        }
        String linkMessage = linkSaverAndCoach(goal);
        if (linkMessage != null) {
            return Optional.empty();
        }
        return Optional.of(savingsGoalRepository.save(goal));
    }

    public String updateStatus(UUID id, SavingsGoalStatus newStatus) {
        Optional<SavingsGoal> optional = savingsGoalRepository.findById(id);
        if (optional.isEmpty()) {
            return "The savings goal with that id does not exist";
        }
        SavingsGoal goal = optional.get();
        SavingsGoalStatus current = goal.getStatus();
        boolean allowed = (current == SavingsGoalStatus.DRAFT
                && (newStatus == SavingsGoalStatus.ACTIVE || newStatus == SavingsGoalStatus.ABANDONED))
                || (current == SavingsGoalStatus.ACTIVE
                && (newStatus == SavingsGoalStatus.ACHIEVED || newStatus == SavingsGoalStatus.ABANDONED));
        if (!allowed) {
            return "Cannot change status from " + current + " to " + newStatus;
        }
        goal.setStatus(newStatus);
        savingsGoalRepository.save(goal);
        return "Savings goal status updated successfully";
    }

    public String delete(UUID id) {
        Optional<SavingsGoal> goal = savingsGoalRepository.findById(id);
        if (goal.isEmpty()) {
            return "The savings goal with that id does not exist";
        }
        if (investmentRepository.existsBySavingsGoal(goal.get())) {
            return "That savings goal has investments, delete them first";
        }
        savingsGoalRepository.delete(goal.get());
        return "Savings goal deleted successfully";
    }

    private String linkSaverAndCoach(SavingsGoal goal) {
        if (goal.getSaver() != null && goal.getSaver().getId() != null) {
            Optional<Saver> saver = saverRepository.findById(goal.getSaver().getId());
            if (saver.isEmpty()) {
                return "The saver with that id does not exist";
            }
            goal.setSaver(saver.get());
        }
        if (goal.getCoach() != null && goal.getCoach().getId() != null) {
            Optional<Coach> coach = coachRepository.findById(goal.getCoach().getId());
            if (coach.isEmpty()) {
                return "The coach with that id does not exist";
            }
            goal.setCoach(coach.get());
        }
        return null;
    }
}
