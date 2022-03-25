package projet.micro.auth.repo;


import org.springframework.data.jpa.repository.JpaRepository;

import projet.micro.auth.model.Role;

public interface RoleRepository extends JpaRepository<Role,Long> {
    Role findByName(String roleName);
}