package projet.micro.auth.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import projet.micro.auth.model.Role;
import projet.micro.auth.model.User;
import projet.micro.auth.repo.RoleRepository;
import projet.micro.auth.repo.UserRepository;

@Service @Transactional @RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService 
{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private final PasswordEncoder passwordEncoder;


    @Override
    public User saveUser(User user) 
    {
    	//verify if user name already exists
		User userr=userRepository.findByUsername(user.getUsername());
		if(userr!=null) throw new RuntimeException("username entred already exist");
		
		//Crypt the password entered
        String passwordToCrypt=user.getPassword();
        user.setPassword(passwordEncoder.encode(passwordToCrypt));
        
        //save user
        return userRepository.save(user);
    }
    
    
    
    @Override
    public User updateUser(User user, Long id) 
    {
        user.setId(id);
        return userRepository.save(user);
    }

    
    
    @Override
    public User getUser(String userName) 
    {
        return userRepository.findByUsername(userName);
    }

    
    
    @Override
    public List<User> getUsers() 
    {
         return userRepository.findAll();
    }

    
    
    @Override
    public Role saveRole(Role role) 
    {
        return roleRepository.save(role);
    }

    
    /*
    @Override
    public void addRoleToUser(String userName, String roleName) {
        User user = userRepository.findByUsername(userName);
        Role role = roleRepository.findByName(roleName) ;
        user.getRoles().add(role);
    }
    */

    
    @Override
    public List<Role> getRoles() 
    {
        return roleRepository.findAll();
    }

    
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException 
    {
        User user = userRepository.findByUsername(userName);

        if(user == null)  throw new UsernameNotFoundException("User not found in the database!");
       
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles()
                .forEach(role -> {
                    authorities.add(new SimpleGrantedAuthority(role.getName()));
                });
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
	public Role getRole(String name) 
	{
		return roleRepository.findByName(name);
	}
}