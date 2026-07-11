package tz.ac.suza.wt.studentsapi.dto;

public record EnrollmentRequest(Long studentId, Long courseId, Integer semester, String grade) {
}
