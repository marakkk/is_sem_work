package org.marakobz.controller;

import org.marakobz.dto.DreamDto;
import org.marakobz.model.Architect;
import org.marakobz.model.Dream;
import org.marakobz.service.ArchitectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dreams/architect-page")
public class ArchitectController {
    @Autowired
    private ArchitectService architectService;

    @GetMapping("/requests")
    public ResponseEntity<List<Dream>> getRequestsForDreams() {
        List<Dream> dreams = architectService.getRequestsForDreams();
        return new ResponseEntity<>(dreams, HttpStatus.OK);
    }

    @GetMapping("/ratings")
    public ResponseEntity<List<Architect>> getArchitectRatings() {
        List<Architect> architects = architectService.getArchitectRatings();
        return new ResponseEntity<>(architects, HttpStatus.OK);
    }

    @PutMapping("/update/{dreamId}")
    public ResponseEntity<Dream> updateDreamPrice(@PathVariable Long dreamId, @RequestParam int price) {
        try {
            Dream updatedDream = architectService.updateDreamPrice(dreamId, price);
            return new ResponseEntity<>(updatedDream, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/create-template")
    public ResponseEntity<Dream> createTemplate(@RequestBody DreamDto dreamDto) {
        try {
            Dream createdDream = architectService.createTemplate(dreamDto);
            return new ResponseEntity<>(createdDream, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}

