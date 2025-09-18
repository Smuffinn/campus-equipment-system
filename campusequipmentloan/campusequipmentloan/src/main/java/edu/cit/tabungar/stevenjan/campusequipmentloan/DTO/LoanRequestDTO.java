package edu.cit.tabungar.stevenjan.campusequipmentloan.DTO;

import jakarta.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Map;


public class LoanRequestDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID of the student who will borrow the equipment", example = "1", required = true)
    @NotNull(message = "Student ID is required")
    private Long studentId;

    @Schema(description = "ID of the equipment to be borrowed", example = "1", required = true)
    @NotNull(message = "Equipment ID is required")
    private Long equipmentId;

    // Constructors
    public LoanRequestDTO() {}

    public LoanRequestDTO(Long studentId, Long equipmentId) {
        this.studentId = studentId;
        this.equipmentId = equipmentId;
    }

    // Getters and setters
    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(Long equipmentId) {
        this.equipmentId = equipmentId;
    }

    // Allow nested object input: { "student": { "id": 1 } }
    @JsonProperty("student")
    public void setStudentFromObject(Map<String, Object> student) {
        if (student != null && student.get("id") != null) {
            Object idVal = student.get("id");
            if (idVal instanceof Number) {
                this.studentId = ((Number) idVal).longValue();
            } else {
                try {
                    this.studentId = Long.parseLong(idVal.toString());
                } catch (NumberFormatException ignored) { }
            }
        }
    }

    // Allow nested object input: { "equipment": { "id": 1 } }
    @JsonProperty("equipment")
    public void setEquipmentFromObject(Map<String, Object> equipment) {
        if (equipment != null && equipment.get("id") != null) {
            Object idVal = equipment.get("id");
            if (idVal instanceof Number) {
                this.equipmentId = ((Number) idVal).longValue();
            } else {
                try {
                    this.equipmentId = Long.parseLong(idVal.toString());
                } catch (NumberFormatException ignored) { }
            }
        }
    }

    @Override
    public String toString() {
        return "LoanRequestDTO{" +
                "studentId=" + studentId +
                ", equipmentId=" + equipmentId +
                '}';
    }
}
