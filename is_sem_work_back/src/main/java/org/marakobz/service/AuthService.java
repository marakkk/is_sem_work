package org.marakobz.service;

import jakarta.persistence.NoResultException;
import org.marakobz.enums.AdminStatus;
import org.marakobz.enums.Roles;
import org.marakobz.model.Admin;
import org.marakobz.model.Architect;
import org.marakobz.model.DreamUser;
import org.marakobz.repository.AdminRepository;
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
    private final AdminRepository adminRepository;

    public AuthService(UserRepository userRepository, ArchitectureRepository architectureRepository, AdminRepository adminRepository) {
        this.userRepository = userRepository;
        this.architectureRepository = architectureRepository;
        this.adminRepository = adminRepository;
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
            architect.setStatus(AdminStatus.REQUESTED); // Set status to REQUESTED
            architectureRepository.save(architect);
        } else if (user.getRole() == Roles.ADMIN) {
            logger.info("Creating admin entry for user: {}", user.getUsername());
            Admin admin = new Admin();
            admin.setUser(user);
            admin.setStatus(AdminStatus.REQUESTED); // Set status to REQUESTED
            adminRepository.save(admin); // Assuming you have an adminRepository
        }
    }


    public AdminStatus getUserStatus(DreamUser user) {
        if (user.getRole() == Roles.ARCHITECT) {
            Architect architect = architectureRepository.findByUser(user)
                    .orElseThrow(() -> new NoResultException("Architect not found"));
            return architect.getStatus();
        } else if (user.getRole() == Roles.ADMIN) {
            Admin admin = adminRepository.findByUser(user)
                    .orElseThrow(() -> new NoResultException("Admin not found"));
            return admin.getStatus();
        }
        return null;
    }


    public String login(String username, String password) {
        try {
            DreamUser user = userRepository.findByUsername(username);
            if (user != null && JWTUtil.verifyPassword(password, user.getPassword())) {
                if (user.getRole() == Roles.ARCHITECT) {
                    Architect architect = architectureRepository.findByUser(user)
                            .orElseThrow(() -> new NoResultException("Architect not found"));
                    if (architect.getStatus() != AdminStatus.APPROVED) {
                        logger.warn("Architect not approved: {}", username);
                        return null;
                    }
                } else if (user.getRole() == Roles.ADMIN) {
                    Admin admin = adminRepository.findByUser(user)
                            .orElseThrow(() -> new NoResultException("Admin not found"));
                    if (admin.getStatus() != AdminStatus.APPROVED) {
                        logger.warn("Admin not approved: {}", username);
                        return null;
                    }
                }
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