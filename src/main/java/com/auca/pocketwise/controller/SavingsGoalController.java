package com.auca.pocketwise.controller;

import com.auca.pocketwise.model.SavingsGoal;
import com.auca.pocketwise.model.SavingsGoalStatus;
import com.auca.pocketwise.service.SavingsGoalService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/savings-goals")
public class SavingsGoalController {

    private final SavingsGoalService savingsGoalService;

    public SavingsGoalController(SavingsGoalService savingsGoalService) {
        this.savingsGoalService = savingsGoalService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody SavingsGoal savingsGoal) {
        String message = savingsGoalService.save(savingsGoal);
        if (message.equals("Savings goal saved successfully")) {
            return new ResponseEntity<>(message, HttpStatus.CREATED);
        } else if (message.equals("Savings goal date cannot be in the past")) {
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        } else if (message.equals("Coach already has a savings goal due on that date")) {
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        } else if (message.equals("The saver with that id does not exist")
                || message.equals("The coach with that id does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> findAll() {
        return new ResponseEntity<>(savingsGoalService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/by-status")
    public ResponseEntity<?> findByStatus(@RequestParam SavingsGoalStatus status) {
        return new ResponseEntity<>(savingsGoalService.findByStatus(status), HttpStatus.OK);
    }

    @GetMapping("/between")
    public ResponseEntity<?> findBetween(@RequestParam String start, @RequestParam String end) {
        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);
        return new ResponseEntity<>(savingsGoalService.findBetween(startDate, endDate), HttpStatus.OK);
    }

    @GetMapping("/page")
    public ResponseEntity<?> page(@RequestParam int page, @RequestParam int size, @RequestParam String sort) {
        String[] parts = sort.split(",");
        Sort.Direction direction = Sort.Direction.ASC;
        if (parts.length > 1 && parts[1].equalsIgnoreCase("desc")) {
            direction = Sort.Direction.DESC;
        }
        PageRequest pageable = PageRequest.of(page, size, Sort.by(direction, parts[0]));
        return new ResponseEntity<>(savingsGoalService.findPage(pageable), HttpStatus.OK);
    }

    @GetMapping("/stats/by-status")
    public ResponseEntity<List<Object[]>> statsByStatus() {
        return new ResponseEntity<>(savingsGoalService.countByStatus(), HttpStatus.OK);
    }

    @PatchMapping("/cancel-day")
    public ResponseEntity<String> cancelDay(@RequestParam UUID coachId, @RequestParam String date) {
        LocalDate day = LocalDate.parse(date);
        return new ResponseEntity<>(savingsGoalService.cancelDay(coachId, day), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable UUID id) {
        Optional<SavingsGoal> savingsGoal = savingsGoalService.findById(id);
        if (savingsGoal.isPresent()) {
            return new ResponseEntity<>(savingsGoal.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("The savings goal with that id does not exist", HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<String> updateStatus(@PathVariable UUID id, @RequestParam SavingsGoalStatus status) {
        String message = savingsGoalService.updateStatus(id, status);
        if (message.equals("Savings goal status updated successfully")) {
            return new ResponseEntity<>(message, HttpStatus.OK);
        } else if (message.equals("The savings goal with that id does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody SavingsGoal savingsGoal) {
        Optional<SavingsGoal> updated = savingsGoalService.update(id, savingsGoal);
        if (updated.isPresent()) {
            return new ResponseEntity<>(updated.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("The savings goal with that id does not exist", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        String message = savingsGoalService.delete(id);
        if (message.equals("Savings goal deleted successfully")) {
            return new ResponseEntity<>(message, HttpStatus.OK);
        } else if (message.equals("That savings goal has investments, delete them first")) {
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        } else {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
    }
}
