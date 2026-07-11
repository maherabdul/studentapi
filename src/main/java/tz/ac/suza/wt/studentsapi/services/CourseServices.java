package tz.ac.suza.wt.studentsapi.services;

import java.util.List;

import org.springframework.stereotype.Service;

import tz.ac.suza.wt.studentsapi.model.Course;
import tz.ac.suza.wt.studentsapi.repository.CourseRepository;

@Service
public class CourseServices {
    private final CourseRepository courseRepository;

    public CourseServices(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course getCourseById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));
    }

    public Course getCourseByCode(String code) {
        return courseRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Course not found with code: " + code));
    }

    public Course createCourse(Course course) {
        course.setId(null);
        return courseRepository.save(course);
    }

    public Course updateCourse(Long id, Course course) {
        Course existingCourse = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));
        existingCourse.setCode(course.getCode());
        existingCourse.setName(course.getName());
        existingCourse.setCredits(course.getCredits());
        return courseRepository.save(existingCourse);
    }

    public void deleteCourse(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new RuntimeException("Course not found with id: " + id);
        }
        courseRepository.deleteById(id);
    }

}
