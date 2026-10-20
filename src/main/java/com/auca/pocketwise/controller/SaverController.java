package com.auca.pocketwise.controller;

import com.auca.pocketwise.model.Saver;
import com.auca.pocketwise.service.CoachService;
import com.auca.pocketwise.service.SaverService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
@RequestMapping("/api/savers")
public class SaverController {

    private final SaverService saverService;
    private final CoachService coachService;

    public SaverController(SaverService saverService, CoachService coachService) {
        this.saverService = saverService;
        this.coachService = coachService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody Saver saver) {
        String message = saverService.save(saver);
        if (message.equals("Saver saved successfully")) {
            return new ResponseEntity<>(message, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> findAll() {
        return new ResponseEntity<>(saverService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/by-last-name")
    public ResponseEntity<?> findByLastName(@RequestParam String lastName) {
        return new ResponseEntity<>(saverService.findByLastName(lastName), HttpStatus.OK);
    }

    @GetMapping("/of-coach/{coachId}")
    public ResponseEntity<?> findSaversOfCoach(@PathVariable UUID coachId) {
        if (coachService.findById(coachId).isEmpty()) {
            return new ResponseEntity<>("The coach with that id does not exist", HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(saverService.findSaversOfCoach(coachId), HttpStatus.OK);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable UUID id) {
        Optional<Saver> saver = saverService.findById(id);
        if (saver.isPresent()) {
            return new ResponseEntity<>(saver.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("The saver with that id does not exist", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody Saver saver) {
        Optional<Saver> updated = saverService.update(id, saver);
        if (updated.isPresent()) {
            return new ResponseEntity<>(updated.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("The saver with that id does not exist", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        String message = saverService.delete(id);
        if (message.equals("Saver deleted successfully")) {
            return new ResponseEntity<>(message, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
    }
}
