package edu.cit.tabungar.stevenjan.campusequipmentloan.Controller;

import edu.cit.tabungar.stevenjan.campusequipmentloan.Model.Loan;
import edu.cit.tabungar.stevenjan.campusequipmentloan.Service.LoanService;
import edu.cit.tabungar.stevenjan.campusequipmentloan.Model.Student;
import edu.cit.tabungar.stevenjan.campusequipmentloan.Service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;
    private final LoanService loanService;

    @Autowired
    public StudentController(StudentService studentService, LoanService loanService) {
        this.studentService = studentService;
        this.loanService = loanService;
    }

    // GET /api/students → get all students
    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = studentService.getAllStudents();
        return ResponseEntity.ok(students);
    }

    // GET /api/students/{id} → get student by id
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        Optional<Student> student = studentService.getStudentById(id);
        return student.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    // GET /api/students/studentNo/{studentNo} → get student by student number
    @GetMapping("/studentNo/{studentNo}")
    public ResponseEntity<Student> getStudentByStudentNo(@PathVariable String studentNo) {
        Optional<Student> student = studentService.getStudentByStudentNo(studentNo);
        return student.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/students → create new student
    @PostMapping({"", "/"})
    public ResponseEntity<Student> createStudent(@Valid @RequestBody Student student) {
        try {
            Student createdStudent = studentService.createStudent(student);
            return ResponseEntity.ok(createdStudent);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // GET /api/students/{id}/loans → get loans for student
    @GetMapping("/{id}/loans")
    public ResponseEntity<List<Loan>> getStudentLoans(@PathVariable Long id) {
        List<Loan> loans = loanService.getLoansByStudent(id);
        return ResponseEntity.ok(loans);
    }

    // GET /api/students/{id}/loans/active → get active loans for student
    @GetMapping("/{id}/loans/active")
    public ResponseEntity<List<Loan>> getActiveStudentLoans(@PathVariable Long id) {
        List<Loan> activeLoans = loanService.getActiveLoansForStudent(id);
        return ResponseEntity.ok(activeLoans);
    }
}
