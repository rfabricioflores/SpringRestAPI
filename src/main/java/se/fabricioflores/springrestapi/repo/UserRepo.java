package se.fabricioflores.springrestapi.repo;

import org.springframework.data.repository.ListCrudRepository;
import se.fabricioflores.springrestapi.model.User;

import java.util.Optional;

public interface UserRepo extends ListCrudRepository<User, Long> {
    Optional<User> findOneByUsername(String username);
}
