package projet.micro.auth.controller;

import java.util.List;

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
import projet.micro.auth.model.Role;
import projet.micro.auth.service.RoleService;

@RestController @RequestMapping("/api/roles") @RequiredArgsConstructor
public class RoleController {
	
	private final RoleService roleService;
	    

    @PreAuthorize("hasRole('ROLE_ADMINISTRATEUR')")
    @PostMapping
    public Role saveRole(Role role)
    {
        return roleService.saveRole(role);
    }
    
    @PreAuthorize("hasRole('ROLE_ADMINISTRATEUR')")
    @GetMapping
    public List<Role> getRoles()
    {
        return roleService.getRoles();
    }
    

    @PreAuthorize("hasRole('ROLE_ADMINISTRATEUR')")
    @GetMapping("/{name}")
    public Role getRole(@PathVariable String name)
    {
        return roleService.getRole(name);
    }
    
    @PreAuthorize("hasRole('ROLE_ADMINISTRATEUR')")
    @PutMapping("/{id}")
	public Role updateRole(@PathVariable Long id,@RequestBody Role role)
	{
		role.setId(id);
		return roleService.saveRole(role);
	}
	
    @PreAuthorize("hasRole('ROLE_ADMINISTRATEUR')")
	@DeleteMapping("/{id}")
	public void deleteRole(@PathVariable Long id)
	{
    	roleService.deleteById(id);
	}

}
