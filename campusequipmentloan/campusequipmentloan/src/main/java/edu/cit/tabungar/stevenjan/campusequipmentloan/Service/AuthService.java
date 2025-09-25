package edu.cit.tabungar.stevenjan.campusequipmentloan.Service;

import edu.cit.tabungar.stevenjan.campusequipmentloan.Payload.Request.SignupRequest;
import edu.cit.tabungar.stevenjan.campusequipmentloan.Payload.Response.MessageResponse;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    boolean existsByEmail(String email);
    ResponseEntity<?> registerStudent(SignupRequest signUpRequest);
    ResponseEntity<?> registerAdmin(SignupRequest signUpRequest);
}
