package com.university.coursework.controller;

import jakarta.servlet.http.HttpServletRequest;
import com.university.coursework.dto.ArchitectDto;
import com.university.coursework.dto.DreamDto;
import com.university.coursework.model.Architect;
import com.university.coursework.model.Dream;
import com.university.coursework.service.ArchitectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@RestController
@RequestMapping("/dreams/architect")
public class ArchitectController {

    @Autowired
    private ArchitectService architectService;

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
    public ResponseEntity<Object> createTemplate(@RequestBody DreamDto dreamDto, HttpServletRequest request) {
        try {
            architectService.createTemplate(dreamDto, request);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Произошла непредвиденная ошибка. Пожалуйста, повторите попытку.");
        }
    }

    @GetMapping("/architects")
    public ResponseEntity<List<Architect>> getArchitects() {
        List<Architect> architects = architectService.getArchitects();
        return ResponseEntity.ok(architects);
    }

    @GetMapping("/all")
    public List<ArchitectDto> getAllArchitects() {
        return architectService.getAllArchitects();
    }
}

