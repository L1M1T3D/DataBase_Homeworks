package hw.skypro.database_intro.controllers;

import hw.skypro.database_intro.models.Avatar;
import hw.skypro.database_intro.repositories.AvatarRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AvatarController {

    private final AvatarRepository avatarRepository;

    public AvatarController(AvatarRepository avatarRepository) {
        this.avatarRepository = avatarRepository;
    }

    @GetMapping("/avatarPaging")
    public Page<Avatar> getAllAvatars(@RequestParam int page,
                                      @RequestParam int size) {

        Pageable pageable = PageRequest.of(page, size);

        return avatarRepository.findAll(pageable);
    }
}