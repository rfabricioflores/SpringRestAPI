package se.fabricioflores.springrestapi.repo;

import org.springframework.data.repository.ListCrudRepository;
import se.fabricioflores.springrestapi.model.Location;

public interface LocationRepo extends ListCrudRepository<Location, Long> {

}
