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
import java.util.*;

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

    // This method has a bug, must be fixed quickly
    public Location createLocationWithCategories(AddLocationReq addLocationReq, Long userId) {

        Location location = new Location(
                addLocationReq.name(),
                addLocationReq.accessibility(),
                addLocationReq.description(),
                addLocationReq.coordinate(),
                userId
        );

        Set<Category> categories = new HashSet<>();

        addLocationReq
                .categories()
                .forEach(categoryId -> {
                    // Uses the EntityManager to create a proxy Category with the specified ID
                    // This way we avoid loading the whole category entity
                    Category categoryProxy = categoryRepo.findById(categoryId).orElseThrow();
                    categories.add(categoryProxy);
                });

        location.setCategories(categories);

        Location savedLocation = locationRepo.save(location);

        categories.forEach(category -> category.setLocations(Set.of(savedLocation)));

        return savedLocation;
    }

    public List<Location> getPublicLocations() {
        return locationRepo.getLocationsByAccessibility(Accessibility.PUBLIC);
    }

    public Optional<Location> getPublicLocation(Long locationId) {
        return locationRepo.getLocationByIdAndAccessibility(locationId, Accessibility.PUBLIC);
    }

    public List<Location> getPublicLocationsByCategory(Long categoryId) {
        return locationRepo.getLocationsByCategoryAndAccessibility(
                categoryId,
                Accessibility.PUBLIC
        );
    }

    public List<Location> getLocationsOfUser(Long userId) {
        return locationRepo.getLocationsByUserId(userId);
    }

    public List<Location> getLocationsWithinRadiusFromCoordinate(Point<G2D> coordinate, double radius) {
        return locationRepo.getLocationsWithinRadius(coordinate, radius);
    }
}
