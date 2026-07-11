package tz.ac.suza.wt.studentsapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import tz.ac.suza.wt.studentsapi.model.StudentCourse;

public interface StudentCourseRepository extends JpaRepository<StudentCourse, Long> {

    List<StudentCourse> findByStudentId(Long studentId);

    List<StudentCourse> findByCourseId(Long courseId);

    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);

}
