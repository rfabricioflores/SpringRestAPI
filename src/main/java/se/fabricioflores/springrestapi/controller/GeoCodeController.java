package se.fabricioflores.springrestapi.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import se.fabricioflores.springrestapi.service.GeoCodeService;

import static org.geolatte.geom.builder.DSL.g;
import static org.geolatte.geom.builder.DSL.point;
import static org.geolatte.geom.crs.CoordinateReferenceSystems.WGS84;

@RestController
@RequestMapping("/geo")
public class GeoCodeController {

    private final GeoCodeService geoCodeService;

    @Autowired
    public GeoCodeController(GeoCodeService geoCodeService) {
        this.geoCodeService = geoCodeService;
    }

    @GetMapping()
    public ResponseEntity<Object> getAdressFromCoordinate(
            @RequestParam() @Valid @NotNull Double lat,
            @RequestParam() @Valid @NotNull Double lon
    ) {
        if(lat > 90 || lat < -90) throw new IllegalArgumentException("Not valid latitude");
        if(lon > 180 || lon < -180) throw new IllegalArgumentException("Not valid longitude");

        var data = geoCodeService.convertCoordinateToAdress(point(WGS84, g(lon, lat)));
        return ResponseEntity.ok().body(data.address());
    }
}
