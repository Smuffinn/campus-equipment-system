package edu.cit.tabungar.stevenjan.campusequipmentloan.Repository;

import edu.cit.tabungar.stevenjan.campusequipmentloan.Model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    
    List<Loan> findByStudentId(Long studentId);
    
    List<Loan> findByEquipmentId(Long equipmentId);
    
    List<Loan> findByStatus(Loan.Status status);
    
    @Query("SELECT l FROM Loan l WHERE l.status = 'ACTIVE' AND l.dueDate < ?1")
    List<Loan> findOverdueLoans(LocalDate currentDate);
    
    @Query("SELECT l FROM Loan l WHERE l.student.id = ?1 AND l.status = 'ACTIVE'")
    List<Loan> findActiveLoansForStudent(Long studentId);
    
    @Query("SELECT l FROM Loan l WHERE l.equipment.id = ?1 AND l.status = 'ACTIVE'")
    List<Loan> findActiveLoanForEquipment(Long equipmentId);
    
    @Query("SELECT COUNT(l) FROM Loan l WHERE l.student.id = ?1 AND l.status = 'ACTIVE'")
    long countActiveLoansForStudent(Long studentId);
}
