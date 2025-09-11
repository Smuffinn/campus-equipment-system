package edu.cit.tabungar.stevenjan.campusequipmentloan.Service;

import edu.cit.tabungar.stevenjan.campusequipmentloan.Repository.StudentRepository;
import edu.cit.tabungar.stevenjan.campusequipmentloan.Model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    public Optional<Student> getStudentByStudentNo(String studentNo) {
        return studentRepository.findByStudentNo(studentNo);
    }

    public Optional<Student> getStudentByEmail(String email) {
        return studentRepository.findByEmail(email);
    }

    public Student createStudent(Student student) {
        if (studentRepository.existsByStudentNo(student.getStudentNo())) {
            throw new IllegalArgumentException("Student number already exists: " + student.getStudentNo());
        }
        if (studentRepository.existsByEmail(student.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + student.getEmail());
        }
        return studentRepository.save(student);
    }

    public Student updateStudent(Long id, Student studentDetails) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with id: " + id));

        // Check if student number is being changed and if it already exists
        if (!student.getStudentNo().equals(studentDetails.getStudentNo()) &&
            studentRepository.existsByStudentNo(studentDetails.getStudentNo())) {
            throw new IllegalArgumentException("Student number already exists: " + studentDetails.getStudentNo());
        }

        // Check if email is being changed and if it already exists
        if (!student.getEmail().equals(studentDetails.getEmail()) &&
            studentRepository.existsByEmail(studentDetails.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + studentDetails.getEmail());
        }

        student.setStudentNo(studentDetails.getStudentNo());
        student.setName(studentDetails.getName());
        student.setEmail(studentDetails.getEmail());

        return studentRepository.save(student);
    }

    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new IllegalArgumentException("Student not found with id: " + id);
        }
        studentRepository.deleteById(id);
    }
}
