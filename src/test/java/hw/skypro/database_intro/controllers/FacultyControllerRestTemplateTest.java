package hw.skypro.database_intro.controllers;

import hw.skypro.database_intro.models.Faculty;
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
class FacultyControllerRestTemplateTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void contextLoads() {
        assertThat(restTemplate).isNotNull();
    }

    @Test
    void createFacultyTest() {
        Faculty faculty = new Faculty();
        faculty.setName("Gryffindor");
        faculty.setColor("Red");

        ResponseEntity<Faculty> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/faculties",
                faculty,
                Faculty.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Gryffindor");
        assertThat(response.getBody().getColor()).isEqualTo("Red");
    }

    @Test
    void getFacultyTest() {
        Faculty faculty = new Faculty();
        faculty.setName("Slytherin");
        faculty.setColor("Green");

        Faculty createdFaculty = restTemplate.postForObject(
                "http://localhost:" + port + "/faculties",
                faculty,
                Faculty.class
        );

        ResponseEntity<Faculty> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/faculties/" + createdFaculty.getId(),
                Faculty.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Slytherin");
        assertThat(response.getBody().getColor()).isEqualTo("Green");
    }

    @Test
    void getAllFacultiesTest() {
        ResponseEntity<Faculty[]> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/faculties",
                Faculty[].class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void updateFacultyTest() {
        Faculty faculty = new Faculty();
        faculty.setName("Ravenclaw");
        faculty.setColor("Blue");

        Faculty createdFaculty = restTemplate.postForObject(
                "http://localhost:" + port + "/faculties",
                faculty,
                Faculty.class
        );

        createdFaculty.setName("Updated Ravenclaw");
        createdFaculty.setColor("Dark Blue");

        restTemplate.put(
                "http://localhost:" + port + "/faculties",
                createdFaculty
        );

        ResponseEntity<Faculty> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/faculties/" + createdFaculty.getId(),
                Faculty.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Updated Ravenclaw");
        assertThat(response.getBody().getColor()).isEqualTo("Dark Blue");
    }

    @Test
    void deleteFacultyTest() {
        Faculty faculty = new Faculty();
        faculty.setName("Hufflepuff");
        faculty.setColor("Yellow");

        Faculty createdFaculty = restTemplate.postForObject(
                "http://localhost:" + port + "/faculties",
                faculty,
                Faculty.class
        );

        restTemplate.delete(
                "http://localhost:" + port + "/faculties/" + createdFaculty.getId()
        );

        ResponseEntity<Faculty> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/faculties/" + createdFaculty.getId(),
                Faculty.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void getFacultiesByColorTest() {
        ResponseEntity<Faculty[]> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/faculties/color/Red",
                Faculty[].class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void searchFacultiesTest() {
        ResponseEntity<Faculty[]> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/faculties/search?query=Red",
                Faculty[].class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void getFacultyNotFoundTest() {
        ResponseEntity<Faculty> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/faculties/999999",
                Faculty.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}