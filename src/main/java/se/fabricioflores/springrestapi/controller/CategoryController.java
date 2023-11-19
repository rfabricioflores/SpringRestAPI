package se.fabricioflores.springrestapi.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.fabricioflores.springrestapi.model.Category;
import se.fabricioflores.springrestapi.payload.AddCategoryReq;
import se.fabricioflores.springrestapi.service.CategoryService;

@RestController
@RequestMapping({"/categories", "/categories/"})
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // ** Get all categories
    @GetMapping
    public ResponseEntity<Object> getCategories() {
        var categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    // ** Get category
    @GetMapping({"/{id}", "/{id}/"})
    public ResponseEntity<Object> getCategory(
            @PathVariable(name = "id") Long id
    ) {
        var optCategory = categoryService.getCategoryById(id);

        if (optCategory.isEmpty()) return new ResponseEntity<>(new Object() {
            final public String error = "Not found";
        }, HttpStatus.NOT_FOUND);

        return ResponseEntity.ok(optCategory);
    }

    // ** Add category
    @PostMapping
    public ResponseEntity<Object> addCategory(
            @RequestBody
            @Valid
            AddCategoryReq body
    ) {
        Category newCategory = new Category();

        newCategory.setName(body.name());
        newCategory.setSymbol(body.symbol());
        newCategory.setDescription(body.description());

        var createdCategory = categoryService.addCategory(newCategory);

        return ResponseEntity.ok(createdCategory);
    }
}