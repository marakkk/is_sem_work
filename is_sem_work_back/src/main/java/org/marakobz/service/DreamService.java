package org.marakobz.service;

import org.marakobz.dto.DreamDto;
import org.marakobz.model.*;
import org.marakobz.repository.DreamRepository;
import org.marakobz.repository.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Service
public class DreamService {

    private final DreamRepository dreamRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private static final Logger logger = LoggerFactory.getLogger(DreamService.class);

    public DreamService(DreamRepository dreamRepository, UserRepository userRepository, AuthService authService) {
        this.dreamRepository = dreamRepository;
        this.userRepository = userRepository;
        this.authService = authService;
    }


}
