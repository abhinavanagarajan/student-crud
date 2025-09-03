package com.students;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import com.students.entity.Student;
import com.students.repository.StudentRepository;
import com.students.service.StudentService;

@SpringBootTest
@Import(StudentService.class)   // tells Spring to load your service along with JPA
class StudentServiceTest {

    @Autowired
    private StudentRepository repo;

    @Autowired
    private StudentService service;

    private Long studentId;

    @BeforeEach
    void setUp() {
        Student s = new Student("Old", "old@mail.com", 20);
        studentId = repo.save(s).getId();
    }

    @Test
    void testUpdateStudent() {
        Student updated = new Student("New", "new@mail.com", 22);

        Student result = service.update(studentId, updated);

        assertNotNull(result);
        assertEquals("New", result.getName());
        assertEquals("new@mail.com", result.getEmail());
        assertEquals(22, result.getAge());
    }

    @Test
    void testPatchStudent() {
        Student partial = new Student();
        partial.setName("Partial Name"); //  updating name

        Student result = service.patch(studentId, partial);

        assertNotNull(result);
        assertEquals("Partial Name", result.getName());
        assertEquals("old@mail.com", result.getEmail()); // not unchanged
        assertEquals(20, result.getAge()); // not changed
    }
}
