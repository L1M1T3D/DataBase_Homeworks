package hw.skypro.database_intro.controllers;

import hw.skypro.database_intro.models.Faculty;
import hw.skypro.database_intro.models.Student;
import hw.skypro.database_intro.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudent(@PathVariable Long id) {
        Student student = studentService.findStudent(id);

        if (student == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(student);
    }

    @GetMapping
    public ResponseEntity<Collection<Student>> getStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        return studentService.createStudent(student);
    }

    @PutMapping
    public ResponseEntity<Student> updateStudent(@RequestBody Student student) {
        Student updatedStudent = studentService.editStudent(student);

        if (updatedStudent == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.ok(updatedStudent);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/age/{age}")
    public Collection<Student> getStudentsByAge(@PathVariable int age) {
        return studentService.findByAge(age);
    }

    @GetMapping("/age")
    public Collection<Student> getStudentsByAgeBetween(
            @RequestParam int min,
            @RequestParam int max
    ) {
        return studentService.findByAgeBetween(min, max);
    }

    @GetMapping("/{id}/faculty")
    public ResponseEntity<Faculty> getStudentFaculty(@PathVariable Long id) {
        Faculty faculty = studentService.findFaculty(id);

        if (faculty == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(faculty);
    }

    @GetMapping("/count")
    public long getStudentsCount() {
        return studentService.getStudentsCount();
    }

    @GetMapping("/average-age")
    public double getAverageAge() {
        return studentService.getAverageAge();
    }

    @GetMapping("/last-five")
    public List<Student> getLastFiveStudents() {
        return studentService.getLastFiveStudents();
    }

    @GetMapping("/names-starting-with-a")
    public List<String> getStudentNamesStartingWithA() {
        return studentService.getStudentNamesStartingWithA();
    }

    @GetMapping("/average-age-stream")
    public double getAverageStudentAge() {
        return studentService.getAverageStudentAge();
    }

    @GetMapping("/parallel-sum")
    public int getParallelSum() {
        return studentService.getParallelSum();
    }

    @GetMapping("/print-parallel")
    public void printStudentsParallel() {
        List<Student> students = (List<Student>) studentService.getAllStudents();

        if (students.size() < 6) {
            System.out.println("Not enough students");
            return;
        }

        System.out.println(students.get(0).getName());
        System.out.println(students.get(1).getName());

        Thread thread1 = new Thread(() -> {
            System.out.println(students.get(2).getName());
            System.out.println(students.get(3).getName());
        });

        Thread thread2 = new Thread(() -> {
            System.out.println(students.get(4).getName());
            System.out.println(students.get(5).getName());
        });

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private synchronized void printName(String name) {
        System.out.println(name);
    }

    @GetMapping("/print-synchronized")
    public void printStudentsSynchronized() {
        List<Student> students = (List<Student>) studentService.getAllStudents();

        if (students.size() < 6) {
            System.out.println("Not enough students");
            return;
        }

        printName(students.get(0).getName());
        printName(students.get(1).getName());

        Thread thread1 = new Thread(() -> {
            printName(students.get(2).getName());
            printName(students.get(3).getName());
        });

        Thread thread2 = new Thread(() -> {
            printName(students.get(4).getName());
            printName(students.get(5).getName());
        });

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}