package com.coursework.controller;

import com.coursework.dto.AdminRequestDto;
import com.coursework.dto.ArchitectRequestDto;
import com.coursework.enums.AdminStatus;
import com.coursework.enums.ReservationStatus;
import com.coursework.service.AdminService;
import com.coursework.service.ReservationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    private final ReservationService reservationService;

    @GetMapping("/architect-requests")
    public ResponseEntity<List<ArchitectRequestDto>> getArchitectRequests() {
        List<ArchitectRequestDto> requests = adminService.getArchitectRequests();
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/admin-requests")
    public ResponseEntity<List<AdminRequestDto>> getAdminRequests() {
        List<AdminRequestDto> requests = adminService.getAdminRequests();
        return ResponseEntity.ok(requests);
    }

    @PostMapping("/approve-architect/{id}")
    public ResponseEntity<Void> approveArchitect(@PathVariable Long id) {
        adminService.updateArchitectStatus(id, AdminStatus.APPROVED);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/deny-architect/{id}")
    public ResponseEntity<Void> denyArchitect(@PathVariable Long id) {
        adminService.updateArchitectStatus(id, AdminStatus.DENIED);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/approve-admin/{id}")
    public ResponseEntity<Void> approveAdmin(@PathVariable Long id) {
        adminService.updateAdminStatus(id, AdminStatus.APPROVED);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/deny-admin/{id}")
    public ResponseEntity<Void> denyAdmin(@PathVariable Long id) {
        adminService.updateAdminStatus(id, AdminStatus.DENIED);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/deny-reservation/{id}")
    public ResponseEntity<Void> denyReservation(@PathVariable Long id) {
        reservationService.updateResStatus(id, ReservationStatus.DENIED);
        return ResponseEntity.ok().build();
    }
}