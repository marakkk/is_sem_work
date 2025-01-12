package org.marakobz.service;

import jakarta.persistence.NoResultException;
import org.marakobz.enums.Roles;
import org.marakobz.model.Architect;
import org.marakobz.model.DreamUser;
import org.marakobz.repository.ArchitectureRepository;
import org.marakobz.repository.UserRepository;
import org.marakobz.security.JWTUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService implements UserDetailsService {

    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    private final ArchitectureRepository architectureRepository;

    public AuthService(UserRepository userRepository, ArchitectureRepository architectureRepository) {
        this.userRepository = userRepository;
        this.architectureRepository = architectureRepository;
    }

    @Transactional
    public void register(DreamUser user) {
        logger.info("Registering user: {}", user.getUsername());
        user.setPassword(JWTUtil.hashPassword(user.getPassword()));
        userRepository.save(user);

        if (user.getRole() == Roles.ARCHITECT) {
            logger.info("Creating architect entry for user: {}", user.getUsername());
            Architect architect = new Architect();
            architect.setPrice(15);
            architect.setRating(0);
            architect.setUser(user);
            architectureRepository.save(architect);
        }
    }


    public String login(String username, String password) {
        try {
            DreamUser user = userRepository.findByUsername(username);
            if (user != null && JWTUtil.verifyPassword(password, user.getPassword())) {
                logger.info("User logged in: {}", username);
                return JWTUtil.generateToken(user);
            }
        } catch (NoResultException e) {
            logger.error("User not found: {}", username);
        }
        return null;
    }

    @Transactional(readOnly = true)
    public DreamUser getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }

    public DreamUser getUserFromToken(String token) {
        String username = JWTUtil.extractUsername(token);
        return userRepository.findByUsername(username);
    }


}