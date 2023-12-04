package se.fabricioflores.springrestapi.repo;

import org.geolatte.geom.G2D;
import org.geolatte.geom.Point;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import se.fabricioflores.springrestapi.model.Accessibility;
import se.fabricioflores.springrestapi.model.Location;

import java.util.List;
import java.util.Optional;

public interface LocationRepo extends ListCrudRepository<Location, Long> {

    List<Location> getLocationsByAccessibility(Accessibility accessibility);

    Optional<Location> getLocationByIdAndAccessibility(Long locationId, Accessibility accessibility);

    @Query("SELECT l FROM Location l JOIN l.categories c WHERE c.id = :categoryId AND l.accessibility = :accessibility")
    List<Location> getLocationsByCategoryAndAccessibility(
            @Param("categoryId") Long categoryId,
            @Param("accessibility") Accessibility accessibility
    );

    List<Location> getLocationsByUserId(Long userId);

    @Query("SELECT l FROM Location l WHERE ST_Distance_Sphere(l.coordinate, :centerCoordinate) <= :radius")
    List<Location> getLocationsWithinRadius(
            @Param("centerCoordinate") Point<G2D> centerCoordinate,
            @Param("radius") double radius
    );

}
