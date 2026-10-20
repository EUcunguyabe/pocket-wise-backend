package com.auca.pocketwise.controller;

import com.auca.pocketwise.model.ExpertiseArea;
import com.auca.pocketwise.service.ExpertiseAreaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/expertise-areas")
public class ExpertiseAreaController {

    private final ExpertiseAreaService expertiseAreaService;

    public ExpertiseAreaController(ExpertiseAreaService expertiseAreaService) {
        this.expertiseAreaService = expertiseAreaService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody ExpertiseArea expertiseArea) {
        String message = expertiseAreaService.save(expertiseArea);
        if (message.equals("Expertise area saved successfully")) {
            return new ResponseEntity<>(message, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> findAll() {
        return new ResponseEntity<>(expertiseAreaService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/unused")
    public ResponseEntity<?> findUnused() {
        return new ResponseEntity<>(expertiseAreaService.findUnused(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable UUID id) {
        Optional<ExpertiseArea> expertiseArea = expertiseAreaService.findById(id);
        if (expertiseArea.isPresent()) {
            return new ResponseEntity<>(expertiseArea.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("The expertise area with that id does not exist", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody ExpertiseArea expertiseArea) {
        Optional<ExpertiseArea> updated = expertiseAreaService.update(id, expertiseArea);
        if (updated.isPresent()) {
            return new ResponseEntity<>(updated.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("The expertise area with that id does not exist", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        String message = expertiseAreaService.delete(id);
        if (message.equals("Expertise area deleted successfully")) {
            return new ResponseEntity<>(message, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
    }
}
