package registro_usuarios.dto;

public class PerfilUsuarioDTO {

    private Long id;
    private String userName;
    private String email;
    private String phoneNumber;
    private String rol;

    public PerfilUsuarioDTO() {
    }

    public PerfilUsuarioDTO(Long id, String userName, String email, String phoneNumber, String rol) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.rol = rol;
    }

    public Long getId() {
        return id;
    }



    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}