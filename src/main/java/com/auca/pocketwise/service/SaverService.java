package com.auca.pocketwise.service;

import com.auca.pocketwise.model.Saver;
import com.auca.pocketwise.repository.SaverRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SaverService {

    private final SaverRepository saverRepository;

    public SaverService(SaverRepository saverRepository) {
        this.saverRepository = saverRepository;
    }

    public String save(Saver saver) {
        saverRepository.save(saver);
        return "Saver saved successfully";
    }

    public List<Saver> findAll() {
        return saverRepository.findAll();
    }

    public List<Saver> findByLastName(String lastName) {
        return saverRepository.findByLastNameIgnoreCaseOrderByFirstNameAsc(lastName);
    }

    public List<Saver> findSaversOfCoach(UUID coachId) {
        return saverRepository.findSaversOfCoach(coachId);
    }

    public Optional<Saver> findById(UUID id) {
        return saverRepository.findById(id);
    }

    public Optional<Saver> update(UUID id, Saver incoming) {
        return saverRepository.findById(id).map(existing -> {
            existing.setFirstName(incoming.getFirstName());
            existing.setLastName(incoming.getLastName());
            existing.setUsername(incoming.getUsername());
            existing.setEmail(incoming.getEmail());
            return saverRepository.save(existing);
        });
    }

    public String delete(UUID id) {
        if (!saverRepository.existsById(id)) {
            return "The saver with that id does not exist";
        }
        saverRepository.deleteById(id);
        return "Saver deleted successfully";
    }
}
