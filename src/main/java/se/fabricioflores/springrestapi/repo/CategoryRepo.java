package se.fabricioflores.springrestapi.repo;

import org.springframework.data.repository.ListCrudRepository;
import se.fabricioflores.springrestapi.model.Category;
import se.fabricioflores.springrestapi.projection.CategoryDetail;

import java.util.List;
import java.util.Optional;

public interface CategoryRepo extends ListCrudRepository<Category, Long> {
    List<CategoryDetail> findAllBy();
    Optional<CategoryDetail> findOneById(Long id);
}
