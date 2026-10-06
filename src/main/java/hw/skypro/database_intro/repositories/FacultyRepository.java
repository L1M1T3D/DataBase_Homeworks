package hw.skypro.database_intro.repositories;

import hw.skypro.database_intro.models.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {

    Collection<Faculty> findByColorIgnoreCase(String color);

    Collection<Faculty> findByNameContainingIgnoreCaseOrColorContainingIgnoreCase(
            String name,
            String color
    );
}