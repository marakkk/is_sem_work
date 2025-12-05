package com.university.coursework.service;

import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import com.university.coursework.dto.AdminRequestDto;
import com.university.coursework.dto.ArchitectRequestDto;
import com.university.coursework.dto.AdminRequestMapper;
import com.university.coursework.dto.ArchitectRequestMapper;
import com.university.coursework.enums.AdminStatus;
import com.university.coursework.model.Admin;
import com.university.coursework.model.Architect;
import com.university.coursework.repository.AdminRepository;
import com.university.coursework.repository.ArchitectureRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class AdminService {

    private final ArchitectureRepository architectureRepository;

    private final AdminRepository adminRepository;

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