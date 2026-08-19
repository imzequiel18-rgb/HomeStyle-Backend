package registro_usuarios.dto;

import registro_usuarios.entities.Usuario.Rol;

public class LoginResponseDTO {

    private Long id;
    private String email;
    private String userName;
    private Rol rol;
    private String token;

    public LoginResponseDTO() {
    }

    public LoginResponseDTO(Long id, String email,String userName, Rol rol, String token) {
        this.id = id;
        this.email = email;
        this.userName = userName;
        this.rol = rol;
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

}
