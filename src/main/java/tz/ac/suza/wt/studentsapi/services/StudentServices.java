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
   

}
