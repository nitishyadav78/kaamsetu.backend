package kaamsetu.com.DTO;

import lombok.Data;

@Data
public class RegisterRequest {
    private String name;

    private String email;

    private String password;

    private String role;

    private String College;

    private String year;

    private  String skills;
}
