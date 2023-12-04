package se.fabricioflores.springrestapi.service;

import jakarta.transaction.Transactional;
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

import java.util.*;
import java.util.stream.Collectors;

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

    @Transactional
    public Location createLocationWithCategories(AddLocationReq addLocationReq, Long userId) {

        Location location = new Location(
                addLocationReq.name(),
                addLocationReq.accessibility(),
                addLocationReq.description(),
                addLocationReq.coordinate(),
                userId
        );

        Set<Category> categories = new HashSet<>(categoryRepo.findAllById(addLocationReq.categories()));

        location.setCategories(categories);

        for (Category category : categories) {
            category.getLocations().add(location);
        }

        return locationRepo.save(location);
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
