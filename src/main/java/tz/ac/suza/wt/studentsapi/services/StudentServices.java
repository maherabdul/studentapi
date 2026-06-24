package tz.ac.suza.wt.studentsapi.services;

import java.util.List;

import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import tz.ac.suza.wt.studentsapi.model.Student;
import tz.ac.suza.wt.studentsapi.repository.StudentRepository;

@Service
public class StudentServices {
    private final StudentRepository studentRepository;

    public StudentServices(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student createStudent(Student student) {
        student.setId(null);
        return studentRepository.save(student);
    }

    public Student updateStudent(Long id, Student student) {
        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
        existingStudent.setName(student.getName());
        existingStudent.setEmail(student.getEmail());
        existingStudent.setCourse(student.getCourse());
        existingStudent.setYear(student.getYear());
        return studentRepository.save(existingStudent);

    }

    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new RuntimeException("Student not found with id: " + id);
        }
        studentRepository.deleteById(id);
    }

    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
    }

    public List<Student> getByCourseAndYear(String course, Integer year) {
        return studentRepository.findByCourseAndYear(course, year);
    }

}
