package projet.micro.auth.service;

import java.util.List;

import projet.micro.auth.model.Role;

public interface RoleService {

	Role saveRole(Role role);

	List<Role> getRoles();

	Role getRole(String name);

	void deleteById(Long id);

}
