package se.fabricioflores.springrestapi.repo;

import org.springframework.data.repository.ListCrudRepository;
import se.fabricioflores.springrestapi.model.Role;

import java.util.Optional;

public interface RoleRepo extends ListCrudRepository<Role, Long> {
    Optional<Role> findRoleByName(String name);
}
