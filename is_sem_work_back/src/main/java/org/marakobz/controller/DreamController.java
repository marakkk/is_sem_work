package org.marakobz.controller;

import org.marakobz.dto.DreamDto;
import org.marakobz.dto.DreamUserDto;
import org.marakobz.model.Dream;
import org.marakobz.service.DreamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/dreams")
public class DreamController {

    @Autowired
    private DreamService dreamService;

    @PostMapping("/create-own-dream")
    public Dream createOwnDream(@RequestBody DreamDto dreamDto) {
        return dreamService.createOwnDream(dreamDto);
    }

    @GetMapping("/templates")
    public List<DreamDto> getTemplateDreams() {
        return dreamService.getTemplateDreams();
    }

    @GetMapping("/architects")
    public List<DreamUserDto> getArchitects() {
        return dreamService.getArchitects();
    }

    @PostMapping("/assign-architect/{dreamId}")
    public Dream assignArchitectToDream(@PathVariable Long dreamId, @RequestBody Long architectId) {
        return dreamService.assignArchitectToDream(dreamId, architectId);
    }
}

