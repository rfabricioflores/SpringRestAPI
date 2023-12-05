package se.fabricioflores.springrestapi.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import se.fabricioflores.springrestapi.dto.LocationDto;
import se.fabricioflores.springrestapi.dto.LocationEditDto;
import se.fabricioflores.springrestapi.model.Accessibility;
import se.fabricioflores.springrestapi.model.Category;
import se.fabricioflores.springrestapi.model.Location;
import se.fabricioflores.springrestapi.repo.CategoryRepo;
import se.fabricioflores.springrestapi.repo.LocationRepo;

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

    @Transactional
    public Location createLocation(LocationDto data, Long userId) {

        Location location = new Location(
                data.name(),
                data.accessibility(),
                data.description(),
                data.coordinate(),
                userId
        );

        if (data.categories() != null && !data.categories().isEmpty()) {
            Set<Category> categories = new HashSet<>(categoryRepo.findAllById(data.categories()));

            location.setCategories(categories);

            for (Category category : categories) {
                category.getLocations().add(location);
            }
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

    public List<Location> getLocationsWithinRadiusFromCoordinate(Point<G2D> coordinate, double radiusInMeters) {
        return locationRepo.getLocationsWithinRadius(coordinate, radiusInMeters);
    }

    @Transactional
    public Location updateLocation(LocationEditDto data, Long userId) {
        Location location = locationRepo.findById(data.id()).orElseThrow();

        if (!location.getUserId().equals(userId))
            throw new AccessDeniedException("You don't have access to this location");

        if (data.name() != null) location.setName(data.name());
        if (data.accessibility() != null) location.setAccessibility(data.accessibility());
        if (data.description() != null) location.setDescription(data.description());
        if (data.coordinate() != null) location.setCoordinate(data.coordinate());

        if (data.categories() != null) {
            // Retrieve categories based on the provided category ids list
            Set<Category> newCategories = new HashSet<>(categoryRepo.findAllById(data.categories()));

            // Update bidirectional relationships
            for (Category category : location.getCategories()) {
                category.getLocations().remove(location);
            }

            // Clear location categories and update them with the new ones
            location.getCategories().clear();
            location.getCategories().addAll(newCategories);

            // Update bidirectional relationships
            for (Category category : newCategories) {
                category.getLocations().add(location);
            }
        }

        return locationRepo.save(location);
    }

    @Transactional
    public void deleteLocation(Long locationId, Long userId) {
        // Checks if locations exists
        Location location = locationRepo.findById(locationId).orElseThrow(() -> new EntityNotFoundException("Not valid location id"));

        // Check if the user logged in owns this location
        if (!location.getUserId().equals(userId))
            throw new AccessDeniedException("You don't have access to this location");

        // Update bidirectional relationships
        // This could have been achieved with CascadeType.REMOVE but i decided to do it this way
        for (Category category : location.getCategories()) {
            category.getLocations().remove(location);
        }

        location.getCategories().clear();

        locationRepo.delete(location);
    }
}
