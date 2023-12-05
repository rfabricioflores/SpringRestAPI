package se.fabricioflores.springrestapi.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Point;
import se.fabricioflores.springrestapi.databind.Point2DJsonMapper;
import se.fabricioflores.springrestapi.model.Accessibility;

import java.util.List;

public record LocationEditDto (
        @NotNull @PositiveOrZero Long id,
        String name,
        List<Long> categories,
        Accessibility accessibility,
        String description,
        @JsonSerialize(using = Point2DJsonMapper.Point2DSerializer.class)
        @JsonDeserialize(using = Point2DJsonMapper.Point2DDeserializer.class)
        Point<G2D> coordinate
) {
}
