package com.bookingManagement.service;


import com.bookingManagement.model.AuthenticationResponse;
import com.bookingManagement.model.Token;
import com.bookingManagement.model.User;
import com.bookingManagement.repository.TokenRepository;
import com.bookingManagement.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    private final TokenRepository tokenRepository;

    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UserRepository repository,
                                 PasswordEncoder passwordEncoder,
                                 JwtService jwtService,
                                 TokenRepository tokenRepository,
                                 AuthenticationManager authenticationManager) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.tokenRepository = tokenRepository;
        this.authenticationManager = authenticationManager;
    }

    public AuthenticationResponse register(User request) {

        // check if user already exist. if exist than authenticate the user
        if(repository.findByUsername(request.getUsername()).isPresent()) {
            return new AuthenticationResponse(null, "User already exist");
        }

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));


        user.setRole(request.getRole());

        user = repository.save(user);

        String jwt = jwtService.generateToken(user);

        saveUserToken(jwt, user);

        return new AuthenticationResponse(jwt, "User registration was successful");

    }

    public AuthenticationResponse update(User updatedUser) {
        // Cerca l'utente nel repository
        User existingUser = repository.findByUsername(updatedUser.getUsername()).orElse(null);
        if (existingUser == null) {
            return new AuthenticationResponse(null, "User not found");
        }

        // Aggiorna le informazioni dell'utente con i nuovi valori
        existingUser.setFirstName(updatedUser.getFirstName()!=null? updatedUser.getFirstName():existingUser.getFirstName());
        existingUser.setLastName(updatedUser.getLastName()!=null? updatedUser.getLastName():existingUser.getLastName());
        if(updatedUser.getPassword()!=null)
            existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword())); // Assicurati di codificare la nuova password

        // Salva l'utente aggiornato nel repository
        User savedUser = repository.save(existingUser);

        // Genera un nuovo token JWT per l'utente aggiornato
        String jwt = jwtService.generateToken(savedUser);

        // Se l'utente ha già dei token salvati, revoca quelli esistenti
        revokeAllTokenByUser(savedUser);

        // Salva il nuovo token per l'utente
        saveUserToken(jwt, savedUser);

        // Ritorna una risposta con il token JWT e un messaggio di successo
        return new AuthenticationResponse(jwt, "User information updated successfully");
    }

    public AuthenticationResponse delete(String username) {
        // Cerca l'utente nel repository
        User user = repository.findByUsername(username).orElse(null);
        if (user == null) {
            return new AuthenticationResponse(null, "User not found");
        }

        // Elimina tutti i token dell'utente
        revokeAllTokenByUser(user);

        // Elimina l'utente dal repository
        repository.delete(user);

        return new AuthenticationResponse(null, "User deleted successfully");
    }



    public AuthenticationResponse authenticate(User request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        User user = repository.findByUsername(request.getUsername()).orElseThrow();
        String jwt = jwtService.generateToken(user);

        revokeAllTokenByUser(user);
        saveUserToken(jwt, user);

        return new AuthenticationResponse(jwt, "User login was successful");

    }
    private void revokeAllTokenByUser(User user) {
        List<Token> validTokens = tokenRepository.findAllTokensByUser(user.getId());
        if(validTokens.isEmpty()) {
            return;
        }

        validTokens.forEach(t-> {
            t.setLoggedOut(true);
        });

        tokenRepository.saveAll(validTokens);
    }
    private void saveUserToken(String jwt, User user) {
        Token token = new Token();
        token.setToken(jwt);
        token.setLoggedOut(false);
        token.setUser(user);
        tokenRepository.save(token);
    }

    //servizio per ottenere le informazioni dell'utente autenticato
    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof User) {
            return (User) authentication.getPrincipal();
        }

        return null;
    }
}
