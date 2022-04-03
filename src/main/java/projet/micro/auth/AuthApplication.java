package projet.micro.auth;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import projet.micro.auth.model.Role;
import projet.micro.auth.model.User;
import projet.micro.auth.service.RoleService;
import projet.micro.auth.service.UserService;

@SpringBootApplication 
public class AuthApplication{

    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

 
	
    @Bean 
    CommandLineRunner run(UserService userService, RoleService roleService)
    {
    	return args->{
    	    Role role1= roleService.saveRole(new Role(1L, "ROLE_ADMINISTRATEUR","test"));
    	    Role role2= roleService.saveRole(new Role(2L, "ROLE_RESPONSABLE","test"));
    	    
    	    List<Role> roles1=new ArrayList<>();
    	    roles1.add(role1);
    	    
    	    
    	    List<Role> roles2=new ArrayList<>();
    	    roles2.add(role2);
    	    
    	    
    	    userService.saveUser(new User(1L,"Mahmoud","Atif","login","Mahmoud@gmail.com","login",roles1));
    	    userService.saveUser(new User(2L,"Khadija","Karimi","login1","Kkarimi@gmail.com","login1",roles2));

    	    
    	};
    }



}