package edu.cit.tabungar.stevenjan.campusequipmentloan.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {

    // This endpoint is secured to force 401 Unauthorized for unauthenticated requests to /
    @GetMapping("/")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> home() {
        return ResponseEntity.ok("Welcome. You are authenticated.");
    }

    // Also secure /login (non-form) to return 401 when not authenticated
    @GetMapping("/login")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> login() {
        return ResponseEntity.ok("Already authenticated.");
    }
}
