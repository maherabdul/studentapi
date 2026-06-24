package tz.ac.suza.wt.studentsapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import tz.ac.suza.wt.studentsapi.model.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findByCourseAndYear(String course, Integer year);

    List<Student> findByCourse(String course);

    List<Student> findByYear(Integer year);

}
