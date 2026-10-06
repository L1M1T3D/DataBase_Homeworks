package hw.skypro.database_intro.service;

import hw.skypro.database_intro.models.Faculty;
import hw.skypro.database_intro.models.Student;
import hw.skypro.database_intro.repositories.FacultyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class FacultyService {

    private final FacultyRepository facultyRepository;

    @Autowired
    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty createFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public Faculty findFaculty(Long id) {
        return facultyRepository.findById(id).orElse(null);
    }

    public Faculty editFaculty(Faculty faculty) {
        if (!facultyRepository.existsById(faculty.getId())) {
            return null;
        }

        return facultyRepository.save(faculty);
    }

    public void deleteFaculty(Long id) {
        facultyRepository.deleteById(id);
    }

    public Collection<Faculty> getAllFaculty() {
        return facultyRepository.findAll();
    }

    public Collection<Faculty> findByColor(String color) {
        return facultyRepository.findByColorIgnoreCase(color);
    }

    public Collection<Faculty> findByNameOrColor(String search) {
        return facultyRepository.findByNameContainingIgnoreCaseOrColorContainingIgnoreCase(
                search,
                search
        );
    }

    public Collection<Student> findStudents(Long facultyId) {
        Faculty faculty = facultyRepository.findById(facultyId).orElse(null);

        if (faculty == null) {
            return null;
        }

        return faculty.getStudents();
    }
}