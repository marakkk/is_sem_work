package org.marakobz.controller;

import org.marakobz.model.DreamUser;
import org.marakobz.service.DreamService;
import org.marakobz.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dreams")
public class DreamController {

    private final DreamService dreamService;
    private final AuthService authService;

    public DreamController(DreamService dreamService, AuthService authService) {
        this.dreamService = dreamService;
        this.authService = authService;
    }

}
