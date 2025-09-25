package edu.cit.tabungar.stevenjan.campusequipmentloan.Service;

import edu.cit.tabungar.stevenjan.campusequipmentloan.Model.Role;
import edu.cit.tabungar.stevenjan.campusequipmentloan.Model.Student;
import edu.cit.tabungar.stevenjan.campusequipmentloan.Model.User;
import edu.cit.tabungar.stevenjan.campusequipmentloan.Payload.Request.SignupRequest;
import edu.cit.tabungar.stevenjan.campusequipmentloan.Payload.Response.MessageResponse;
import edu.cit.tabungar.stevenjan.campusequipmentloan.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final StudentService studentService;
    private final PasswordEncoder encoder;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository, StudentService studentService, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.studentService = studentService;
        this.encoder = encoder;
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public ResponseEntity<?> registerStudent(SignupRequest signUpRequest) {
        // Create Student entity
        Student student = new Student();
        student.setName(signUpRequest.getName());
        student.setEmail(signUpRequest.getEmail());
        student.setStudentNo(signUpRequest.getStudentNo());
        Student savedStudent = studentService.createStudent(student);

        // Create User entity linked to student
        User user = new User();
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(encoder.encode(signUpRequest.getPassword()));
        user.addRole(Role.ROLE_STUDENT);
        user.setStudent(savedStudent);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("Student registered successfully!"));
    }

    @Override
    public ResponseEntity<?> registerAdmin(SignupRequest signUpRequest) {
        // Create User entity with ADMIN role
        User user = new User();
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(encoder.encode(signUpRequest.getPassword()));
        user.addRole(Role.ROLE_ADMIN);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("Admin registered successfully!"));
    }
}
