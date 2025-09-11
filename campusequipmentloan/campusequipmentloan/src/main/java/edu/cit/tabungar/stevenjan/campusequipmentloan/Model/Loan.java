package edu.cit.tabungar.stevenjan.campusequipmentloan.Model;

import edu.cit.tabungar.stevenjan.campusequipmentloan.Repository.Rules;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;
import java.time.LocalDate;

@Entity
public class Loan {

    public enum Status {
        ACTIVE,
        RETURNED,
        OVERDUE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "equipment_id")
    @NotNull(message = "Equipment is required")
    private Equipment equipment;

    @ManyToOne
    @JoinColumn(name = "student_id")
    @NotNull(message = "Student is required")
    private Student student;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;
    
    @NotNull(message = "Due date is required")
    private LocalDate dueDate;
    
    private LocalDate returnDate;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Status is required")
    private Status status;

    // Getters and setters...

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }


    public boolean isOverdue() {
        return (returnDate == null) && LocalDate.now().isAfter(dueDate);
    }

    // Calculate penalty using strategy
    public double calculatePenalty(Rules rules) {
        return rules.calculatePenalty(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Loan)) return false;
        Loan that = (Loan) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
