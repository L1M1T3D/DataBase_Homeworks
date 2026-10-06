package hw.skypro.database_intro.service;

import hw.skypro.database_intro.models.Avatar;
import hw.skypro.database_intro.models.Student;
import hw.skypro.database_intro.repositories.AvatarRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
@Transactional
public class AvatarService {

    private static final Logger logger =
            LoggerFactory.getLogger(AvatarService.class);

    @Value("avatars")
    private String avatarsDir;

    private final StudentService studentService;
    private final AvatarRepository avatarRepository;

    public AvatarService(StudentService studentService,
                         AvatarRepository avatarRepository) {
        this.studentService = studentService;
        this.avatarRepository = avatarRepository;
    }

    public Avatar findByStudentId(long studentId) {
        logger.info("Was invoked method for find avatar by student id");

        Avatar avatar = avatarRepository.findByStudentId(studentId)
                .orElse(null);

        if (avatar == null) {
            logger.warn("Avatar for student with id {} was not found", studentId);
        }

        return avatar;
    }

    private String getExtension(String fileName) {
        logger.info("Was invoked method for get file extension");

        if (fileName == null || !fileName.contains(".")) {
            logger.debug("File has no extension, jpg will be used");
            return "jpg";
        }

        return fileName.substring(fileName.lastIndexOf('.') + 1)
                .toLowerCase();
    }

    public void uploadAvatar(Long studentId, MultipartFile file)
            throws IOException {

        logger.info("Was invoked method for upload avatar");

        Student student = studentService.findStudent(studentId);

        if (student == null) {
            logger.error("No student with id = {}", studentId);
            throw new IllegalStateException(
                    "Student with id " + studentId + " not found"
            );
        }

        String extension = getExtension(file.getOriginalFilename());

        Path filePath = Path.of(
                avatarsDir,
                student.getId() + "." + extension
        );

        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);

        try (InputStream inputStream = file.getInputStream();
             OutputStream outputStream =
                     Files.newOutputStream(filePath, CREATE_NEW)) {

            inputStream.transferTo(outputStream);
        }

        Avatar avatar = avatarRepository
                .findByStudentId(studentId)
                .orElse(new Avatar());

        avatar.setStudent(student);
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(file.getSize());
        avatar.setMediaType(file.getContentType());
        avatar.setData(generateAvatar(filePath));

        avatarRepository.save(avatar);

        logger.debug("Avatar for student with id {} was saved", studentId);
    }

    private byte[] generateAvatar(Path filePath) throws IOException {
        logger.info("Was invoked method for generate avatar preview");

        try (InputStream inputStream = Files.newInputStream(filePath)) {

            BufferedImage image = ImageIO.read(inputStream);

            if (image == null) {
                logger.warn("Could not read image from {}", filePath);
                return new byte[0];
            }

            int width = 100;
            int height = (int) (
                    width
                            * (double) image.getHeight()
                            / image.getWidth()
            );

            BufferedImage preview = new BufferedImage(
                    width,
                    height,
                    BufferedImage.TYPE_INT_RGB
            );

            Graphics2D graphics = preview.createGraphics();
            graphics.drawImage(image, 0, 0, width, height, null);
            graphics.dispose();

            try (ByteArrayOutputStream outputStream =
                         new ByteArrayOutputStream()) {

                ImageIO.write(preview, "jpg", outputStream);

                return outputStream.toByteArray();
            }
        }
    }
}