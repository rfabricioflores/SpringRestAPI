package se.fabricioflores.springrestapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
}
