package projet.micro.auth.service;

import java.util.List;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import projet.micro.auth.model.Role;
import projet.micro.auth.repo.RoleRepository;

@Service @Transactional @RequiredArgsConstructor
public class RoleServiceImpl implements RoleService  {


    private final RoleRepository roleRepository;
    

    
    @Override
    public Role saveRole(Role role) 
    {
        return roleRepository.save(role);
    }


    
    @Override
	@Transactional(readOnly=true)
    public List<Role> getRoles() 
    {
        return roleRepository.findAll();
    }

    
   	@Override
   	public void deleteById(Long id) 
   	{
   		roleRepository.deleteById(id);	
   	}

   	
   	@Override
	@Transactional(readOnly=true)
   	public Role getRole(String name) 
   	{
   		return roleRepository.findByName(name);
   	}
}
