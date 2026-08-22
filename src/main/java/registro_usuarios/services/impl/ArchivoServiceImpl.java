package registro_usuarios.services.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import registro_usuarios.services.ArchivoService;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class ArchivoServiceImpl implements ArchivoService {

    private final Path rutaUploads;

    public ArchivoServiceImpl(@Value("${app.upload-dir:uploads}") String uploadDir) {
        this.rutaUploads = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(rutaUploads);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo crear la carpeta de uploads: " + rutaUploads, e);
        }
    }

    @Override
    public String guardarImagen(MultipartFile archivo) {
        if (archivo == null || archivo.isEmpty()) return null;

        String extension = StringUtils.getFilenameExtension(archivo.getOriginalFilename());
        String nombreArchivo = UUID.randomUUID() + (extension == null || extension.isBlank() ? "" : "." + extension);

        try {
            Files.copy(archivo.getInputStream(), rutaUploads.resolve(nombreArchivo), StandardCopyOption.REPLACE_EXISTING);
            return nombreArchivo;
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar la imagen", e);
        }
    }

    @Override
    public void eliminarImagen(String nombreArchivo) {
        if (nombreArchivo == null || nombreArchivo.isBlank()) return;
        try {
            Files.deleteIfExists(rutaUploads.resolve(Paths.get(nombreArchivo).getFileName()));
        } catch (IOException e) {
            throw new RuntimeException("No se pudo eliminar la imagen", e);
        }
    }
}
