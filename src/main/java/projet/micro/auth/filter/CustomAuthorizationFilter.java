package projet.micro.auth.filter;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomAuthorizationFilter extends OncePerRequestFilter {
	

	
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    	
    	 response.addHeader("Access-Control-Allow-Origin", "*");
         response.addHeader("Access-Control-Allow-Headers", "Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers,Authorization");
         response.addHeader("Access-Control-Expose-Headers", "Access-Control-Allow-Origin, Access-Control-Allow-Credentials, Authorization");
         response.addHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS, PUT, PATCH, DELETE");
        
         if(request.getServletPath().equals("/login") || request.getServletPath().equals("/api/token/refresh")){
            filterChain.doFilter(request, response);
         }
         else
         {

            String authorizationHeader = request.getHeader(JWTInfos.AUTH_HEADER);
            if(authorizationHeader != null && authorizationHeader.startsWith(JWTInfos.PREFIX)){
                try{
                    String token = authorizationHeader.substring(JWTInfos.PREFIX.length());
                    Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                    JWTVerifier verifier = JWT.require(algorithm).build();
                    DecodedJWT decodedJWT = verifier.verify(token);

                    String userName = decodedJWT.getSubject();
                    String [] roles = decodedJWT.getClaim("roles").asArray(String.class);

                    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    stream(roles).forEach(role -> {
                        authorities.add(new SimpleGrantedAuthority(role));
                    });

                    UsernamePasswordAuthenticationToken authenticationToken
                            = new UsernamePasswordAuthenticationToken(userName, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                    filterChain.doFilter(request, response);
                }catch (Exception exception){
                    log.error("Error loggin in: {}", exception.getMessage());
                    response.setHeader("error", exception.getMessage());
                    response.setStatus(FORBIDDEN.value());
                    Map<String, String> error = new HashMap<>();
                    error.put("error_message", exception.getMessage());
                    response.setContentType(APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream(), error);
                }
            }else{
                filterChain.doFilter(request, response);
            }
        }

    }
}
