package tz.ac.suza.wt.studentsapi.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tz.ac.suza.wt.studentsapi.model.Student;
import tz.ac.suza.wt.studentsapi.services.StudentServices;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/v1/students")
public class StudentController {
    private final StudentServices studentServices;

    public StudentController(StudentServices studentServices) {
        this.studentServices = studentServices;
    }

    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = (List<Student>) studentServices.getAllStudents();
        return ResponseEntity.ok(students);

        // @GetMapping("/api/v1/")
        // public String welcome() {
        // return "Welcome to Students API";
        // }
        // @GetMapping("/api/v1/message")
        // public ResponseEntity<Map<String,String>> getMessage(){
        // return ResponseEntity.ok(Map.of("message","Welcome to Students API"));
        // }
    }

    @PostMapping()
    public ResponseEntity<Student> create(@RequestBody Student student) {
        Student savedStudent = studentServices.createStudent(student);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedStudent);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> update(@PathVariable Long id, @RequestBody Student student) {
        return ResponseEntity.ok(studentServices.updateStudent(id, student));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        studentServices.deleteStudent(id);
        return ResponseEntity.noContent().build();

    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        return ResponseEntity.ok(studentServices.getStudentById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Student>> getByCourseAndYear(@RequestParam String course, @RequestParam Integer year) {
        return ResponseEntity.ok(studentServices.getByCourseAndYear(course, year));
    }

}
