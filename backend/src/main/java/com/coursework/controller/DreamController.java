package com.coursework.controller;

import com.coursework.dto.DreamDto;
import com.coursework.dto.DreamTemplateDto;
import com.coursework.dto.DreamUserDto;
import com.coursework.model.Characters;
import com.coursework.service.DreamService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@RestController
@RequestMapping("/dreams")
public class DreamController {

    @Autowired
    private DreamService dreamService;

    @PostMapping("/create-own-dream")
    public ResponseEntity<Object> createOwnDream(@RequestBody DreamDto dreamDto, HttpServletRequest request) {
        try {
            dreamService.createOwnDream(dreamDto, request);
            return ResponseEntity.status(HttpStatus.CREATED).build();

        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Произошла непредвиденная ошибка. Пожалуйста, повторите попытку.");
        }
    }

    @GetMapping("/templates")
    public List<DreamTemplateDto> getTemplateDreams() {
        return dreamService.getTemplateDreams();
    }

    @GetMapping("/architects")
    public List<DreamUserDto> getArchitects() {
        return dreamService.getArchitects();
    }

    @GetMapping("/{dreamId}/characters")
    public ResponseEntity<List<Characters>> getCharactersByDreamId(@PathVariable Long dreamId) {
        List<Characters> characters = dreamService.getCharactersByDreamId(dreamId);
        return ResponseEntity.ok(characters);
    }

}

