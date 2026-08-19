package registro_usuarios.dto;

public class UsuarioDTO {

    private Long Id;
    private String userName;
    private String email;
    private String phoneNumber;

    public  UsuarioDTO(){}

    public UsuarioDTO(Long id, String user, String email, String phoneNumber) {
        Id = id;
        this.userName = user;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
