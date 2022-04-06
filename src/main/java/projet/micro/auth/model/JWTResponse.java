package projet.micro.auth.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JWTResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    Boolean success;

    String dataURL;

    @java.lang.SuppressWarnings("squid:S116")
    String refresh_token;

    @java.lang.SuppressWarnings("squid:S116")
    String access_token;

    public JWTResponse(String dataURL) {
        this.dataURL = dataURL;
        this.success = true;
    }

    public JWTResponse() {
        this.success = true;
    }
}