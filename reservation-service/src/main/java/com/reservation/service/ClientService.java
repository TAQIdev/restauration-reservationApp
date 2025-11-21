package com.reservation.service;

import com.reservation.entity.Client;
import com.reservation.repository.ClientRepository;
import com.reservation.security.JwtUtil;
import com.reservation.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository repository;
    private final JwtUtil jwtUtil;

    public Client register(Client client) {
        if (repository.findByEmail(client.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }
        return repository.save(client);
    }

    public LoginResponse login(LoginRequest request) {
        Client client = repository.findByEmail(request.getEmail())
                .filter(c -> c.getPassword().equals(request.getPassword()))
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        String token = jwtUtil.generateToken(client.getEmail(), client.getId());

        return new LoginResponse(token, client.getId(), client.getName(), client.getEmail());
    }
}