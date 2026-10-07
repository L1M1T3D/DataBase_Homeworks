package hw.skypro.database_intro.service;

import hw.skypro.database_intro.models.Faculty;
import hw.skypro.database_intro.models.Student;
import hw.skypro.database_intro.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Stream;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    public Student createStudent(Student student) {
        logger.info("Was invoked method for create student");
        logger.debug("Student name: {}", student.getName());

        return studentRepository.save(student);
    }

    public Student findStudent(Long id) {
        logger.info("Was invoked method for find student");

        Student student = studentRepository.findById(id).orElse(null);

        if (student == null) {
            logger.warn("Student with id {} was not found", id);
        }

        return student;
    }

    public Student editStudent(Student student) {
        logger.info("Was invoked method for edit student");

        if (!studentRepository.existsById(student.getId())) {
            logger.warn("Student with id {} does not exist", student.getId());
            return null;
        }

        return studentRepository.save(student);
    }

    public void deleteStudent(Long id) {
        logger.info("Was invoked method for delete student");

        studentRepository.deleteById(id);
    }

    public Collection<Student> getAllStudents() {
        logger.info("Was invoked method for get all students");

        return studentRepository.findAll();
    }

    public Collection<Student> findByAge(int age) {
        logger.info("Was invoked method for find students by age");

        return studentRepository.findByAge(age);
    }

    public Collection<Student> findByAgeBetween(int min, int max) {
        logger.info("Was invoked method for find students by age between");

        return studentRepository.findByAgeBetween(min, max);
    }

    public Faculty findFaculty(Long id) {
        logger.info("Was invoked method for find student faculty");

        Student student = findStudent(id);

        if (student == null) {
            logger.warn("Faculty cannot be found because student with id {} does not exist", id);
            return null;
        }

        return student.getFaculty();
    }

    public long getStudentsCount() {
        logger.info("Was invoked method for get students count");

        return studentRepository.getStudentsCount();
    }

    public double getAverageAge() {
        logger.info("Was invoked method for get average student age");

        Double averageAge = studentRepository.getAverageAge();

        if (averageAge == null) {
            logger.debug("There are no students, average age is 0");
            return 0.0;
        }

        return averageAge;
    }

    public List<Student> getLastFiveStudents() {
        logger.info("Was invoked method for get last five students");

        return studentRepository.getLastFiveStudents();
    }

    public List<String> getStudentNamesStartingWithA() {
        logger.info("Was invoked method for get student names starting with A");

        return studentRepository.findAll().stream()
                .map(Student::getName)
                .map(String::toUpperCase)
                .filter(name -> name.startsWith("A"))
                .sorted()
                .toList();
    }

    public double getAverageStudentAge() {
        logger.info("Was invoked method for get average student age by stream");

        return studentRepository.findAll().stream()
                .mapToInt(Student::getAge)
                .average()
                .orElse(0);
    }

    public int getParallelSum() {
        logger.info("Was invoked method for calculate parallel sum");

        return Stream.iterate(1, a -> a + 1)
                .limit(1_000_000)
                .parallel()
                .reduce(0, Integer::sum);
    }

}