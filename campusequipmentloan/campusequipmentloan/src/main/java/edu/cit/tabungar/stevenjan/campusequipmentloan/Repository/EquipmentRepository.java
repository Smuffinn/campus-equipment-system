package edu.cit.tabungar.stevenjan.campusequipmentloan.Repository;

import edu.cit.tabungar.stevenjan.campusequipmentloan.Model.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
    
    List<Equipment> findByAvailabilityTrue();
    
    List<Equipment> findByType(String type);
    
    Optional<Equipment> findBySerialNumber(String serialNumber);
    
    boolean existsBySerialNumber(String serialNumber);
    
    @Query("SELECT e FROM Equipment e WHERE e.availability = true AND e.type = ?1")
    List<Equipment> findAvailableEquipmentByType(String type);
    
    @Query("SELECT COUNT(e) FROM Equipment e WHERE e.availability = true")
    long countAvailableEquipment();
}
