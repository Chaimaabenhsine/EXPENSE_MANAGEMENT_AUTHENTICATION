package projet.micro.auth.controller;


import java.io.IOException;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import projet.micro.auth.model.User;
import projet.micro.auth.service.UserService;


@RestController @RequestMapping("/api/users") @RequiredArgsConstructor
public class UserController 
{
	
    private final UserService userService;
    
    @PreAuthorize("hasRole('ROLE_ADMINISTRATEUR')")
    @GetMapping
    public List<User> getUsers()
    {
        return userService.getUsers();
    }

    @PreAuthorize("hasRole('ROLE_ADMINISTRATEUR')")
    @GetMapping("/{username}")
    public User getUser(@PathVariable String username)
    {
        return userService.getUser(username);
    }
    
    @PreAuthorize("hasRole('ROLE_ADMINISTRATEUR')")
    @PostMapping
    public User saveUser(@Valid @RequestBody User user)
    {
          return userService.saveUser(user);
    }

    @PreAuthorize("hasRole('ROLE_ADMINISTRATEUR')")
    @PutMapping("/{id}")
	public User updateUser(@PathVariable Long id,@RequestBody User user)
	{
		return userService.updateUser(user,id);
	}
	
    @PreAuthorize("hasRole('ROLE_ADMINISTRATEUR')")
	@DeleteMapping("/{id}")
	public void deleteUser(@PathVariable Long id)
	{
		 userService.deleteById(id);
	}
   
	
    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException 
    {
    	System.out.print("im inside the controller");
       userService.refreshToken(httpServletRequest, httpServletResponse);
    }
}

