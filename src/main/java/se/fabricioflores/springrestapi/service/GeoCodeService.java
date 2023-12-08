package se.fabricioflores.springrestapi.service;

import org.geolatte.geom.G2D;
import org.geolatte.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
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

    @Retryable(
            retryFor = RestClientException.class,
            backoff = @Backoff(delay = 1000),
            maxAttempts = 2
    )
    public GeoResponse convertCoordinateToAdress(Point<G2D> coordinate) {
        Double latitude = coordinate.getPosition().getLat();
        Double longitude = coordinate.getPosition().getLon();

        return restClient
                .get()
                .uri("/reverse?lat={latitude}&lon={longitude}", latitude, longitude)
                .retrieve()
                .body(GeoResponse.class);
    }

    @Recover
    public GeoResponse recover(RestClientException exception, Point<G2D> ignoredCoordinate) {
        System.out.println(exception.getMessage());
        return new GeoResponse(null, "Geocode service is down, please try again later!");
    }

}
