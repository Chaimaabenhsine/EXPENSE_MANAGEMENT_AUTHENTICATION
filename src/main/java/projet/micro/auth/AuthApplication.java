package projet.micro.auth;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import projet.micro.auth.model.Role;
import projet.micro.auth.model.User;
import projet.micro.auth.service.UserService;

@SpringBootApplication
public class AuthApplication{

    @Autowired
    UserService userService;
    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
	CommandLineRunner run(UserService userService)
	{
		return args ->{
			Role role1= userService.saveRole(new Role(null,"ROLE_COLLABORATEUR","test"));
			Role role2= userService.saveRole(new Role(null,"ROLE_RESPONSABLE","test"));
			Role role3= userService.saveRole(new Role(null,"ROLE_ADMINISTRATEUR","test"));
			Role role4= userService.saveRole(new Role(null,"ROLE_DIRECTEUR","test"));
			
			Collection <Role> roles=new ArrayList<>();
			roles.add(role1);
			
			Collection <Role> roles1=new ArrayList<>();
			roles1.add(role1);
			roles1.add(role3);
			userService.saveUser(new User(null,"Mahmoud","Atif","login","matif@gmail.com","login",roles));
			userService.saveUser(new User(null,"Khadija","Karimi","login1","kkarimi@gmail.com","login1",roles1));
			
			//userService.addRoleToUser("login", "ROLE_ADMINISTRATEUR");
			//userService.addRoleToUser("login1", "ROLE_RESPONSABLE");
		};
		
	}




}