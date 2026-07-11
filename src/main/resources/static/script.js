// ============================================================
// Students API - fetch() CRUD client
//
// Each resource (students, courses, enrollments) follows the
// same pattern:
//   GET    /api/v1/{resource}        -> load all
//   POST   /api/v1/{resource}        -> create (JSON body)
//   PUT    /api/v1/{resource}/{id}   -> update (JSON body)
//   DELETE /api/v1/{resource}/{id}   -> delete
// ============================================================

const API_BASE = '/api/v1';

// ---------- Generic fetch helper ----------
// Wraps fetch() so every call gets JSON headers and error handling.
async function api(path, options = {}) {
    const response = await fetch(API_BASE + path, {
        headers: { 'Content-Type': 'application/json' },
        ...options,
    });

    if (!response.ok) {
        // Try to read the server's error message, fall back to status text
        let detail = response.statusText;
        try {
            const body = await response.json();
            detail = body.message || body.error || detail;
        } catch (_) { /* body was not JSON */ }
        throw new Error(`${response.status}: ${detail}`);
    }

    // DELETE returns 204 No Content -> nothing to parse
    if (response.status === 204) return null;
    return response.json();
}

// ---------- User feedback ----------
const messageEl = document.getElementById('message');
let messageTimer;

function showMessage(text, type = 'success') {
    messageEl.textContent = text;
    messageEl.className = `message ${type}`;
    clearTimeout(messageTimer);
    messageTimer = setTimeout(() => messageEl.classList.add('hidden'), 4000);
}

// Escape user data before inserting it into HTML (prevents XSS)
function esc(value) {
    const div = document.createElement('div');
    div.textContent = value ?? '';
    return div.innerHTML;
}

// ---------- Tab switching ----------
document.querySelectorAll('.tab-btn').forEach((btn) => {
    btn.addEventListener('click', () => {
        document.querySelectorAll('.tab-btn').forEach((b) => b.classList.remove('active'));
        document.querySelectorAll('.tab-panel').forEach((p) => p.classList.add('hidden'));
        btn.classList.add('active');
        document.getElementById(btn.dataset.tab).classList.remove('hidden');

        if (btn.dataset.tab === 'students') loadStudents();
        if (btn.dataset.tab === 'courses') loadCourses();
        if (btn.dataset.tab === 'enrollments') loadEnrollmentsTab();
    });
});

// ============================================================
// STUDENTS
// ============================================================
const studentForm = document.getElementById('student-form');

async function loadStudents(year = null) {
    const tbody = document.getElementById('students-tbody');
    try {
        const students = year
            ? await api(`/students/search?year=${year}`)
            : await api('/students');

        if (students.length === 0) {
            tbody.innerHTML = '<tr class="empty-row"><td colspan="5">No students found</td></tr>';
            return;
        }

        tbody.innerHTML = students.map((s) => `
            <tr>
                <td>${s.id}</td>
                <td>${esc(s.name)}</td>
                <td>${esc(s.email)}</td>
                <td>${s.year}</td>
                <td>
                    <button class="btn small" onclick="editStudent(${s.id})">Edit</button>
                    <button class="btn small danger" onclick="deleteStudent(${s.id})">Delete</button>
                </td>
            </tr>`).join('');
    } catch (err) {
        showMessage(`Failed to load students - ${err.message}`, 'error');
    }
}

studentForm.addEventListener('submit', async (event) => {
    event.preventDefault(); // stop the browser's default page reload

    const id = document.getElementById('student-id').value;
    const student = {
        name: document.getElementById('student-name').value.trim(),
        email: document.getElementById('student-email').value.trim(),
        year: Number(document.getElementById('student-year').value),
    };

    try {
        if (id) {
            await api(`/students/${id}`, { method: 'PUT', body: JSON.stringify(student) });
            showMessage('Student updated');
        } else {
            await api('/students', { method: 'POST', body: JSON.stringify(student) });
            showMessage('Student created');
        }
        resetStudentForm();
        loadStudents();
    } catch (err) {
        showMessage(`Save failed - ${err.message}`, 'error');
    }
});

async function editStudent(id) {
    try {
        const s = await api(`/students/${id}`);
        document.getElementById('student-id').value = s.id;
        document.getElementById('student-name').value = s.name;
        document.getElementById('student-email').value = s.email;
        document.getElementById('student-year').value = s.year;
        document.getElementById('student-form-title').textContent = `Edit Student #${s.id}`;
    } catch (err) {
        showMessage(`Could not load student - ${err.message}`, 'error');
    }
}

async function deleteStudent(id) {
    if (!confirm(`Delete student #${id}?`)) return;
    try {
        await api(`/students/${id}`, { method: 'DELETE' });
        showMessage('Student deleted');
        loadStudents();
    } catch (err) {
        showMessage(`Delete failed - ${err.message}`, 'error');
    }
}

function resetStudentForm() {
    studentForm.reset();
    document.getElementById('student-id').value = '';
    document.getElementById('student-form-title').textContent = 'Add Student';
}

document.getElementById('student-cancel').addEventListener('click', resetStudentForm);

// Search by year (uses /students/search?year=N)
document.getElementById('student-search-btn').addEventListener('click', () => {
    const year = document.getElementById('student-search-year').value;
    if (year) loadStudents(year);
});
document.getElementById('student-search-reset').addEventListener('click', () => {
    document.getElementById('student-search-year').value = '';
    loadStudents();
});

// ============================================================
// COURSES
// ============================================================
const courseForm = document.getElementById('course-form');

async function loadCourses() {
    const tbody = document.getElementById('courses-tbody');
    try {
        const courses = await api('/courses');

        if (courses.length === 0) {
            tbody.innerHTML = '<tr class="empty-row"><td colspan="5">No courses found</td></tr>';
            return;
        }

        tbody.innerHTML = courses.map((c) => `
            <tr>
                <td>${c.id}</td>
                <td>${esc(c.code)}</td>
                <td>${esc(c.name)}</td>
                <td>${c.credits}</td>
                <td>
                    <button class="btn small" onclick="editCourse(${c.id})">Edit</button>
                    <button class="btn small danger" onclick="deleteCourse(${c.id})">Delete</button>
                </td>
            </tr>`).join('');
    } catch (err) {
        showMessage(`Failed to load courses - ${err.message}`, 'error');
    }
}

courseForm.addEventListener('submit', async (event) => {
    event.preventDefault();

    const id = document.getElementById('course-id').value;
    const course = {
        code: document.getElementById('course-code').value.trim(),
        name: document.getElementById('course-name').value.trim(),
        credits: Number(document.getElementById('course-credits').value),
    };

    try {
        if (id) {
            await api(`/courses/${id}`, { method: 'PUT', body: JSON.stringify(course) });
            showMessage('Course updated');
        } else {
            await api('/courses', { method: 'POST', body: JSON.stringify(course) });
            showMessage('Course created');
        }
        resetCourseForm();
        loadCourses();
    } catch (err) {
        showMessage(`Save failed - ${err.message}`, 'error');
    }
});

async function editCourse(id) {
    try {
        const c = await api(`/courses/${id}`);
        document.getElementById('course-id').value = c.id;
        document.getElementById('course-code').value = c.code;
        document.getElementById('course-name').value = c.name;
        document.getElementById('course-credits').value = c.credits;
        document.getElementById('course-form-title').textContent = `Edit Course #${c.id}`;
    } catch (err) {
        showMessage(`Could not load course - ${err.message}`, 'error');
    }
}

async function deleteCourse(id) {
    if (!confirm(`Delete course #${id}?`)) return;
    try {
        await api(`/courses/${id}`, { method: 'DELETE' });
        showMessage('Course deleted');
        loadCourses();
    } catch (err) {
        showMessage(`Delete failed - ${err.message}`, 'error');
    }
}

function resetCourseForm() {
    courseForm.reset();
    document.getElementById('course-id').value = '';
    document.getElementById('course-form-title').textContent = 'Add Course';
}

document.getElementById('course-cancel').addEventListener('click', resetCourseForm);

// ============================================================
// ENROLLMENTS
// ============================================================
const enrollmentForm = document.getElementById('enrollment-form');

// The enrollment form needs dropdowns of existing students and courses,
// so we load all three lists together when the tab opens.
async function loadEnrollmentsTab() {
    try {
        // Promise.all runs the three requests in parallel
        const [students, courses] = await Promise.all([
            api('/students'),
            api('/courses'),
        ]);

        document.getElementById('enrollment-student').innerHTML =
            '<option value="">-- select student --</option>' +
            students.map((s) => `<option value="${s.id}">${esc(s.name)} (#${s.id})</option>`).join('');

        document.getElementById('enrollment-course').innerHTML =
            '<option value="">-- select course --</option>' +
            courses.map((c) => `<option value="${c.id}">${esc(c.code)} - ${esc(c.name)}</option>`).join('');

        await loadEnrollments();
    } catch (err) {
        showMessage(`Failed to load data - ${err.message}`, 'error');
    }
}

async function loadEnrollments() {
    const tbody = document.getElementById('enrollments-tbody');
    try {
        const enrollments = await api('/enrollments');

        if (enrollments.length === 0) {
            tbody.innerHTML = '<tr class="empty-row"><td colspan="6">No enrollments found</td></tr>';
            return;
        }

        tbody.innerHTML = enrollments.map((e) => `
            <tr>
                <td>${e.id}</td>
                <td>${esc(e.student?.name)}</td>
                <td>${esc(e.course?.code)}</td>
                <td>${e.semester}</td>
                <td>${esc(e.grade) || '-'}</td>
                <td>
                    <button class="btn small" onclick="editEnrollment(${e.id})">Edit</button>
                    <button class="btn small danger" onclick="deleteEnrollment(${e.id})">Delete</button>
                </td>
            </tr>`).join('');
    } catch (err) {
        showMessage(`Failed to load enrollments - ${err.message}`, 'error');
    }
}

enrollmentForm.addEventListener('submit', async (event) => {
    event.preventDefault();

    const id = document.getElementById('enrollment-id').value;
    // Matches the EnrollmentRequest record on the server:
    // { studentId, courseId, semester, grade }
    const request = {
        studentId: Number(document.getElementById('enrollment-student').value),
        courseId: Number(document.getElementById('enrollment-course').value),
        semester: Number(document.getElementById('enrollment-semester').value),
        grade: document.getElementById('enrollment-grade').value.trim() || null,
    };

    try {
        if (id) {
            await api(`/enrollments/${id}`, { method: 'PUT', body: JSON.stringify(request) });
            showMessage('Enrollment updated');
        } else {
            await api('/enrollments', { method: 'POST', body: JSON.stringify(request) });
            showMessage('Student enrolled');
        }
        resetEnrollmentForm();
        loadEnrollments();
    } catch (err) {
        showMessage(`Save failed - ${err.message}`, 'error');
    }
});

async function editEnrollment(id) {
    try {
        const e = await api(`/enrollments/${id}`);
        document.getElementById('enrollment-id').value = e.id;
        document.getElementById('enrollment-student').value = e.student.id;
        document.getElementById('enrollment-course').value = e.course.id;
        document.getElementById('enrollment-semester').value = e.semester;
        document.getElementById('enrollment-grade').value = e.grade ?? '';
        document.getElementById('enrollment-form-title').textContent = `Edit Enrollment #${e.id}`;
    } catch (err) {
        showMessage(`Could not load enrollment - ${err.message}`, 'error');
    }
}

async function deleteEnrollment(id) {
    if (!confirm(`Delete enrollment #${id}?`)) return;
    try {
        await api(`/enrollments/${id}`, { method: 'DELETE' });
        showMessage('Enrollment deleted');
        loadEnrollments();
    } catch (err) {
        showMessage(`Delete failed - ${err.message}`, 'error');
    }
}

function resetEnrollmentForm() {
    enrollmentForm.reset();
    document.getElementById('enrollment-id').value = '';
    document.getElementById('enrollment-form-title').textContent = 'Enroll Student';
}

document.getElementById('enrollment-cancel').addEventListener('click', resetEnrollmentForm);

// ---------- Initial load ----------
loadStudents();
