package se.fabricioflores.springrestapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.fabricioflores.springrestapi.model.Category;
import se.fabricioflores.springrestapi.projection.CategoryDetail;
import se.fabricioflores.springrestapi.repo.CategoryRepo;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    private final CategoryRepo categoryRepo;

    @Autowired
    public CategoryService(CategoryRepo categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    public List<CategoryDetail> getAllCategories() {
        return categoryRepo.findAllBy();
    }

    public Optional<CategoryDetail> getCategoryById(Long id) {
        return categoryRepo.findOneById(id);
    }

    public Category addCategory(Category category) {
        return categoryRepo.save(category);
    }
}
