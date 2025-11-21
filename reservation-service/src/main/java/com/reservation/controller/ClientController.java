package com.reservation.controller;

import com.reservation.entity.Client;
import com.reservation.service.ClientService;
import com.reservation.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class ClientController {
    private final ClientService service;

    @PostMapping("/register")
    public ResponseEntity<Client> register(@RequestBody Client client) {
        return ResponseEntity.ok(service.register(client));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(service.login(request));
    }
}