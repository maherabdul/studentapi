# Students API Documentation

A simple REST API for managing students, courses, and enrollments, built with Spring Boot and PostgreSQL.

**Base URL:** `http://localhost:8090/api/v1`

All requests and responses use JSON (`Content-Type: application/json`).

## Data Model

Students and Courses have a **many-to-many** relationship, broken into a join entity **StudentCourse** (exposed as *enrollments*). Breaking the many-to-many lets the enrollment carry its own data (semester, grade).

```
Student 1 ──── * StudentCourse * ──── 1 Course
```

### Student

| Field | Type    | Description                                     |
|-------|---------|-------------------------------------------------|
| id    | Long    | Auto-generated unique identifier                |
| name  | String  | Student name (required, max 100 chars)          |
| email | String  | Email address (required, unique, max 150 chars) |
| year  | Integer | Year of study (required)                        |

### Course

| Field   | Type    | Description                                  |
|---------|---------|----------------------------------------------|
| id      | Long    | Auto-generated unique identifier             |
| code    | String  | Course code (required, unique, max 20 chars) |
| name    | String  | Course name (required, max 200 chars)        |
| credits | Integer | Number of credits (required)                 |

### StudentCourse (Enrollment)

| Field    | Type    | Description                            |
|----------|---------|----------------------------------------|
| id       | Long    | Auto-generated unique identifier       |
| student  | Student | The enrolled student                   |
| course   | Course  | The course enrolled in                 |
| semester | Integer | Semester of enrollment (required)      |
| grade    | String  | Grade, e.g. "A", "B+" (optional)       |

A student can enroll in the same course only once (unique constraint on `student_id` + `course_id`).

---

## Student Endpoints

### Get All Students

```
GET /api/v1/students
```

**Response:** `200 OK`

```json
[
  { "id": 1, "name": "Amina Juma", "email": "amina.juma@suza.ac.tz", "year": 2 }
]
```

### Get Student by ID

```
GET /api/v1/students/{id}
```

**Response:** `200 OK` with the student object.

### Search Students by Year

```
GET /api/v1/students/search?year={year}
```

**Response:** `200 OK` with a list of matching students.

### Create a Student

```
POST /api/v1/students
```

```json
{ "name": "Amina Juma", "email": "amina.juma@suza.ac.tz", "year": 2 }
```

**Response:** `201 Created` with the saved student.

```bash
curl -X POST http://localhost:8090/api/v1/students \
  -H "Content-Type: application/json" \
  -d '{"name":"Amina Juma","email":"amina.juma@suza.ac.tz","year":2}'
```

### Update a Student

```
PUT /api/v1/students/{id}
```

Request body: same shape as Create. **Response:** `200 OK` with the updated student.

### Delete a Student

```
DELETE /api/v1/students/{id}
```

**Response:** `204 No Content`.

---

## Course Endpoints

### Get All Courses

```
GET /api/v1/courses
```

**Response:** `200 OK`

```json
[
  { "id": 1, "code": "CS101", "name": "Introduction to Programming", "credits": 10 }
]
```

### Get Course by ID

```
GET /api/v1/courses/{id}
```

### Get Course by Code

```
GET /api/v1/courses/code/{code}
```

Example: `GET /api/v1/courses/code/CS101`

### Create a Course

```
POST /api/v1/courses
```

```json
{ "code": "CS101", "name": "Introduction to Programming", "credits": 10 }
```

**Response:** `201 Created` with the saved course.

```bash
curl -X POST http://localhost:8090/api/v1/courses \
  -H "Content-Type: application/json" \
  -d '{"code":"CS101","name":"Introduction to Programming","credits":10}'
```

### Update a Course

```
PUT /api/v1/courses/{id}
```

Request body: same shape as Create. **Response:** `200 OK` with the updated course.

### Delete a Course

```
DELETE /api/v1/courses/{id}
```

**Response:** `204 No Content`.

---

## Enrollment Endpoints (StudentCourse)

### Get All Enrollments

```
GET /api/v1/enrollments
```

**Response:** `200 OK`

```json
[
  {
    "id": 1,
    "student": { "id": 1, "name": "Amina Juma", "email": "amina.juma@suza.ac.tz", "year": 2 },
    "course": { "id": 1, "code": "CS101", "name": "Introduction to Programming", "credits": 10 },
    "semester": 1,
    "grade": null
  }
]
```

### Get Enrollment by ID

```
GET /api/v1/enrollments/{id}
```

### Get Enrollments for a Student

```
GET /api/v1/enrollments/student/{studentId}
```

Returns all courses a student is enrolled in.

### Get Enrollments for a Course

```
GET /api/v1/enrollments/course/{courseId}
```

Returns all students enrolled in a course.

### Enroll a Student in a Course

```
POST /api/v1/enrollments
```

```json
{ "studentId": 1, "courseId": 1, "semester": 1 }
```

**Response:** `201 Created` with the enrollment (including nested student and course).

Fails if the student is already enrolled in the course.

```bash
curl -X POST http://localhost:8090/api/v1/enrollments \
  -H "Content-Type: application/json" \
  -d '{"studentId":1,"courseId":1,"semester":1}'
```

### Update an Enrollment (e.g. assign a grade)

```
PUT /api/v1/enrollments/{id}
```

```json
{ "semester": 1, "grade": "A" }
```

**Response:** `200 OK` with the updated enrollment.

### Remove an Enrollment

```
DELETE /api/v1/enrollments/{id}
```

**Response:** `204 No Content`.

---

## Summary

| Method | Endpoint                                  | Description                     | Success Status |
|--------|-------------------------------------------|---------------------------------|----------------|
| GET    | `/api/v1/students`                        | Get all students                | 200 OK         |
| GET    | `/api/v1/students/{id}`                   | Get a student by ID             | 200 OK         |
| GET    | `/api/v1/students/search?year=`           | Search students by year         | 200 OK         |
| POST   | `/api/v1/students`                        | Create a new student            | 201 Created    |
| PUT    | `/api/v1/students/{id}`                   | Update a student                | 200 OK         |
| DELETE | `/api/v1/students/{id}`                   | Delete a student                | 204 No Content |
| GET    | `/api/v1/courses`                         | Get all courses                 | 200 OK         |
| GET    | `/api/v1/courses/{id}`                    | Get a course by ID              | 200 OK         |
| GET    | `/api/v1/courses/code/{code}`             | Get a course by code            | 200 OK         |
| POST   | `/api/v1/courses`                         | Create a new course             | 201 Created    |
| PUT    | `/api/v1/courses/{id}`                    | Update a course                 | 200 OK         |
| DELETE | `/api/v1/courses/{id}`                    | Delete a course                 | 204 No Content |
| GET    | `/api/v1/enrollments`                     | Get all enrollments             | 200 OK         |
| GET    | `/api/v1/enrollments/{id}`                | Get an enrollment by ID         | 200 OK         |
| GET    | `/api/v1/enrollments/student/{studentId}` | Get a student's enrollments     | 200 OK         |
| GET    | `/api/v1/enrollments/course/{courseId}`   | Get a course's enrollments      | 200 OK         |
| POST   | `/api/v1/enrollments`                     | Enroll a student in a course    | 201 Created    |
| PUT    | `/api/v1/enrollments/{id}`                | Update semester/grade           | 200 OK         |
| DELETE | `/api/v1/enrollments/{id}`                | Remove an enrollment            | 204 No Content |
