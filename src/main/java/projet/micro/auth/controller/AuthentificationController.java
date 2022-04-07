package projet.micro.auth.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.security.auth.message.AuthException;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.service.spi.ServiceException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import lombok.RequiredArgsConstructor;
import projet.micro.auth.exception.BadCredentialsException;
import projet.micro.auth.model.JWTRequest;
import projet.micro.auth.model.JWTResponse;
import projet.micro.auth.model.Role;
import projet.micro.auth.model.TokenDto;
import projet.micro.auth.service.JWTService;
import projet.micro.auth.service.UserService;


@RestController @RequestMapping("/api/auth") @RequiredArgsConstructor
public class AuthentificationController 
{
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final UserService userService;


	 	@PostMapping(value = "login",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
	    public ResponseEntity<JWTResponse>  createAuthenticationTokenLoginteqt(@RequestBody JWTRequest authenticationRequest, HttpServletRequest request) throws ServiceException, AuthException ,BadCredentialsException{
	            Authentication authentication= authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
	            User user = (User) authentication.getPrincipal();
	            Algorithm algorithm = Algorithm.HMAC256("secret");
	            List<String> roles = user.getAuthorities()
	                    .stream()
	                    .map(GrantedAuthority::getAuthority)
	                    .collect(Collectors.toList());
	            System.out.print("roles in jwt are: "+roles);
	            Map<String, String> tokens = jwtService.jwtTokens(roles, algorithm, request.getRequestURL().toString(), user.getUsername());

	            JWTResponse jwtResponce = new JWTResponse();
	            jwtResponce.setDataURL(request.getRequestURL().toString());
	            jwtResponce.setAccess_token(tokens.get("accessToken"));
	            jwtResponce.setRefresh_token(tokens.get("refreshToken"));
	            return ResponseEntity.ok(jwtResponce);
	    }
	 	
		private Authentication authenticate(String username, String password) throws ServiceException,BadCredentialsException {
		    try {
		        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		    } catch (DisabledException e) {
		        throw new ServiceException("USER_DISABLED");}
		    
}
		@PostMapping("refreshToken")
	    public ResponseEntity<Map<String, String>> refreshToken(@RequestBody TokenDto token, HttpServletRequest request) throws AuthException {
			System.out.println("I enterred the refresh token method");
	        Algorithm algorithm = Algorithm.HMAC256("secret");
	        DecodedJWT decodedJWT = JWT.decode(token.getRefreshToken());
	        String username=decodedJWT.getSubject();
	        projet.micro.auth.model.User user = userService.getUser(username);
	        if (null==user) {
	            throw new AuthException("invalid credentials here");
	        }
	        List<String> roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toList());
	        Map<String, String> tokens = jwtService.jwtTokens(roles, algorithm, request.getRequestURL().toString(), user.getUsername());
	        return ResponseEntity.ok(tokens);
	    }
	 	
}
