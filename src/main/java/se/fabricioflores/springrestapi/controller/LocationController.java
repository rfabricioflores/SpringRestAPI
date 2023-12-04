package se.fabricioflores.springrestapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import se.fabricioflores.springrestapi.dto.AddLocationReq;
import se.fabricioflores.springrestapi.service.IUserService;
import se.fabricioflores.springrestapi.service.LocationService;

@RestController
@RequestMapping({"/location", "/location/"})
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
        var location =  locationService.getLocation(id).orElseThrow(() -> new RuntimeException("Location with id" + id + " does not exists"));
        return ResponseEntity.ok().body(location);
    }

    @PostMapping
    public ResponseEntity<Object> addLocation(@RequestBody AddLocationReq body) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userService.getUser(username).orElseThrow();
        var location = locationService.createLocationWithCategories(body, user.getId());

       return ResponseEntity.ok(location);
    }
}
