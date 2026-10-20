package com.auca.pocketwise.controller;

import com.auca.pocketwise.model.Coach;
import com.auca.pocketwise.service.CoachService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/coaches")
public class CoachController {

    private final CoachService coachService;

    public CoachController(CoachService coachService) {
        this.coachService = coachService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody Coach coach) {
        String message = coachService.save(coach);
        if (message.equals("Coach saved successfully")) {
            return new ResponseEntity<>(message, HttpStatus.CREATED);
        } else if (message.equals("The savings circle with that id does not exist")
                || message.equals("The expertise area with that id does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> findAll() {
        return new ResponseEntity<>(coachService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/by-expertise-area")
    public ResponseEntity<?> findByExpertiseArea(@RequestParam String name) {
        return new ResponseEntity<>(coachService.findByExpertiseAreaName(name), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable UUID id) {
        Optional<Coach> coach = coachService.findById(id);
        if (coach.isPresent()) {
            return new ResponseEntity<>(coach.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("The coach with that id does not exist", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody Coach coach) {
        Optional<Coach> updated = coachService.update(id, coach);
        if (updated.isPresent()) {
            return new ResponseEntity<>(updated.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("The coach with that id does not exist", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{coachId}/expertise-areas/{expertiseAreaId}")
    public ResponseEntity<String> addExpertiseArea(@PathVariable UUID coachId, @PathVariable UUID expertiseAreaId) {
        String message = coachService.addExpertiseArea(coachId, expertiseAreaId);
        if (message.equals("Expertise area added to the coach")) {
            return new ResponseEntity<>(message, HttpStatus.OK);
        } else if (message.equals("The coach already has that expertise area")) {
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        } else {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/{coachId}/savings-circle")
    public ResponseEntity<String> changeSavingsCircle(@PathVariable UUID coachId, @RequestParam int circleNumber) {
        String message = coachService.changeSavingsCircle(coachId, circleNumber);
        if (message.equals("Savings circle changed successfully")) {
            return new ResponseEntity<>(message, HttpStatus.OK);
        } else if (message.equals("That savings circle is taken by another coach")) {
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        } else {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        String message = coachService.delete(id);
        if (message.equals("Coach deleted successfully")) {
            return new ResponseEntity<>(message, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
    }
}
