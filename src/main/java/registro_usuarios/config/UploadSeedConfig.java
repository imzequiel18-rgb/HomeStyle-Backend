package registro_usuarios.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.nio.file.*;
import java.util.List;

@Component
public class UploadSeedConfig implements CommandLineRunner {

    private final Path uploadDir;

    private static final List<String> INITIAL_IMAGES = List.of(
            "966e3a30-8051-4680-bdf4-ff2f260a57e7.png",
            "690e89bc-f524-453e-8d29-2b37b1f04b58.png",
            "6a5eec98-529f-4aa8-ae25-6f8f5e41f367.png",
            "6583b49b-bef1-475f-bf77-bd4b5854e1c0.png",
            "e594cd26-0fbc-4835-97af-adecc920125d.png",
            "6a0ab242-c533-4baa-b1e0-58be74db59d9.png",
            "89aa1ba7-2019-4339-a645-50afa6b45ba2.png",
            "b121eadb-9cb6-4804-8830-65708941db47.png",
            "09f801db-f832-4280-b52c-749f77a50c78.png",
            "0ddfdf2c-5ab0-46f9-8577-40f21be44aa8.png"
    );

    public UploadSeedConfig(@Value("${app.upload-dir:uploads}") String uploadDir) {
        this.uploadDir = Paths.get(uploadDir).toAbsolutePath().normalize();
    }

    @Override
    public void run(String... args) throws Exception {
        Files.createDirectories(uploadDir);
        for (String filename : INITIAL_IMAGES) {
            Path destination = uploadDir.resolve(filename);
            if (Files.notExists(destination)) {
                ClassPathResource resource = new ClassPathResource("seed-uploads/" + filename);
                if (resource.exists()) {
                    try (InputStream in = resource.getInputStream()) {
                        Files.copy(in, destination, StandardCopyOption.REPLACE_EXISTING);
                    }
                }
            }
        }
    }
}
