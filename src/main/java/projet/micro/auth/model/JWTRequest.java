package projet.micro.auth.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JWTRequest implements Serializable {

    private static final long serialVersionUID = 5926468583005150707L;
    @NotBlank 
    private String username;
    @NotBlank 
    private String password;
    private String code;
}