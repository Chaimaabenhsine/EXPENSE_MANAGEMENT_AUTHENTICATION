package projet.micro.auth.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import projet.micro.auth.model.User;

public interface UserRepository extends JpaRepository<User,Long> {

    User findByUsername(String userName);
}

