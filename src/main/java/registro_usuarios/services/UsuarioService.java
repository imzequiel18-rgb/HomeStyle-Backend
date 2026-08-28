package registro_usuarios.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import registro_usuarios.config.JwtService;
import registro_usuarios.dto.*;
import registro_usuarios.entities.Usuario;
import registro_usuarios.exceptions.RecursoDuplicadoException;
import registro_usuarios.repositories.UsuarioRepository;
import registro_usuarios.exceptions.BadRequestException;

import java.util.List;


@Service
public class UsuarioService {

    //clase UsuarioRepository
    @Autowired
    private UsuarioRepository repository;

    //clase PasswordEncoder
    @Autowired
    private PasswordEncoder passwordEncoder;

    //clase JwtService
    @Autowired
    private JwtService jwtService;

    //registra nuevo usuario
    public UsuarioDTO agregar(Usuario usuario){

        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        //dentro de la variable "guardado" se almacena el objeto de usuario
        //usando los metodos del reporitory
        Usuario guardado = repository.save(usuario);

        return convertirDTO(guardado);
    }

    private UsuarioDTO convertirDTO(Usuario u){
        return new UsuarioDTO(
                u.getId(),
                u.getUserName(),
                u.getEmail(),
                u.getPhoneNumber()
        );
    }

    public void eliminar(Long id){
        repository.deleteById(id);
    }

    public UsuarioDTO obtenerUsuarioDTO(Long id){
        Usuario u = repository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return convertirDTO(u);
    }

    public List<UsuarioDTO> obtenerTodosDTO() {
        List<Usuario> usuarios = repository.findAll();

        return usuarios.stream()
                .map(this::convertirDTO)
                .toList();
    }

    public UsuarioDTO actualizar(Long id, Usuario usuarioActualizado){
        Usuario u = repository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        u.setUserName(usuarioActualizado.getUserName());
        u.setEmail(usuarioActualizado.getEmail());
        u.setPassword(
                passwordEncoder.encode(usuarioActualizado.getPassword())
        );
        u.setPhoneNumber(usuarioActualizado.getPhoneNumber());
        u.setRol(usuarioActualizado.getRol());

        Usuario actualizado = repository.save(u);

        return convertirDTO(actualizado);
    }

    public LoginResponseDTO login(LoginRequestDTO request){

        Usuario usuario = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if(!passwordEncoder.matches(
                request.getPassword(),
                usuario.getPassword()
        ))
        {
            throw new RuntimeException("Contraseña incorrecta");
        }

        String token = jwtService.generarToken(usuario);

        return new LoginResponseDTO(
                usuario.getId(),
                usuario.getEmail(),
                usuario.getUserName(),
                usuario.getRol(),
                token
        );
    }

    public PerfilUsuarioDTO obtenerPerfil() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        Usuario usuario = repository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Usuario no encontrado"));

        return new PerfilUsuarioDTO(
                usuario.getId(),
                usuario.getUserName(),
                usuario.getEmail(),
                usuario.getPhoneNumber(),
                usuario.getRol().name()
        );
    }

    public PerfilUsuarioDTO actualizarPerfil(ActualizarPerfilDTO dto) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        Usuario usuario = repository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Usuario usuarioEmail = repository.findByEmail(dto.getEmail()).orElse(null);

        if (usuarioEmail != null && !usuarioEmail.getId().equals(usuario.getId())) {
            throw new RecursoDuplicadoException("El correo ya está registrado");
        }

        Usuario usuarioNombre = repository.findByUserName(dto.getUserName()).orElse(null);

        if (usuarioNombre != null && !usuarioNombre.getId().equals(usuario.getId())) {
            throw new RecursoDuplicadoException("El nombre de usuario ya está registrado");
        }

        usuario.setUserName(dto.getUserName());
        usuario.setEmail(dto.getEmail());
        usuario.setPhoneNumber(dto.getPhoneNumber());

        Usuario actualizado = repository.save(usuario);

        return new PerfilUsuarioDTO(
                actualizado.getId(),
                actualizado.getUserName(),
                actualizado.getEmail(),
                actualizado.getPhoneNumber(),
                actualizado.getRol().name()
        );
    }

    public String cambiarPassword(CambiarPasswordDTO dto) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        Usuario usuario = repository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Usuario no encontrado"));

        // Verificar contraseña actual
        if (!passwordEncoder.matches(dto.getPasswordActual(), usuario.getPassword())) {

            throw new BadRequestException("La contraseña actual es incorrecta");
        }

        // Verificar confirmación
        if (!dto.getPasswordNueva().equals(dto.getConfirmarPassword())) {
            throw new BadRequestException("La nueva contraseña y la confirmación no coinciden");
        }

        // Guardar nueva contraseña encriptada
        usuario.setPassword(
                passwordEncoder.encode(dto.getPasswordNueva())
        );

        repository.save(usuario);

        return "Contraseña actualizada correctamente";
    }

}
