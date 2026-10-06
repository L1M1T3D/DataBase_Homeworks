package hw.skypro.database_intro.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import hw.skypro.database_intro.models.Faculty;
import hw.skypro.database_intro.service.FacultyService;
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

@WebMvcTest(FacultyController.class)
class FacultyControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private FacultyService facultyService;

    @Test
    void createFacultyTest() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Gryffindor");
        faculty.setColor("Red");

        when(facultyService.createFaculty(any(Faculty.class)))
                .thenReturn(faculty);

        mockMvc.perform(post("/faculties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(faculty)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Gryffindor"))
                .andExpect(jsonPath("$.color").value("Red"));
    }

    @Test
    void getFacultyTest() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Slytherin");
        faculty.setColor("Green");

        when(facultyService.findFaculty(1L))
                .thenReturn(faculty);

        mockMvc.perform(get("/faculties/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Slytherin"))
                .andExpect(jsonPath("$.color").value("Green"));
    }

    @Test
    void getAllFacultiesTest() throws Exception {
        Faculty firstFaculty = new Faculty();
        firstFaculty.setId(1L);
        firstFaculty.setName("Gryffindor");
        firstFaculty.setColor("Red");

        Faculty secondFaculty = new Faculty();
        secondFaculty.setId(2L);
        secondFaculty.setName("Slytherin");
        secondFaculty.setColor("Green");

        when(facultyService.getAllFaculty())
                .thenReturn(List.of(firstFaculty, secondFaculty));

        mockMvc.perform(get("/faculties"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Gryffindor"))
                .andExpect(jsonPath("$[1].name").value("Slytherin"));
    }

    @Test
    void updateFacultyTest() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Ravenclaw");
        faculty.setColor("Blue");

        when(facultyService.editFaculty(any(Faculty.class)))
                .thenReturn(faculty);

        mockMvc.perform(put("/faculties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(faculty)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Ravenclaw"))
                .andExpect(jsonPath("$.color").value("Blue"));
    }

    @Test
    void deleteFacultyTest() throws Exception {
        mockMvc.perform(delete("/faculties/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getFacultiesByColorTest() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Gryffindor");
        faculty.setColor("Red");

        when(facultyService.findByColor("Red"))
                .thenReturn(List.of(faculty));

        mockMvc.perform(get("/faculties/color/Red"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Gryffindor"))
                .andExpect(jsonPath("$[0].color").value("Red"));
    }

    @Test
    void searchFacultiesTest() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Ravenclaw");
        faculty.setColor("Blue");

        when(facultyService.findByNameOrColor("Blue"))
                .thenReturn(List.of(faculty));

        mockMvc.perform(get("/faculties/search")
                        .param("query", "Blue"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Ravenclaw"))
                .andExpect(jsonPath("$[0].color").value("Blue"));
    }

    @Test
    void getFacultyNotFoundTest() throws Exception {
        when(facultyService.findFaculty(999L))
                .thenReturn(null);

        mockMvc.perform(get("/faculties/999"))
                .andExpect(status().isNotFound());
    }
}