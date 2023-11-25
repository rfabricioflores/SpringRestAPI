package se.fabricioflores.springrestapi.repo;

import org.springframework.data.repository.ListCrudRepository;
import se.fabricioflores.springrestapi.model.User;

public interface UserRepo extends ListCrudRepository<User, Long> {
}
