package com.auca.pocketwise.controller;

import com.auca.pocketwise.model.SavingsCircle;
import com.auca.pocketwise.service.SavingsCircleService;
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
@RequestMapping("/api/savings-circles")
public class SavingsCircleController {

    private final SavingsCircleService savingsCircleService;

    public SavingsCircleController(SavingsCircleService savingsCircleService) {
        this.savingsCircleService = savingsCircleService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody SavingsCircle savingsCircle) {
        String message = savingsCircleService.save(savingsCircle);
        if (message.equals("Savings circle saved successfully")) {
            return new ResponseEntity<>(message, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> findAll() {
        return new ResponseEntity<>(savingsCircleService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/free")
    public ResponseEntity<?> findFree() {
        return new ResponseEntity<>(savingsCircleService.findFree(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable UUID id) {
        Optional<SavingsCircle> savingsCircle = savingsCircleService.findById(id);
        if (savingsCircle.isPresent()) {
            return new ResponseEntity<>(savingsCircle.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("The savings circle with that id does not exist", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody SavingsCircle savingsCircle) {
        Optional<SavingsCircle> updated = savingsCircleService.update(id, savingsCircle);
        if (updated.isPresent()) {
            return new ResponseEntity<>(updated.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("The savings circle with that id does not exist", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        String message = savingsCircleService.delete(id);
        if (message.equals("Savings circle deleted successfully")) {
            return new ResponseEntity<>(message, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
    }
}
