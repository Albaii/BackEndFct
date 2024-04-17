package com.fct.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.fct.entities.*;
import com.fct.repository.PlayerRepository;
import com.fct.requests.*;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PlayerRepository playerRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager; //autenticar al usuario

    @CrossOrigin
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));//Recibe credenciales
        UserDetails player = playerRepository.findByUsername(request.getUsername()).orElseThrow(); 
        String token = jwtService.getToken((Player) player);
        return AuthResponse.builder() 
            .token(token)
            .build();
    }

    @CrossOrigin
    public AuthResponse register(RegisterRequest request) {
        Player player = Player.builder()
            .username(request.getUsername())
            .password(passwordEncoder.encode(request.getPassword()))
            .firstname(request.getFirstname())
            .lastname(request.getLastname())
            .country(request.getCountry())
            .role(Role.USER)
            .build();

        playerRepository.save(player); //Se guarda el objeto en la base de datos 

        return AuthResponse.builder() //Se obtiene el token a traves del lservidode de jwt que se retorna al controlador y luego al cleinte
            .token(jwtService.getToken(player))
            .build();
    }

}
