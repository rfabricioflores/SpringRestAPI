package se.fabricioflores.springrestapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import se.fabricioflores.springrestapi.dto.AddLocationReq;
import se.fabricioflores.springrestapi.service.IUserService;
import se.fabricioflores.springrestapi.service.LocationService;

import java.util.Optional;

@RestController
@RequestMapping({"/locations", "/locations/"})
public class LocationController {
    private final LocationService locationService;
    private final IUserService userService;

    public LocationController(
            LocationService locationService,
            IUserService userService
    ) {
        this.locationService = locationService;
        this.userService = userService;
    }

    @GetMapping()
    public ResponseEntity<Object> getLocations() {
        var locations = locationService.getAllLocations();
        return ResponseEntity.ok(locations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getLocation(@PathVariable(name = "id") Long id) {
        var location =  locationService.getLocation(id).orElseThrow(() -> new RuntimeException("Location does not exist"));
        return ResponseEntity.ok().body(location);
    }

    // ** Public locations
    @GetMapping("/public")
    public ResponseEntity<Object> getPublicLocations(
            @RequestParam(name = "category") Optional<Long> categoryId
    ) {
        if(categoryId.isPresent()) {
            return ResponseEntity.ok().body(locationService.getPublicLocationsByCategory(categoryId.get()));
        } else {
            return ResponseEntity.ok().body(locationService.getPublicLocations());
        }
    }

    @GetMapping("/public/{locationId}")
    public ResponseEntity<Object> getPublicLocation(@PathVariable(name = "locationId") Long id) {
        var location = locationService.getPublicLocation(id).orElseThrow(() -> new RuntimeException("Location does not exist"));
        return ResponseEntity.ok().body(location);
    }

    // ** Create a new location
    @PostMapping
    public ResponseEntity<Object> addLocation(@RequestBody AddLocationReq body) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userService.getUser(username).orElseThrow();
        var location = locationService.createLocationWithCategories(body, user.getId());

       return ResponseEntity.ok(location);
    }
}
