package org.marakobz.controller;

import org.marakobz.dto.DreamDto;
import org.marakobz.model.Dream;
import org.marakobz.service.DreamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dreams")
public class DreamController {

    @Autowired
    private DreamService dreamService;

    @PostMapping("/create-own-dream")
    public Dream createOwnDream(@RequestBody DreamDto dreamDto) {
        return dreamService.createOwnDream(dreamDto);
    }
}

