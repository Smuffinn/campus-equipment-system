package edu.cit.tabungar.stevenjan.campusequipmentloan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.CommandLineRunner;

import edu.cit.tabungar.stevenjan.campusequipmentloan.Model.Student;
import edu.cit.tabungar.stevenjan.campusequipmentloan.Model.Equipment;
import edu.cit.tabungar.stevenjan.campusequipmentloan.Repository.StudentRepository;
import edu.cit.tabungar.stevenjan.campusequipmentloan.Repository.EquipmentRepository;

@SpringBootApplication
public class CampusequipmentloanApplication {

    public static void main(String[] args) {
        SpringApplication.run(CampusequipmentloanApplication.class, args);
    }


    @Bean
    CommandLineRunner seedData(StudentRepository students, EquipmentRepository equipmentRepo) {
        return args -> {
            // Seed student if not present
            if (!students.existsByStudentNo("S0001")) {
                Student s1 = new Student("S0001", "John Doe", "john.doe@example.com");
                students.save(s1);
            }

            if (!students.existsByStudentNo("S0002")) {
                Student s2 = new Student("S0002", "Jane Smith", "jane.smith@example.com");
                students.save(s2);
            }

            // Seed equipment if not present (unique by serialNumber)
            if (!equipmentRepo.existsBySerialNumber("SN-0001")) {
                Equipment e1 = new Equipment("Canon DSLR", "Camera", "SN-0001");
                equipmentRepo.save(e1);
            }

            if (!equipmentRepo.existsBySerialNumber("SN-0002")) {
                Equipment e2 = new Equipment("ThinkPad T14", "Laptop", "SN-0002");
                equipmentRepo.save(e2);
            }
        };
    }
}
