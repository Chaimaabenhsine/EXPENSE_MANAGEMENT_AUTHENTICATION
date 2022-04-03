package projet.micro.auth.service;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import projet.micro.auth.model.Role;
import projet.micro.auth.model.User;
import projet.micro.auth.repo.UserRepository;
import projet.micro.auth.utils.JWTUtils;

@Slf4j
@Service 
@Transactional 
@RequiredArgsConstructor 
public class UserServiceImpl implements UserService, UserDetailsService 
{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public User saveUser(User user) 
    {
		if(userRepository.existsById(user.getId())) 
		{ 
			log.debug("username entred already exist");
		}
		String passwordToCrypt=user.getPassword();
        user.setPassword(passwordEncoder.encode(passwordToCrypt));
        return userRepository.save(user);
    }
    
    
    
    @Override
    public User updateUser(User user, Long id) 
    {
        user.setId(id);
        return userRepository.save(user);
    }

    
    
    @Override
	@Transactional(readOnly=true)
    public User getUser(String userName) 
    {
        return userRepository.findByUsername(userName);
    }

    
    
    @Override
	@Transactional(readOnly=true)
    public List<User> getUsers() 
    {
         return userRepository.findAll();
    }

    
    
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException 
    {
        User user = userRepository.findByUsername(userName);

        if(user == null)  throw new UsernameNotFoundException("User not found in the database!");
       
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles()
                .forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(), authorities
        );
    }

    
	@Override
	public void deleteById(Long id) 
	{
		userRepository.deleteById(id);	
	}
	
	@Override
	public void refreshToken(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException 
    {
    	System.out.print("im inside the function of service");

		
        String authorizationHeader = httpServletRequest.getHeader("Authorization");
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer "))
        {
        	System.out.print("im inside the if");

            try
            {
            	System.out.print("im inside the try");
                String refreshToken = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refreshToken);

                String userName = decodedJWT.getSubject();
                User user = this.getUser(userName);
                List<String> listRoles = user.getRoles()
                        .stream()
                        .map(Role::getName)
                        .collect(Collectors.toList());

                String accessToken = JWT.create()
                        .withSubject(user.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() +JWTUtils.EXPIRE_ACCES_TOKEN))
                        .withIssuer(httpServletRequest.getRequestURL().toString())
                        .withArrayClaim("roles", listRoles.toArray(new String[0]))
                        .sign(algorithm);

                Map<String, String> tokens =  new HashMap<>();
                tokens.put("accessToken", accessToken);
                tokens.put("refreshToken", refreshToken);
                httpServletResponse.setContentType(APPLICATION_JSON_VALUE);

                new ObjectMapper().writeValue(httpServletResponse.getOutputStream(), tokens);
                
            }catch (Exception exception)
            {
            	System.out.print("im inside the catch");
                httpServletResponse.setHeader("error", exception.getMessage());
                httpServletResponse.setStatus(FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error_message", exception.getMessage());
                httpServletResponse.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(httpServletResponse.getOutputStream(), error);
            }
        }else
        {
        	log.debug("The refresh token is missing");
        }
    }

	
}