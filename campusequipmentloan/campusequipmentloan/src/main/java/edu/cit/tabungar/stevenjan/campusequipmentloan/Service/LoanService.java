package edu.cit.tabungar.stevenjan.campusequipmentloan.Service;

import edu.cit.tabungar.stevenjan.campusequipmentloan.DTO.LoanRequestDTO;
import edu.cit.tabungar.stevenjan.campusequipmentloan.Model.Equipment;
import edu.cit.tabungar.stevenjan.campusequipmentloan.Model.Loan;
import edu.cit.tabungar.stevenjan.campusequipmentloan.Repository.EquipmentRepository;
import edu.cit.tabungar.stevenjan.campusequipmentloan.Repository.LoanRepository;
import edu.cit.tabungar.stevenjan.campusequipmentloan.Repository.Rules;
import edu.cit.tabungar.stevenjan.campusequipmentloan.Repository.StudentRepository;
import edu.cit.tabungar.stevenjan.campusequipmentloan.Model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class LoanService {

    private final LoanRepository loanRepository;
    private final EquipmentRepository equipmentRepository;
    private final StudentRepository studentRepository;
    private final Rules penaltyRules;

    @Autowired
    public LoanService(LoanRepository loanRepository, 
                      EquipmentRepository equipmentRepository,
                      StudentRepository studentRepository) {
        this.loanRepository = loanRepository;
        this.equipmentRepository = equipmentRepository;
        this.studentRepository = studentRepository;
        this.penaltyRules = new Rules.DailyRules(50.0); // ₱50 per day penalty
    }

    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }

    public Optional<Loan> getLoanById(Long id) {
        return loanRepository.findById(id);
    }

    public List<Loan> getLoansByStudent(Long studentId) {
        return loanRepository.findByStudentId(studentId);
    }

    public List<Loan> getActiveLoansForStudent(Long studentId) {
        return loanRepository.findActiveLoansForStudent(studentId);
    }

    public List<Loan> getOverdueLoans() {
        return loanRepository.findOverdueLoans(LocalDate.now());
    }

    public Loan createLoan(LoanRequestDTO loanRequest) {
        // Validate student exists
        Student student = studentRepository.findById(loanRequest.getStudentId())
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        // Validate equipment exists and is available
        Equipment equipment = equipmentRepository.findById(loanRequest.getEquipmentId())
                .orElseThrow(() -> new IllegalArgumentException("Equipment not found"));

        if (!equipment.isAvailable()) {
            throw new IllegalArgumentException("Equipment is not available for loan");
        }

        // Check if student has too many active loans (max 2)
        long activeLoans = loanRepository.countActiveLoansForStudent(student.getId());
        if (activeLoans >= 2) {
            throw new IllegalArgumentException("Student has reached maximum number of active loans (2)");
        }

        // Set loan details
        Loan loan = new Loan();
        loan.setStudent(student);
        loan.setEquipment(equipment);
        loan.setStartDate(LocalDate.now());
        loan.setDueDate(LocalDate.now().plusDays(7)); // Default 7-day loan period
        loan.setStatus(Loan.Status.ACTIVE);

        // Mark equipment as unavailable
        equipment.markAsUnavailable();
        equipmentRepository.save(equipment);

        return loanRepository.save(loan);
    }

    public Loan returnLoan(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("Loan not found"));

        if (loan.getStatus() != Loan.Status.ACTIVE) {
            throw new IllegalArgumentException("Loan is not active");
        }

        // Set return date and update status
        loan.setReturnDate(LocalDate.now());
        
        if (loan.isOverdue()) {
            loan.setStatus(Loan.Status.OVERDUE);
        } else {
            loan.setStatus(Loan.Status.RETURNED);
        }

        // Mark equipment as available again
        Equipment equipment = loan.getEquipment();
        equipment.markAsAvailable();
        equipmentRepository.save(equipment);

        return loanRepository.save(loan);
    }

    public double calculatePenalty(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("Loan not found"));
        
        return loan.calculatePenalty(penaltyRules);
    }

    public List<Loan> getLoansByStatus(Loan.Status status) {
        return loanRepository.findByStatus(status);
    }

    public Loan extendLoan(Long loanId, int additionalDays) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("Loan not found"));

        if (loan.getStatus() != Loan.Status.ACTIVE) {
            throw new IllegalArgumentException("Can only extend active loans");
        }

        if (additionalDays <= 0 || additionalDays > 7) {
            throw new IllegalArgumentException("Extension must be between 1 and 7 days");
        }

        loan.setDueDate(loan.getDueDate().plusDays(additionalDays));
        return loanRepository.save(loan);
    }

    public void updateOverdueLoans() {
        List<Loan> overdueLoans = loanRepository.findOverdueLoans(LocalDate.now());
        for (Loan loan : overdueLoans) {
            if (loan.getStatus() == Loan.Status.ACTIVE) {
                loan.setStatus(Loan.Status.OVERDUE);
                loanRepository.save(loan);
            }
        }
    }
}
