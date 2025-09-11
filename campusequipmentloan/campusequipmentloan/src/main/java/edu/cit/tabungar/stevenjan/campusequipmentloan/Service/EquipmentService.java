package edu.cit.tabungar.stevenjan.campusequipmentloan.Service;

import edu.cit.tabungar.stevenjan.campusequipmentloan.Model.Equipment;
import edu.cit.tabungar.stevenjan.campusequipmentloan.Repository.EquipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class EquipmentService {

    private final EquipmentRepository equipmentRepository;

    @Autowired
    public EquipmentService(EquipmentRepository equipmentRepository) {
        this.equipmentRepository = equipmentRepository;
    }

    public List<Equipment> getAllEquipment() {
        return equipmentRepository.findAll();
    }

    public Optional<Equipment> getEquipmentById(Long id) {
        return equipmentRepository.findById(id);
    }

    public List<Equipment> getAvailableEquipment() {
        return equipmentRepository.findByAvailabilityTrue();
    }

    public List<Equipment> getEquipmentByType(String type) {
        return equipmentRepository.findByType(type);
    }

    public List<Equipment> getAvailableEquipmentByType(String type) {
        return equipmentRepository.findAvailableEquipmentByType(type);
    }

    public Optional<Equipment> getEquipmentBySerialNumber(String serialNumber) {
        return equipmentRepository.findBySerialNumber(serialNumber);
    }

    public Equipment createEquipment(Equipment equipment) {
        if (equipmentRepository.existsBySerialNumber(equipment.getSerialNumber())) {
            throw new IllegalArgumentException("Serial number already exists: " + equipment.getSerialNumber());
        }
        return equipmentRepository.save(equipment);
    }

    public Equipment updateEquipment(Long id, Equipment equipmentDetails) {
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Equipment not found with id: " + id));

        // Check if serial number is being changed and if it already exists
        if (!equipment.getSerialNumber().equals(equipmentDetails.getSerialNumber()) &&
            equipmentRepository.existsBySerialNumber(equipmentDetails.getSerialNumber())) {
            throw new IllegalArgumentException("Serial number already exists: " + equipmentDetails.getSerialNumber());
        }

        equipment.setName(equipmentDetails.getName());
        equipment.setType(equipmentDetails.getType());
        equipment.setSerialNumber(equipmentDetails.getSerialNumber());
        equipment.setAvailability(equipmentDetails.getAvailability());

        return equipmentRepository.save(equipment);
    }

    public void deleteEquipment(Long id) {
        if (!equipmentRepository.existsById(id)) {
            throw new IllegalArgumentException("Equipment not found with id: " + id);
        }
        equipmentRepository.deleteById(id);
    }

    public Equipment markAsUnavailable(Long id) {
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Equipment not found with id: " + id));
        equipment.markAsUnavailable();
        return equipmentRepository.save(equipment);
    }

    public Equipment markAsAvailable(Long id) {
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Equipment not found with id: " + id));
        equipment.markAsAvailable();
        return equipmentRepository.save(equipment);
    }

    public long getAvailableEquipmentCount() {
        return equipmentRepository.countAvailableEquipment();
    }
}
