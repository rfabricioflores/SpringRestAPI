package se.fabricioflores.springrestapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.fabricioflores.springrestapi.model.Accessibility;
import se.fabricioflores.springrestapi.model.Location;
import se.fabricioflores.springrestapi.service.LocationService;

@RestController
@RequestMapping({"/location", "/location/"})
public class LocationController {
    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping()
    public ResponseEntity<Object> getLocations() {
        var locations = locationService.getAllLocations();
        return ResponseEntity.ok(locations);
    }

    @PostMapping
    public ResponseEntity<Object> addLocation(@RequestBody Location body) {
        var location = locationService.createLocation(body);
        return ResponseEntity.ok(location);
    }
}
