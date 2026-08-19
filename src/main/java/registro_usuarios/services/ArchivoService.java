package registro_usuarios.services;

import org.springframework.web.multipart.MultipartFile;

public interface ArchivoService {

    String guardarImagen(MultipartFile archivo);

    void eliminarImagen(String nombreArchivo);

}