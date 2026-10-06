package hw.skypro.database_intro.repositories;

import hw.skypro.database_intro.models.Avatar;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AvatarRepository extends JpaRepository<Avatar, Long> {

    Optional<Avatar> findByStudentId(long id);

    Page<Avatar> findAll(Pageable pageable);
}