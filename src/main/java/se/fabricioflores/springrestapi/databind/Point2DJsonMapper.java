package se.fabricioflores.springrestapi.databind;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Point;

import java.io.IOException;

import static org.geolatte.geom.builder.DSL.g;
import static org.geolatte.geom.builder.DSL.point;
import static org.geolatte.geom.crs.CoordinateReferenceSystems.WGS84;

public class Point2DJsonMapper {

    public static class Point2DSerializer extends JsonSerializer<Point<G2D>> {

        @Override
        public void serialize(
                Point<G2D> value,
                JsonGenerator gen,
                SerializerProvider serializerProvider
        ) throws IOException {
            gen.writeStartObject();
            gen.writeNumberField("lat", value.getPosition().getLat());
            gen.writeNumberField("lon", value.getPosition().getLon());
            gen.writeEndObject();
        }

    }

    public static class Point2DDeserializer extends JsonDeserializer<Point<G2D>> {

        @Override
        public Point<G2D> deserialize(
                JsonParser jsonParser,
                DeserializationContext deserializationContext
        ) throws IOException, NumberFormatException {
            JsonNode node = jsonParser.getCodec().readTree(jsonParser);

            double lat = Double.parseDouble(node.get("lat").asText());
            double lon = Double.parseDouble(node.get("lon").asText());

            if(lat > 90 || lat < -90) throw new IllegalArgumentException("Not valid latitude");
            if(lon > 180 || lon < -180) throw new IllegalArgumentException("Not valid longitude");

            return point(WGS84, g(lon, lat));
        }

    }
}
