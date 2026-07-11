package tz.ac.suza.wt.studentsapi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;

// Join entity: breaks the Student <-> Course many-to-many into two one-to-many
// relationships, so the enrollment itself can carry data (semester, grade).
@Entity
@Data
@Table(name = "student_courses", uniqueConstraints = @UniqueConstraint(columnNames = { "student_id", "course_id" }))
public class StudentCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(nullable = false)
    private Integer semester;

    @Column(length = 5)
    private String grade;

}



// select sc.*,s.*,c.*
// from student_courses sc 
// join students s ON s.id=sc.student_id 
// join courses c ON c.id=sc.course_id where s.id=1;
