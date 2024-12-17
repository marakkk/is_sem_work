package org.marakobz.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/home-page")
public class CustomerController {

    @GetMapping("/main")
    public ResponseEntity<String> getHomePage() {
        return ResponseEntity.ok("Добро пожаловать на главную страницу DreamLand!");
    }
}
