package projet.micro.auth.controller;


import java.io.IOException;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;

import projet.micro.auth.filter.JWTInfos;
import projet.micro.auth.model.Role;
import projet.micro.auth.model.User;
import projet.micro.auth.service.UserService;


@CrossOrigin("*") @RestController @RequestMapping("/api")
public class UserController 
{
	
    @Autowired 
    private UserService userService;
    
    //get the list of Users
    @PreAuthorize("hasRole('ROLE_ADMINISTRATEUR')")
    @GetMapping("/users")
    public List<User> getUsers()
    {
        return userService.getUsers();
    }

    //get a specific user by its user name    
    @PreAuthorize("hasRole('ROLE_ADMINISTRATEUR')")
    @GetMapping("/users/{username}")
    public User getUser(@PathVariable String username)
    {
        return userService.getUser(username);
    }
    
    //save a new user with an existing role
    @PreAuthorize("hasRole('ROLE_ADMINISTRATEUR')")
    @PostMapping("/users")
    public User saveUser(@RequestBody User user)
    {
          return userService.saveUser(user);
    }

    //update an existing user by id
    @PreAuthorize("hasRole('ROLE_ADMINISTRATEUR')")
    @PutMapping("users/{id}")
	public User updateUser(@PathVariable Long id,@RequestBody User user)
	{
		return userService.updateUser(user,id);
	}
	
    //delete a user by id 
    @PreAuthorize("hasRole('ROLE_ADMINISTRATEUR')")
	@DeleteMapping("users/{id}")
	public void deleteUser(@PathVariable Long id)
	{
		 userService.deleteById(id);
	}
    
	//save a new role
    @PreAuthorize("hasRole('ROLE_ADMINISTRATEUR')")
    @PostMapping("/roles")
    public Role saveRole(Role role)
    {
        return userService.saveRole(role);
    }
    
    //get all roles 
    @PreAuthorize("hasRole('ROLE_ADMINISTRATEUR')")
    @GetMapping("/roles")
    public List<Role> getRoles()
    {
        return userService.getRoles();
    }
    
    //get a role with its name 
    @PreAuthorize("hasRole('ROLE_ADMINISTRATEUR')")
    @GetMapping("/roles/{name}")
    public Role getRole(@PathVariable String name)
    {
        return userService.getRole(name);
    }
    
    //update an existing role by id
    @PreAuthorize("hasRole('ROLE_ADMINISTRATEUR')")
    @PutMapping("roles/{id}")
	public Role updateRole(@PathVariable Long id,@RequestBody Role role)
	{
		role.setId(id);
		return userService.saveRole(role);
	}
	
    //delete a role by id
    @PreAuthorize("hasRole('ROLE_ADMINISTRATEUR')")
	@DeleteMapping("role/{id}")
	public void deleteRole(@PathVariable Long id)
	{
		 userService.deleteById(id);
	}

	
    //send the refresh token to get an access token when the old one expires
    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException 
    {
        String authorizationHeader = httpServletRequest.getHeader(JWTInfos.AUTH_HEADER);
        if(authorizationHeader != null && authorizationHeader.startsWith(JWTInfos.PREFIX))
        {
            try
            {
                String refresh_token = authorizationHeader.substring(JWTInfos.PREFIX.length());
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refresh_token);

                String userName = decodedJWT.getSubject();
                User user = userService.getUser(userName);
                List<String> listRoles = user.getRoles()
                        .stream()
                        .map(Role::getName)
                        .collect(Collectors.toList());

                String access_token = JWT.create()
                        .withSubject(user.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() +JWTInfos.EXPIRE_ACCES_TOKEN))
                        .withIssuer(httpServletRequest.getRequestURL().toString())
                        .withArrayClaim("roles", listRoles.toArray(new String[0]))
                        .sign(algorithm);

                Map<String, String> tokens =  new HashMap<>();
                tokens.put("access_token", access_token);
                tokens.put("refresh_token", refresh_token);
                httpServletResponse.setContentType(APPLICATION_JSON_VALUE);

                new ObjectMapper().writeValue(httpServletResponse.getOutputStream(), tokens);
                
            }catch (Exception exception)
            {
                httpServletResponse.setHeader("error", exception.getMessage());
                httpServletResponse.setStatus(FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error_message", exception.getMessage());
                httpServletResponse.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(httpServletResponse.getOutputStream(), error);
            }
        }else
        {
            throw new RuntimeException("The refresh token is missing");
        }
    }
}

