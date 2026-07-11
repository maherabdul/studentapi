package tz.ac.suza.wt.studentsapi.services;

import java.util.List;

import org.springframework.stereotype.Service;

import tz.ac.suza.wt.studentsapi.dto.EnrollmentRequest;
import tz.ac.suza.wt.studentsapi.model.Course;
import tz.ac.suza.wt.studentsapi.model.Student;
import tz.ac.suza.wt.studentsapi.model.StudentCourse;
import tz.ac.suza.wt.studentsapi.repository.CourseRepository;
import tz.ac.suza.wt.studentsapi.repository.StudentCourseRepository;
import tz.ac.suza.wt.studentsapi.repository.StudentRepository;

@Service
public class StudentCourseServices {
    private final StudentCourseRepository studentCourseRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public StudentCourseServices(StudentCourseRepository studentCourseRepository,
            StudentRepository studentRepository, CourseRepository courseRepository) {
        this.studentCourseRepository = studentCourseRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    public List<StudentCourse> getAllEnrollments() {
        return studentCourseRepository.findAll();
    }

    public StudentCourse getEnrollmentById(Long id) {
        return studentCourseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Enrollment not found with id: " + id));
    }

    public List<StudentCourse> getEnrollmentsByStudent(Long studentId) {
        return studentCourseRepository.findByStudentId(studentId);
    }

    public List<StudentCourse> getEnrollmentsByCourse(Long courseId) {
        return studentCourseRepository.findByCourseId(courseId);
    }

    public StudentCourse enroll(EnrollmentRequest request) {
        Student student = studentRepository.findById(request.studentId())
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + request.studentId()));
        Course course = courseRepository.findById(request.courseId())
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + request.courseId()));
        if (studentCourseRepository.existsByStudentIdAndCourseId(request.studentId(), request.courseId())) {
            throw new RuntimeException("Student " + request.studentId()
                    + " is already enrolled in course " + request.courseId());
        }
        StudentCourse enrollment = new StudentCourse();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setSemester(request.semester());
        enrollment.setGrade(request.grade());
        return studentCourseRepository.save(enrollment);
    }

    public StudentCourse updateEnrollment(Long id, EnrollmentRequest request) {
        StudentCourse enrollment = studentCourseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Enrollment not found with id: " + id));
        if (request.semester() != null) {
            enrollment.setSemester(request.semester());
        }
        enrollment.setGrade(request.grade());
        return studentCourseRepository.save(enrollment);
    }

    public void deleteEnrollment(Long id) {
        if (!studentCourseRepository.existsById(id)) {
            throw new RuntimeException("Enrollment not found with id: " + id);
        }
        studentCourseRepository.deleteById(id);
    }

}
