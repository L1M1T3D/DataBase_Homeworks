package hw.skypro.database_intro.repositories;

import hw.skypro.database_intro.models.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {

    Collection<Faculty> findByColorIgnoreCase(String color);

    List<Faculty> findByNameContainingIgnoreCaseOrColorContainingIgnoreCase(
            String name,
            String color
    );
}