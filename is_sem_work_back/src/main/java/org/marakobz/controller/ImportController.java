package org.marakobz.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.marakobz.dto.ImportHistoryDto;
import org.marakobz.model.*;
import org.marakobz.security.JWTUtil;
import org.marakobz.service.ImportService;
import org.marakobz.service.AuthService;
import org.marakobz.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/book-creatures/")
public class ImportController {

    private final ImportService importService;

    private final AuthService authService;

    private final UserRepository userRepository;

    public ImportController(ImportService importService, AuthService authService, UserRepository userRepository) {
        this.importService = importService;
        this.authService = authService;
        this.userRepository = userRepository;
    }

    /*@GetMapping("/history")
    public List<ImportHistoryDto> getHistory(
            @RequestParam(value = "userId", required = false) Long userId,
            @RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        Users user;

        if (userId != null) {
            user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));
        } else {
            user = authService.getUserFromToken(token);
        }

    }*/


    /*@PostMapping("/import")
    public ResponseEntity<ImportHistory> importCsv(
            @RequestParam("file") MultipartFile file,
            HttpServletRequest request) throws Exception {

        String username = JWTUtil.extractUsernameFromRequest(request);
        if (username == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Пользователь не существует");
        }

        Users user = userRepository.findByUsername(username);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Пользователь не найден");
        }

        ImportHistory history = importService.importFromCsv(file, user);

        return ResponseEntity.ok(history);
    }*/


}
