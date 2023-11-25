package se.fabricioflores.springrestapi.repo;

import org.springframework.data.repository.ListCrudRepository;
import se.fabricioflores.springrestapi.model.Role;

public interface RoleRepo extends ListCrudRepository<Role, Long> {
}
