package se.fabricioflores.springrestapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.fabricioflores.springrestapi.model.Location;
import se.fabricioflores.springrestapi.repo.LocationRepo;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LocationService {
    private final LocationRepo locationRepo;

    @Autowired
    public LocationService(LocationRepo locationRepo) {
        this.locationRepo = locationRepo;
    }

    public List<Location> getAllLocations() {
        return locationRepo.findAll();
    }

    public Location addLocation(Location location) {
        return locationRepo.save(location);
    }
}
