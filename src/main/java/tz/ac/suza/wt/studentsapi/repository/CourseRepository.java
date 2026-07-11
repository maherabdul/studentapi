package tz.ac.suza.wt.studentsapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import tz.ac.suza.wt.studentsapi.model.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {

    Optional<Course> findByCode(String code);

}
