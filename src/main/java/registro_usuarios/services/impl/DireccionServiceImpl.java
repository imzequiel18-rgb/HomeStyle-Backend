package registro_usuarios.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import registro_usuarios.dto.DireccionDTO;
import registro_usuarios.dto.DireccionRequestDTO;
import registro_usuarios.mapper.DireccionMapper;
import registro_usuarios.repositories.DireccionRepository;
import registro_usuarios.repositories.UsuarioRepository;
import registro_usuarios.services.DireccionService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import registro_usuarios.entities.Direccion;
import registro_usuarios.entities.Usuario;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DireccionServiceImpl implements DireccionService {

    private final DireccionRepository direccionRepository;

    private final UsuarioRepository usuarioRepository;

    private final DireccionMapper direccionMapper;

    private Usuario obtenerUsuarioAutenticado() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        return usuarioRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Usuario no encontrado."));

    }

    @Override
    @Transactional(readOnly = true)
    public List<DireccionDTO> obtenerMisDirecciones() {

        Usuario usuario = obtenerUsuarioAutenticado();

        return direccionRepository
                .findByUsuarioAndActivoTrue(usuario)
                .stream()
                .map(direccionMapper::toDTO)
                .toList();

    }

    @Override
    @Transactional(readOnly = true)
    public DireccionDTO obtenerDireccion(Long id) {

        Usuario usuario = obtenerUsuarioAutenticado();

        Direccion direccion = direccionRepository
                .findByIdAndActivoTrue(id)
                .orElseThrow(() ->
                        new RuntimeException("Dirección no encontrada."));

        if (!direccion.getUsuario().getId().equals(usuario.getId())) {

            throw new RuntimeException(
                    "No tienes permiso para consultar esta dirección."
            );

        }

        return direccionMapper.toDTO(direccion);

    }

    private Direccion buscarDireccion(Long id){

        return direccionRepository
                .findByIdAndActivoTrue(id)
                .orElseThrow(() ->
                        new RuntimeException("Dirección no encontrada."));

    }


    @Override
    public DireccionDTO crearDireccion(DireccionRequestDTO request) {

        Usuario usuario = obtenerUsuarioAutenticado();

        Direccion direccion = Direccion.builder()

                .usuario(usuario)

                .nombreDestinatario(request.getNombreDestinatario())

                .telefonoContacto(request.getTelefonoContacto())

                .calle(request.getCalle())

                .numeroExterior(request.getNumeroExterior())

                .numeroInterior(request.getNumeroInterior())

                .colonia(request.getColonia())

                .ciudad(request.getCiudad())

                .estado(request.getEstado())

                .codigoPostal(request.getCodigoPostal())

                .referencias(request.getReferencias())

                .latitud(request.getLatitud())

                .longitud(request.getLongitud())

                .activo(true)

                .predeterminada(false)

                .build();

        List<Direccion> direcciones =
                direccionRepository.findByUsuarioAndActivoTrue(usuario);

        if(direcciones.isEmpty()){

            direccion.setPredeterminada(true);

        }

        Direccion nuevaDireccion = direccionRepository.save(direccion);

        return direccionMapper.toDTO(nuevaDireccion);
    }

    @Override
    public DireccionDTO actualizarDireccion(
            Long id,
            DireccionRequestDTO request) {

        Usuario usuario = obtenerUsuarioAutenticado();

        Direccion direccion = buscarDireccion(id);

        if (!direccion.getUsuario().getId().equals(usuario.getId())) {

            throw new RuntimeException(
                    "No tienes permiso para modificar esta dirección."
            );

        }

        direccion.setNombreDestinatario(request.getNombreDestinatario());

        direccion.setTelefonoContacto(request.getTelefonoContacto());

        direccion.setCalle(request.getCalle());

        direccion.setNumeroExterior(request.getNumeroExterior());

        direccion.setNumeroInterior(request.getNumeroInterior());

        direccion.setColonia(request.getColonia());

        direccion.setCiudad(request.getCiudad());

        direccion.setEstado(request.getEstado());

        direccion.setCodigoPostal(request.getCodigoPostal());

        direccion.setReferencias(request.getReferencias());

        direccion.setLatitud(request.getLatitud());

        direccion.setLongitud(request.getLongitud());

        Direccion direccionActualizada =
                direccionRepository.save(direccion);

        return direccionMapper.toDTO(direccionActualizada);

    }

    @Override
    public void eliminarDireccion(Long id) {

        Usuario usuario = obtenerUsuarioAutenticado();

        Direccion direccion = buscarDireccion(id);

        if (!direccion.getUsuario().getId().equals(usuario.getId())) {

            throw new RuntimeException(
                    "No tienes permiso para eliminar esta dirección."
            );

        }

        boolean eraPredeterminada = direccion.getPredeterminada();

        direccion.setActivo(false);

        direccion.setPredeterminada(false);

        direccionRepository.save(direccion);

        if (eraPredeterminada) {

            List<Direccion> direcciones =
                    direccionRepository.findByUsuarioAndActivoTrue(usuario);

            if (!direcciones.isEmpty()) {

                Direccion nuevaPredeterminada = direcciones.get(0);

                nuevaPredeterminada.setPredeterminada(true);

                direccionRepository.save(nuevaPredeterminada);

            }

        }

    }


    @Override
    public void establecerPredeterminada(Long id) {

        Usuario usuario = obtenerUsuarioAutenticado();

        Direccion direccion = buscarDireccion(id);

        if (!direccion.getUsuario().getId().equals(usuario.getId())) {

            throw new RuntimeException(
                    "No tienes permiso para modificar esta dirección."
            );

        }

        List<Direccion> direcciones =
                direccionRepository.findByUsuarioAndActivoTrue(usuario);

        for (Direccion item : direcciones) {

            item.setPredeterminada(
                    item.getId().equals(id)
            );

        }

        direccionRepository.saveAll(direcciones);

    }


}