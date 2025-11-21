package org.marakobz.service;

import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import org.marakobz.dto.AdminRequestDto;
import org.marakobz.dto.ArchitectRequestDto;
import org.marakobz.dto.AdminRequestMapper;
import org.marakobz.dto.ArchitectRequestMapper;
import org.marakobz.enums.AdminStatus;
import org.marakobz.enums.ReservationStatus;
import org.marakobz.model.Admin;
import org.marakobz.model.Architect;
import org.marakobz.model.Reservation;
import org.marakobz.repository.AdminRepository;
import org.marakobz.repository.ArchitectureRepository;
import org.marakobz.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {

    private final ArchitectureRepository architectureRepository;
    private final AdminRepository adminRepository;

    public AdminService(ArchitectureRepository architectureRepository, AdminRepository adminRepository, ReservationRepository reservationRepository) {
        this.architectureRepository = architectureRepository;
        this.adminRepository = adminRepository;
    }

    public List<ArchitectRequestDto> getArchitectRequests() {
        List<Architect> architects = architectureRepository.findByStatus(AdminStatus.REQUESTED);
        return architects.stream()
                .map(ArchitectRequestMapper.INSTANCE::architectToArchitectRequestDto)
                .collect(Collectors.toList());
    }

    public List<AdminRequestDto> getAdminRequests() {
        List<Admin> admins = adminRepository.findByStatus(AdminStatus.REQUESTED);
        return admins.stream()
                .map(AdminRequestMapper.INSTANCE::adminToAdminRequestDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateArchitectStatus(Long architectId, AdminStatus status) {
        Architect architect = architectureRepository.findById(architectId)
                .orElseThrow(() -> new NoResultException("Architect not found"));
        architect.setStatus(status);
        architectureRepository.save(architect);
    }

    @Transactional
    public void updateAdminStatus(Long adminId, AdminStatus status) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new NoResultException("Admin not found"));
        admin.setStatus(status);
        adminRepository.save(admin);
    }


}