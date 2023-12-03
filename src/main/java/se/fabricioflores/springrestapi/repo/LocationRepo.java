package se.fabricioflores.springrestapi.repo;

import org.geolatte.geom.G2D;
import org.geolatte.geom.Point;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import se.fabricioflores.springrestapi.model.Accessibility;
import se.fabricioflores.springrestapi.model.Category;
import se.fabricioflores.springrestapi.model.Location;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface LocationRepo extends ListCrudRepository<Location, Long> {

    List<Location> getLocationsByAccessibility(Accessibility accessibility);

    Optional<Location> getLocationByIdAndAccessibility(Long locationId, Accessibility accessibility);

    List<Location> getLocationsByAccessibilityAndCategoriesIn(
            Accessibility accessibility,
            Collection<Set<Category>> categories
    );

    List<Location> getLocationsByUserId(Long userId);

    @Query("SELECT l FROM Location l WHERE ST_Within(l.coordinate, ST_Buffer(:centerCoordinate, :radius)) = true")
    List<Location> getLocationsWithinRadius(
            @Param("centerCoordinate") Point<G2D> centerCoordinate,
            @Param("radius") double radius
    );

}
