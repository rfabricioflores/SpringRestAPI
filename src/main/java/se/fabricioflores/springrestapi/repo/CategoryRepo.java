package se.fabricioflores.springrestapi.repo;

import org.springframework.data.repository.ListCrudRepository;
import se.fabricioflores.springrestapi.model.Category;

public interface CategoryRepo extends ListCrudRepository<Category, Long> { }
