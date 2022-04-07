package projet.micro.auth.security.filters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static java.util.Arrays.stream;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@AllArgsConstructor
public class JWTTokenUtil {


    public static final String AUTHORIZATION_HEADER = "Authorization";

    public String resolveToken(HttpServletRequest request) {
        System.out.println("Im inside the resolve function");

        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        System.out.println("the old bearer token: "+bearerToken);
        if (Util.isNotBlank(bearerToken) && bearerToken.startsWith("Bearer ")) {
            System.out.println("the new bearer token: "+bearerToken.substring(7));
            return bearerToken.substring(7);

        }
        return null;
    }

    public UsernamePasswordAuthenticationToken getAuthentication(String jwt) {
        System.out.println("Im inside the getAuth function");
        Algorithm algorithm = Algorithm.HMAC256("secret");
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = jwtVerifier.verify(jwt);
        String username = decodedJWT.getSubject();
        System.out.println("The username is "+username);
        String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
        System.out.println("The roles are "+roles);
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        stream(roles).forEach(role ->  authorities.add(new SimpleGrantedAuthority(role)));
        System.out.println("The new roles are "+authorities);
        return new UsernamePasswordAuthenticationToken(username, null, authorities);
    }

    public boolean validateToken(String jwt, HttpServletResponse httpServletResponse) throws IOException {
        System.out.println("Im inside the validate function");

        String message;
        try {
            Algorithm algorithm = Algorithm.HMAC256("secret");
            JWTVerifier jwtVerifier = JWT.require(algorithm).build();
            jwtVerifier.verify(jwt);
            return true;
        } catch (TokenExpiredException e) {
            message = e.getMessage();
           
            log.error("Token Expired",e);
        } catch (InvalidClaimException | IllegalArgumentException e) {
            message = e.getMessage();
            log.error("invalid", e);
        }
        httpServletResponse.setHeader( "error-message", message);
        return false;
    }

}