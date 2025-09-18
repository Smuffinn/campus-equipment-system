package edu.cit.tabungar.stevenjan.campusequipmentloan.Controller;

import edu.cit.tabungar.stevenjan.campusequipmentloan.Service.EquipmentService;
import edu.cit.tabungar.stevenjan.campusequipmentloan.Model.Loan;
import edu.cit.tabungar.stevenjan.campusequipmentloan.DTO.LoanRequestDTO;
import edu.cit.tabungar.stevenjan.campusequipmentloan.Service.LoanService;
import edu.cit.tabungar.stevenjan.campusequipmentloan.Service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/api")

public class LoanController {

    private final LoanService loanService;
    private final EquipmentService equipmentService;
    private final StudentService studentService;

    @Autowired
    public LoanController(LoanService loanService, EquipmentService equipmentService, StudentService studentService) {
        this.loanService = loanService;
        this.equipmentService = equipmentService;
        this.studentService = studentService;
    }

 
    @GetMapping("/loans")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved loans")
    public ResponseEntity<List<Loan>> getAllLoans() {
        List<Loan> loans = loanService.getAllLoans();
        return ResponseEntity.ok(loans);
    }

    // GET /api/loans/{id} → get loan by id
    @GetMapping("/loans/{id}")
    public ResponseEntity<Loan> getLoanById(@PathVariable Long id) {
        Optional<Loan> loan = loanService.getLoanById(id);
        return loan.map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/loans → create loan
    @PostMapping("/loans")
    public ResponseEntity<Loan> createLoan(@Valid @RequestBody LoanRequestDTO loanRequest) {
        try {
            Loan createdLoan = loanService.createLoan(loanRequest);
            return ResponseEntity.ok(createdLoan);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // POST /api/loans/{id}/return → return loan
    @PostMapping("/loans/{id}/return")
    public ResponseEntity<Loan> returnLoan(@PathVariable Long id) {
        try {
            Loan returnedLoan = loanService.returnLoan(id);
            return ResponseEntity.ok(returnedLoan);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // POST /api/loans/{id}/extend → extend loan
    @PostMapping("/loans/{id}/extend")
    public ResponseEntity<Loan> extendLoan(@PathVariable Long id, @RequestParam int days) {
        try {
            Loan extendedLoan = loanService.extendLoan(id, days);
            return ResponseEntity.ok(extendedLoan);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // GET /api/loans/student/{studentId} → get loans for student
    @GetMapping("/loans/student/{studentId}")
    public ResponseEntity<List<Loan>> getLoansByStudent(@PathVariable Long studentId) {
        List<Loan> loans = loanService.getLoansByStudent(studentId);
        return ResponseEntity.ok(loans);
    }

    // GET /api/loans/overdue → get overdue loans
    @GetMapping("/loans/overdue")
    public ResponseEntity<List<Loan>> getOverdueLoans() {
        List<Loan> overdueLoans = loanService.getOverdueLoans();
        return ResponseEntity.ok(overdueLoans);
    }

    // GET /api/loans/{id}/penalty → calculate penalty for loan
    @GetMapping("/loans/{id}/penalty")
    public ResponseEntity<Double> calculatePenalty(@PathVariable Long id) {
        try {
            double penalty = loanService.calculatePenalty(id);
            return ResponseEntity.ok(penalty);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

}

