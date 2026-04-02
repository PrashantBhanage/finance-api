package com.prash.fintrackapi.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prash.fintrackapi.dto.DashboardDTO;
import com.prash.fintrackapi.service.DashboardService;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    // Get dashboard data
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<DashboardDTO> getDashboard(Authentication authentication) {
        String userEmail = authentication.getName();
        DashboardDTO dashboard = dashboardService.getDashboard(userEmail);
        return ResponseEntity.ok(dashboard);
    }
}