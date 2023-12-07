package se.fabricioflores.springrestapi.service;

import org.geolatte.geom.G2D;
import org.geolatte.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import se.fabricioflores.springrestapi.dto.GeoResponse;

@Service
public class GeoCodeService {
    private final RestClient restClient;

    @Autowired
    public GeoCodeService(Environment environment, RestClient.Builder restClientBuilder) {
        var geoCodeUrl = environment.getProperty("geocode_url", String.class);
        if (geoCodeUrl == null || geoCodeUrl.isEmpty())
            throw new RuntimeException("Missing geocode_url environment variable");

        this.restClient = restClientBuilder.baseUrl(geoCodeUrl).build();
    }

    public GeoResponse convertCoordinateToAdress(Point<G2D> coordinate) {
        System.out.println("service");
        Double latitude = coordinate.getPosition().getLat();
        Double longitude = coordinate.getPosition().getLon();

        return restClient
                .get()
                .uri("/reverse?lat={latitude}&lon={longitude}", latitude, longitude)
                .retrieve()
                .body(GeoResponse.class);
    }
}
