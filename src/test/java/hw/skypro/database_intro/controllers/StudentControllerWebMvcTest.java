package hw.skypro.database_intro.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import hw.skypro.database_intro.models.Student;
import hw.skypro.database_intro.service.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
class StudentControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private StudentService studentService;

    @Test
    void createStudentTest() throws Exception {
        Student student = new Student();
        student.setId(1L);
        student.setName("Harry Potter");
        student.setAge(17);

        when(studentService.createStudent(any(Student.class)))
                .thenReturn(student);

        mockMvc.perform(post("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Harry Potter"))
                .andExpect(jsonPath("$.age").value(17));
    }

    @Test
    void getStudentTest() throws Exception {
        Student student = new Student();
        student.setId(1L);
        student.setName("Hermione Granger");
        student.setAge(18);

        when(studentService.findStudent(1L))
                .thenReturn(student);

        mockMvc.perform(get("/students/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Hermione Granger"))
                .andExpect(jsonPath("$.age").value(18));
    }

    @Test
    void getAllStudentsTest() throws Exception {
        Student firstStudent = new Student();
        firstStudent.setId(1L);
        firstStudent.setName("Harry Potter");
        firstStudent.setAge(17);

        Student secondStudent = new Student();
        secondStudent.setId(2L);
        secondStudent.setName("Ron Weasley");
        secondStudent.setAge(17);

        when(studentService.getAllStudents())
                .thenReturn(List.of(firstStudent, secondStudent));

        mockMvc.perform(get("/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Harry Potter"))
                .andExpect(jsonPath("$[1].name").value("Ron Weasley"));
    }

    @Test
    void updateStudentTest() throws Exception {
        Student student = new Student();
        student.setId(1L);
        student.setName("Updated Harry");
        student.setAge(18);

        when(studentService.editStudent(any(Student.class)))
                .thenReturn(student);

        mockMvc.perform(put("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Updated Harry"))
                .andExpect(jsonPath("$.age").value(18));
    }

    @Test
    void deleteStudentTest() throws Exception {
        mockMvc.perform(delete("/students/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getStudentsByAgeTest() throws Exception {
        Student student = new Student();
        student.setId(1L);
        student.setName("Harry Potter");
        student.setAge(17);

        when(studentService.findByAge(17))
                .thenReturn(List.of(student));

        mockMvc.perform(get("/students/age/17"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].age").value(17));
    }

    @Test
    void getStudentsByAgeBetweenTest() throws Exception {
        Student student = new Student();
        student.setId(1L);
        student.setName("Harry Potter");
        student.setAge(17);

        when(studentService.findByAgeBetween(16, 18))
                .thenReturn(List.of(student));

        mockMvc.perform(get("/students/age")
                        .param("min", "16")
                        .param("max", "18"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Harry Potter"))
                .andExpect(jsonPath("$[0].age").value(17));
    }

    @Test
    void getStudentNotFoundTest() throws Exception {
        when(studentService.findStudent(999L))
                .thenReturn(null);

        mockMvc.perform(get("/students/999"))
                .andExpect(status().isNotFound());
    }
}