package se.fabricioflores.springrestapi.service;

import org.geolatte.geom.G2D;
import org.geolatte.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.fabricioflores.springrestapi.dto.AddLocationReq;
import se.fabricioflores.springrestapi.model.Accessibility;
import se.fabricioflores.springrestapi.model.Category;
import se.fabricioflores.springrestapi.model.Location;
import se.fabricioflores.springrestapi.repo.CategoryRepo;
import se.fabricioflores.springrestapi.repo.LocationRepo;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class LocationService {
    private final LocationRepo locationRepo;
    private final CategoryRepo categoryRepo;

    @Autowired
    public LocationService(LocationRepo locationRepo, CategoryRepo categoryRepo) {
        this.locationRepo = locationRepo;
        this.categoryRepo = categoryRepo;
    }

    public List<Location> getAllLocations() {
        return locationRepo.findAll();
    }

    public Optional<Location> getLocation(Long id) {
        return locationRepo.findById(id);
    }

    public Location createLocation(Location location) {
        return locationRepo.save(location);
    }

    public Location createLocationWithCategories(AddLocationReq addLocationReq, Long userId) {
        Location location = new Location(
                addLocationReq.name(),
                addLocationReq.accessibility(),
                addLocationReq.description(),
                addLocationReq.coordinate(),
                userId
        );

        addLocationReq
                .categories()
                .forEach(categoryId -> {
                    // Uses the EntityManager to create a proxy Category with the specified ID
                    // This way we avoid loading the whole category entity
                    Category categoryProxy = categoryRepo.findById(categoryId).orElseThrow();
                    location.getCategories().add(categoryProxy);
                });

        return locationRepo.save(location);
    }

    public List<Location> getPublicLocations() {
        return locationRepo.getLocationsByAccessibility(Accessibility.PUBLIC);
    }

    public Optional<Location> getPublicLocation(Long locationId) {
        return locationRepo.getLocationByIdAndAccessibility(locationId, Accessibility.PUBLIC);
    }

    public List<Location> getPublicLocationsByCategory(Long categoryId) {
        var optCategory = categoryRepo.findById(categoryId);
        if(optCategory.isEmpty()) throw new RuntimeException("Category doesn't exist");

        return locationRepo.getLocationsByAccessibilityAndCategoriesIn(
                Accessibility.PUBLIC,
                Set.of(Collections.singleton(optCategory.get()))
        );
    }

    public List<Location> getLocationsOfUser(Long userId) {
        return locationRepo.getLocationsByUserId(userId);
    }

    public List<Location> getLocationsWithinRadiusFromCoordinate(Point<G2D> coordinate, double radius) {
        return locationRepo.getLocationsWithinRadius(coordinate, radius);
    }
}
