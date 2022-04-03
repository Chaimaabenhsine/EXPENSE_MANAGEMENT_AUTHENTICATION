package projet.micro.auth.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import projet.micro.auth.model.User;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    User findByUsername(String userName);
}

