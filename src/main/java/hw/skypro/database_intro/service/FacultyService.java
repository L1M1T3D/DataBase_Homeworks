package hw.skypro.database_intro.service;

import hw.skypro.database_intro.models.Faculty;
import hw.skypro.database_intro.models.Student;
import hw.skypro.database_intro.repositories.FacultyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class FacultyService {

    private final FacultyRepository facultyRepository;

    @Autowired
    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    private static final Logger logger =
            LoggerFactory.getLogger(FacultyService.class);

    public Faculty createFaculty(Faculty faculty) {
        logger.info("Was invoked method for create faculty");
        logger.debug("Creating faculty with name {}", faculty.getName());

        return facultyRepository.save(faculty);
    }

    public Faculty findFaculty(Long id) {
        logger.info("Was invoked method for find faculty");

        Faculty faculty = facultyRepository.findById(id).orElse(null);

        if (faculty == null) {
            logger.warn("Faculty with id {} was not found", id);
        }

        return faculty;
    }

    public Faculty editFaculty(Faculty faculty) {
        logger.info("Was invoked method for edit faculty");

        if (!facultyRepository.existsById(faculty.getId())) {
            logger.warn(
                    "Faculty with id {} does not exist",
                    faculty.getId()
            );
            return null;
        }

        return facultyRepository.save(faculty);
    }

    public void deleteFaculty(Long id) {
        logger.info("Was invoked method for delete faculty");

        facultyRepository.deleteById(id);
    }

    public Collection<Faculty> getAllFaculty() {
        logger.info("Was invoked method for get all faculties");

        return facultyRepository.findAll();
    }

    public Collection<Faculty> findByColor(String color) {
        logger.info("Was invoked method for find faculties by color");

        return facultyRepository.findByColorIgnoreCase(color);
    }

    public List<Faculty> findByNameOrColor(String query) {
        logger.info("Was invoked method for find faculty by name or color");

        return facultyRepository
                .findByNameContainingIgnoreCaseOrColorContainingIgnoreCase(
                        query,
                        query
                );
    }

    public Collection<Student> findStudents(Long id) {
        logger.info("Was invoked method for find faculty students");

        Faculty faculty = findFaculty(id);

        if (faculty == null) {
            logger.warn(
                    "Students cannot be found because faculty with id {} does not exist",
                    id
            );
            return null;
        }

        return faculty.getStudents();
    }

}