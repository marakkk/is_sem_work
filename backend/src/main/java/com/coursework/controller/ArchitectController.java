package com.coursework.controller;

import com.coursework.dto.ArchitectDto;
import com.coursework.dto.DreamDto;
import com.coursework.model.Architect;
import com.coursework.model.Dream;
import com.coursework.service.ArchitectService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
        Dream updatedDream = architectService.updateDreamPrice(dreamId, price);
        return new ResponseEntity<>(updatedDream, HttpStatus.OK);
    }

    @PostMapping("/create-template")
    public ResponseEntity<Object> createTemplate(@RequestBody DreamDto dreamDto, HttpServletRequest request) {
        architectService.createTemplate(dreamDto, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
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

