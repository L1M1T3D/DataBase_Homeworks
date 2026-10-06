package hw.skypro.database_intro.controllers;

import hw.skypro.database_intro.models.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
class StudentControllerRestTemplateTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void contextLoads() {
        assertThat(restTemplate).isNotNull();
    }

    @Test
    void createStudentTest() {
        Student student = new Student();
        student.setName("Harry Potter");
        student.setAge(17);

        ResponseEntity<Student> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/students",
                student,
                Student.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Harry Potter");
        assertThat(response.getBody().getAge()).isEqualTo(17);
    }

    @Test
    void getStudentTest() {
        Student student = new Student();
        student.setName("Hermione Granger");
        student.setAge(18);

        Student createdStudent = restTemplate.postForObject(
                "http://localhost:" + port + "/students",
                student,
                Student.class
        );

        ResponseEntity<Student> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/students/" + createdStudent.getId(),
                Student.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Hermione Granger");
        assertThat(response.getBody().getAge()).isEqualTo(18);
    }

    @Test
    void getAllStudentsTest() {
        ResponseEntity<Student[]> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/students",
                Student[].class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void updateStudentTest() {
        Student student = new Student();
        student.setName("Ron");
        student.setAge(17);

        Student createdStudent = restTemplate.postForObject(
                "http://localhost:" + port + "/students",
                student,
                Student.class
        );

        createdStudent.setName("Ron Weasley");
        createdStudent.setAge(18);

        restTemplate.put(
                "http://localhost:" + port + "/students",
                createdStudent
        );

        ResponseEntity<Student> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/students/" + createdStudent.getId(),
                Student.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Ron Weasley");
        assertThat(response.getBody().getAge()).isEqualTo(18);
    }

    @Test
    void deleteStudentTest() {
        Student student = new Student();
        student.setName("Draco Malfoy");
        student.setAge(17);

        Student createdStudent = restTemplate.postForObject(
                "http://localhost:" + port + "/students",
                student,
                Student.class
        );

        restTemplate.delete(
                "http://localhost:" + port + "/students/" + createdStudent.getId()
        );

        ResponseEntity<Student> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/students/" + createdStudent.getId(),
                Student.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void getStudentsByAgeTest() {
        ResponseEntity<Student[]> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/students/age/17",
                Student[].class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void getStudentsByAgeBetweenTest() {
        ResponseEntity<Student[]> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/students/age?min=16&max=18",
                Student[].class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void getStudentNotFoundTest() {
        ResponseEntity<Student> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/students/999999",
                Student.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}