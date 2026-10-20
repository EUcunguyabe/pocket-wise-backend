package com.auca.pocketwise.service;

import com.auca.pocketwise.model.ExpertiseArea;
import com.auca.pocketwise.repository.ExpertiseAreaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ExpertiseAreaService {

    private final ExpertiseAreaRepository expertiseAreaRepository;

    public ExpertiseAreaService(ExpertiseAreaRepository expertiseAreaRepository) {
        this.expertiseAreaRepository = expertiseAreaRepository;
    }

    public String save(ExpertiseArea expertiseArea) {
        expertiseAreaRepository.save(expertiseArea);
        return "Expertise area saved successfully";
    }

    public List<ExpertiseArea> findAll() {
        return expertiseAreaRepository.findAll();
    }

    public List<ExpertiseArea> findUnused() {
        return expertiseAreaRepository.findUnused();
    }

    public Optional<ExpertiseArea> findById(UUID id) {
        return expertiseAreaRepository.findById(id);
    }

    public Optional<ExpertiseArea> update(UUID id, ExpertiseArea incoming) {
        return expertiseAreaRepository.findById(id).map(existing -> {
            existing.setName(incoming.getName());
            return expertiseAreaRepository.save(existing);
        });
    }

    public String delete(UUID id) {
        if (!expertiseAreaRepository.existsById(id)) {
            return "The expertise area with that id does not exist";
        }
        expertiseAreaRepository.deleteById(id);
        return "Expertise area deleted successfully";
    }
}
