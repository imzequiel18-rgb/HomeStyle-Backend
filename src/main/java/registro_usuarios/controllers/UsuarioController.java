package registro_usuarios.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import registro_usuarios.dto.*;
import registro_usuarios.entities.Usuario;
import registro_usuarios.services.UsuarioService;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @PostMapping
    public UsuarioDTO agregar(@Valid @RequestBody Usuario usuario){
        return service.agregar(usuario);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id){
        service.eliminar(id);
    }

    @GetMapping
    public List<UsuarioDTO> obtenerTodos(){
        return service.obtenerTodosDTO();
    }

    @GetMapping("/{id}")
    public UsuarioDTO obtenerUsuario(@PathVariable Long id){
        return service.obtenerUsuarioDTO(id);
    }

    @PutMapping("/{id}")
    public UsuarioDTO actualizar(@PathVariable Long id,@Valid @RequestBody Usuario usuario){
        return service.actualizar(id, usuario);
    }

    @PostMapping("/login")
    public LoginResponseDTO login(@Valid @RequestBody LoginRequestDTO request){
        return service.login(request);
    }

    @GetMapping("/perfil")
    public PerfilUsuarioDTO obtenerPerfil() {
        return service.obtenerPerfil();
    }

    @PutMapping("/perfil")
    public PerfilUsuarioDTO actualizarPerfil(
            @Valid @RequestBody ActualizarPerfilDTO dto) {

        return service.actualizarPerfil(dto);
    }

    @PutMapping("/cambiar-password")
    public String cambiarPassword(
            @Valid @RequestBody CambiarPasswordDTO dto) {

        return service.cambiarPassword(dto);
    }

}
