package projet.micro.auth.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import projet.micro.auth.model.Role;
import projet.micro.auth.model.User;

public interface UserService 
{
	User saveUser(User user);
    User updateUser(User user, Long id);
    User getUser(String userName);
    List<User> getUsers();
    Role saveRole(Role role);
    List<Role> getRoles();
    UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException;
	void deleteById(Long id);
	Role getRole(String name);

}
