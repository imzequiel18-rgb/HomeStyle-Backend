package registro_usuarios.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import registro_usuarios.services.ArchivoService;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class ArchivoServiceImpl implements ArchivoService {

    private final Path rutaUploads = Paths.get("uploads");

    public ArchivoServiceImpl() {
        try {
            Files.createDirectories(rutaUploads);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo crear la carpeta uploads", e);
        }
    }

    @Override
    public String guardarImagen(MultipartFile archivo) {

        if (archivo == null || archivo.isEmpty()) {
            return null;
        }

        String extension = StringUtils.getFilenameExtension(
                archivo.getOriginalFilename());

        String nombreArchivo = UUID.randomUUID() + "." + extension;

        try {

            Files.copy(
                    archivo.getInputStream(),
                    rutaUploads.resolve(nombreArchivo),
                    StandardCopyOption.REPLACE_EXISTING
            );

            return nombreArchivo;

        } catch (IOException e) {
            throw new RuntimeException("Error al guardar la imagen", e);
        }

    }

    @Override
    public void eliminarImagen(String nombreArchivo) {

        if (nombreArchivo == null || nombreArchivo.isBlank()) {
            return;
        }

        try {

            Files.deleteIfExists(
                    rutaUploads.resolve(nombreArchivo)
            );

        } catch (IOException e) {

            throw new RuntimeException("No se pudo eliminar la imagen", e);

        }

    }
}