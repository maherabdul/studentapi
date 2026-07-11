package tz.ac.suza.wt.studentsapi.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tz.ac.suza.wt.studentsapi.dto.EnrollmentRequest;
import tz.ac.suza.wt.studentsapi.model.StudentCourse;
import tz.ac.suza.wt.studentsapi.services.StudentCourseServices;

@RestController
@RequestMapping("/api/v1/enrollments")
public class StudentCourseController {
    private final StudentCourseServices studentCourseServices;

    public StudentCourseController(StudentCourseServices studentCourseServices) {
        this.studentCourseServices = studentCourseServices;
    }

    @GetMapping
    public ResponseEntity<List<StudentCourse>> getAllEnrollments() {
        return ResponseEntity.ok(studentCourseServices.getAllEnrollments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentCourse> getEnrollmentById(@PathVariable Long id) {
        return ResponseEntity.ok(studentCourseServices.getEnrollmentById(id));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<StudentCourse>> getEnrollmentsByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(studentCourseServices.getEnrollmentsByStudent(studentId));
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<StudentCourse>> getEnrollmentsByCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(studentCourseServices.getEnrollmentsByCourse(courseId));
    }

    @PostMapping
    public ResponseEntity<StudentCourse> enroll(@RequestBody EnrollmentRequest request) {
        StudentCourse enrollment = studentCourseServices.enroll(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(enrollment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentCourse> update(@PathVariable Long id, @RequestBody EnrollmentRequest request) {
        return ResponseEntity.ok(studentCourseServices.updateEnrollment(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        studentCourseServices.deleteEnrollment(id);
        return ResponseEntity.noContent().build();
    }

}
