package se.fabricioflores.springrestapi.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Point;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import se.fabricioflores.springrestapi.dto.LocationDto;
import se.fabricioflores.springrestapi.service.IUserService;
import se.fabricioflores.springrestapi.service.LocationService;

import java.util.Optional;

import static org.geolatte.geom.builder.DSL.g;
import static org.geolatte.geom.builder.DSL.point;
import static org.geolatte.geom.crs.CoordinateReferenceSystems.WGS84;

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

    // ** Create a new location
    @PostMapping
    public ResponseEntity<Object> addLocation(@RequestBody LocationDto body) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userService.getUser(username).orElseThrow();
        var location = locationService.createLocation(body, user.getId());

        return ResponseEntity.ok(location);
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

    // ** User locations
    @GetMapping("/user")
    public ResponseEntity<Object> getLocationsOfUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userService.getUser(username).orElseThrow();
        var locations = locationService.getLocationsOfUser(user.getId());
        return ResponseEntity.ok().body(locations);
    }

    // ** Near by locations
    @GetMapping("/nearby")
    public ResponseEntity<Object> getLocationsNearby(
            @RequestParam @Valid @NotNull @NotEmpty Double lat,
            @RequestParam @Valid @NotNull @NotEmpty Double lon
    ) {
        if(lat > 90 || lat < -90) throw new IllegalArgumentException("Not valid latitude");
        if(lon > 180 || lon < -180) throw new IllegalArgumentException("Not valid longitude");

        Point<G2D> coordinate = point(WGS84, g(lon, lat));

        var locations = locationService.getLocationsWithinRadiusFromCoordinate(coordinate, 10000.0);
        return ResponseEntity.ok().body(locations);
    }

}
