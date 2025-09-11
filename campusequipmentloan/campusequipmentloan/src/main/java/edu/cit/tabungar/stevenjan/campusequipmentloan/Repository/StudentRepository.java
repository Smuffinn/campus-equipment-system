package edu.cit.tabungar.stevenjan.campusequipmentloan.Repository;

import edu.cit.tabungar.stevenjan.campusequipmentloan.Model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    
    Optional<Student> findByStudentNo(String studentNo);
    
    Optional<Student> findByEmail(String email);
    
    boolean existsByStudentNo(String studentNo);
    
    boolean existsByEmail(String email);
}
