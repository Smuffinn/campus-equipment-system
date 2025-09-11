package edu.cit.tabungar.stevenjan.campusequipmentloan.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;

@Entity
public class Equipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Equipment name is required")
    private String name;

    @NotBlank(message = "Equipment type is required")
    private String type;

    @NotBlank(message = "Serial number is required")
    @Column(unique = true)
    private String serialNumber;

    @NotNull(message = "Availability status is required")
    private Boolean availability = true;

    // Constructors
    public Equipment() {}

    public Equipment(String name, String type, String serialNumber) {
        this.name = name;
        this.type = type;
        this.serialNumber = serialNumber;
        this.availability = true;
    }

    public Equipment(String name, String type, String serialNumber, Boolean availability) {
        this.name = name;
        this.type = type;
        this.serialNumber = serialNumber;
        this.availability = availability;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Boolean getAvailability() {
        return availability;
    }

    public void setAvailability(Boolean availability) {
        this.availability = availability;
    }

    // Convenience methods
    public boolean isAvailable() {
        return availability != null && availability;
    }

    public void markAsUnavailable() {
        this.availability = false;
    }

    public void markAsAvailable() {
        this.availability = true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Equipment)) return false;
        Equipment equipment = (Equipment) o;
        return Objects.equals(id, equipment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Equipment{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", serialNumber='" + serialNumber + '\'' +
                ", availability=" + availability +
                '}';
    }
}
