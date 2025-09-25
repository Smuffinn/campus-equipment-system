package edu.cit.tabungar.stevenjan.campusequipmentloan.Controller;

import edu.cit.tabungar.stevenjan.campusequipmentloan.Model.Equipment;
import edu.cit.tabungar.stevenjan.campusequipmentloan.Service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/equipment")
public class EquipmentController {

    private final EquipmentService equipmentService;

    @Autowired
    public EquipmentController(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }

    // GET /api/equipment → get all equipment
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Equipment>> getAllEquipment() {
        List<Equipment> equipment = equipmentService.getAllEquipment();
        return ResponseEntity.ok(equipment);
    }

    // GET /api/equipment/{id} → get equipment by id
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Equipment> getEquipmentById(@PathVariable Long id) {
        Optional<Equipment> equipment = equipmentService.getEquipmentById(id);
        return equipment.map(ResponseEntity::ok)
                       .orElse(ResponseEntity.notFound().build());
    }

    // GET /api/equipment/available → get available equipment
    @GetMapping("/available")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Equipment>> getAvailableEquipment() {
        List<Equipment> availableEquipment = equipmentService.getAvailableEquipment();
        return ResponseEntity.ok(availableEquipment);
    }

    // GET /api/equipment/type/{type} → get equipment by type
    @GetMapping("/type/{type}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Equipment>> getEquipmentByType(@PathVariable String type) {
        List<Equipment> equipment = equipmentService.getEquipmentByType(type);
        return ResponseEntity.ok(equipment);
    }

    // GET /api/equipment/available/type/{type} → get available equipment by type
    @GetMapping("/available/type/{type}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Equipment>> getAvailableEquipmentByType(@PathVariable String type) {
        List<Equipment> equipment = equipmentService.getAvailableEquipmentByType(type);
        return ResponseEntity.ok(equipment);
    }

    // GET /api/equipment/serial/{serialNumber} → get equipment by serial number
    @GetMapping("/serial/{serialNumber}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Equipment> getEquipmentBySerialNumber(@PathVariable String serialNumber) {
        Optional<Equipment> equipment = equipmentService.getEquipmentBySerialNumber(serialNumber);
        return equipment.map(ResponseEntity::ok)
                       .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/equipment → create new equipment
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Equipment> createEquipment(@Valid @RequestBody Equipment equipment) {
        try {
            Equipment createdEquipment = equipmentService.createEquipment(equipment);
            return ResponseEntity.ok(createdEquipment);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // POST /api/equipment/{id}/mark-unavailable → mark equipment as unavailable
    @PostMapping("/{id}/mark-unavailable")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Equipment> markAsUnavailable(@PathVariable Long id) {
        try {
            Equipment equipment = equipmentService.markAsUnavailable(id);
            return ResponseEntity.ok(equipment);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // POST /api/equipment/{id}/mark-available → mark equipment as available
    @PostMapping("/{id}/mark-available")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Equipment> markAsAvailable(@PathVariable Long id) {
        try {
            Equipment equipment = equipmentService.markAsAvailable(id);
            return ResponseEntity.ok(equipment);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // GET /api/equipment/available/count → get count of available equipment
    @GetMapping("/available/count")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Long> getAvailableEquipmentCount() {
        long count = equipmentService.getAvailableEquipmentCount();
        return ResponseEntity.ok(count);
    }
}
