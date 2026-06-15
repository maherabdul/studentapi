package tz.ac.suza.wt.studentsapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import tz.ac.suza.wt.studentsapi.model.Student;

public interface StudentRepository extends JpaRepository<Student, Long>  {
    
}
