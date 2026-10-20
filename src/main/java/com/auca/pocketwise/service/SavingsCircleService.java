package com.auca.pocketwise.service;

import com.auca.pocketwise.model.SavingsCircle;
import com.auca.pocketwise.repository.SavingsCircleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SavingsCircleService {

    private final SavingsCircleRepository savingsCircleRepository;

    public SavingsCircleService(SavingsCircleRepository savingsCircleRepository) {
        this.savingsCircleRepository = savingsCircleRepository;
    }

    public String save(SavingsCircle savingsCircle) {
        savingsCircleRepository.save(savingsCircle);
        return "Savings circle saved successfully";
    }

    public List<SavingsCircle> findAll() {
        return savingsCircleRepository.findAll();
    }

    public List<SavingsCircle> findFree() {
        return savingsCircleRepository.findFree();
    }

    public Optional<SavingsCircle> findById(UUID id) {
        return savingsCircleRepository.findById(id);
    }

    public Optional<SavingsCircle> update(UUID id, SavingsCircle incoming) {
        return savingsCircleRepository.findById(id).map(existing -> {
            existing.setCircleNumber(incoming.getCircleNumber());
            existing.setName(incoming.getName());
            return savingsCircleRepository.save(existing);
        });
    }

    public String delete(UUID id) {
        if (!savingsCircleRepository.existsById(id)) {
            return "The savings circle with that id does not exist";
        }
        savingsCircleRepository.deleteById(id);
        return "Savings circle deleted successfully";
    }
}
