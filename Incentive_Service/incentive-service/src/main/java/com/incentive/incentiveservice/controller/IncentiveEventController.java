package com.incentive.incentiveservice.controller;

import com.incentive.incentiveservice.dto.IncentiveEventDTO;
import com.incentive.incentiveservice.dto.Response;
import com.incentive.incentiveservice.enums.IncentiveType;
import com.incentive.incentiveservice.service.IncentiveEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/incentive")
@RequiredArgsConstructor
public class IncentiveEventController {

    private final IncentiveEventService incentiveEventService;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<Response<?>> createIncentiveEvent(@RequestBody IncentiveEventDTO incentiveEventDTO) {
        return ResponseEntity.ok(incentiveEventService.createIncentiveEvent(incentiveEventDTO));
    }

    @GetMapping("/points/{id}")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<Response<Integer>> getUserBalance(@PathVariable ("id") Long id) {
        return ResponseEntity.ok(incentiveEventService.getUserBalance(id));
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<Response<List<IncentiveEventDTO>>> getAllIncentiveEvents() {
        return ResponseEntity.ok(incentiveEventService.getAllIncentiveEvents());
    }

    @GetMapping("/type")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<Response<List<IncentiveEventDTO>>> getAllByType(@RequestBody Map<String, String> body) {
        IncentiveType type = IncentiveType.valueOf(body.get("type").toUpperCase());
        return ResponseEntity.ok(incentiveEventService.getAllByType(type));
    }

    @GetMapping("/user/{id}")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<Response<List<IncentiveEventDTO>>> getUserEvents(@PathVariable ("id") Long id) {
        return ResponseEntity.ok(incentiveEventService.getUserEvents(id));
    }
}
