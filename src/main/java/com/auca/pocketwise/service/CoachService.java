package com.auca.pocketwise.service;

import com.auca.pocketwise.model.Coach;
import com.auca.pocketwise.model.ExpertiseArea;
import com.auca.pocketwise.model.SavingsCircle;
import com.auca.pocketwise.repository.CoachRepository;
import com.auca.pocketwise.repository.ExpertiseAreaRepository;
import com.auca.pocketwise.repository.SavingsCircleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CoachService {

    private final CoachRepository coachRepository;
    private final SavingsCircleRepository savingsCircleRepository;
    private final ExpertiseAreaRepository expertiseAreaRepository;

    public CoachService(CoachRepository coachRepository,
                        SavingsCircleRepository savingsCircleRepository,
                        ExpertiseAreaRepository expertiseAreaRepository) {
        this.coachRepository = coachRepository;
        this.savingsCircleRepository = savingsCircleRepository;
        this.expertiseAreaRepository = expertiseAreaRepository;
    }

    public String save(Coach coach) {
        String linkMessage = linkCircleAndExpertise(coach);
        if (linkMessage != null) {
            return linkMessage;
        }
        coachRepository.save(coach);
        return "Coach saved successfully";
    }

    public List<Coach> findAll() {
        return coachRepository.findAll();
    }

    public List<Coach> findByExpertiseAreaName(String name) {
        return coachRepository.findByExpertiseAreaName(name);
    }

    public Optional<Coach> findById(UUID id) {
        return coachRepository.findById(id);
    }

    public Optional<Coach> update(UUID id, Coach incoming) {
        Optional<Coach> existing = coachRepository.findById(id);
        if (existing.isEmpty()) {
            return Optional.empty();
        }
        Coach coach = existing.get();
        coach.setFirstName(incoming.getFirstName());
        coach.setLastName(incoming.getLastName());
        coach.setEmail(incoming.getEmail());
        if (incoming.getSavingsCircle() != null) {
            coach.setSavingsCircle(incoming.getSavingsCircle());
        }
        if (incoming.getExpertiseAreas() != null) {
            coach.setExpertiseAreas(incoming.getExpertiseAreas());
        }
        String linkMessage = linkCircleAndExpertise(coach);
        if (linkMessage != null) {
            return Optional.empty();
        }
        return Optional.of(coachRepository.save(coach));
    }

    @Transactional
    public String addExpertiseArea(UUID coachId, UUID expertiseAreaId) {
        Optional<Coach> coachOpt = coachRepository.findById(coachId);
        if (coachOpt.isEmpty()) {
            return "The coach with that id does not exist";
        }
        Optional<ExpertiseArea> areaOpt = expertiseAreaRepository.findById(expertiseAreaId);
        if (areaOpt.isEmpty()) {
            return "That expertise area does not exist";
        }
        Coach coach = coachOpt.get();
        ExpertiseArea area = areaOpt.get();
        if (coach.getExpertiseAreas().contains(area)) {
            return "The coach already has that expertise area";
        }
        coach.getExpertiseAreas().add(area);
        coachRepository.save(coach);
        return "Expertise area added to the coach";
    }

    @Transactional
    public String changeSavingsCircle(UUID coachId, int circleNumber) {
        Optional<Coach> coachOpt = coachRepository.findById(coachId);
        if (coachOpt.isEmpty()) {
            return "The coach with that id does not exist";
        }
        Optional<SavingsCircle> circleOpt = savingsCircleRepository.findByCircleNumber(circleNumber);
        if (circleOpt.isEmpty()) {
            return "The savings circle with that circleNumber does not exist";
        }
        SavingsCircle circle = circleOpt.get();
        Coach coach = coachOpt.get();
        if (coachRepository.existsBySavingsCircle(circle)) {
            boolean alreadyHere = coach.getSavingsCircle() != null
                    && coach.getSavingsCircle().getId().equals(circle.getId());
            if (!alreadyHere) {
                return "That savings circle is taken by another coach";
            }
        }
        coach.setSavingsCircle(circle);
        coachRepository.save(coach);
        return "Savings circle changed successfully";
    }

    public String delete(UUID id) {
        if (!coachRepository.existsById(id)) {
            return "The coach with that id does not exist";
        }
        coachRepository.deleteById(id);
        return "Coach deleted successfully";
    }

    private String linkCircleAndExpertise(Coach coach) {
        if (coach.getSavingsCircle() != null && coach.getSavingsCircle().getId() != null) {
            Optional<SavingsCircle> circle = savingsCircleRepository.findById(coach.getSavingsCircle().getId());
            if (circle.isEmpty()) {
                return "The savings circle with that id does not exist";
            }
            coach.setSavingsCircle(circle.get());
        }
        if (coach.getExpertiseAreas() != null) {
            List<ExpertiseArea> managed = new ArrayList<>();
            for (ExpertiseArea area : coach.getExpertiseAreas()) {
                if (area.getId() == null) {
                    continue;
                }
                Optional<ExpertiseArea> found = expertiseAreaRepository.findById(area.getId());
                if (found.isEmpty()) {
                    return "The expertise area with that id does not exist";
                }
                managed.add(found.get());
            }
            coach.setExpertiseAreas(managed);
        }
        return null;
    }
}
