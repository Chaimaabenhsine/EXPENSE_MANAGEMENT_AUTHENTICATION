package projet.micro.auth.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String refreshToken;
}