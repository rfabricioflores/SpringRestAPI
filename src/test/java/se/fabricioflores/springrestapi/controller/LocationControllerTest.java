package se.fabricioflores.springrestapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Point;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import se.fabricioflores.springrestapi.config.JwtUtil;
import se.fabricioflores.springrestapi.config.SecurityConfig;
import se.fabricioflores.springrestapi.model.Accessibility;
import se.fabricioflores.springrestapi.model.Location;
import se.fabricioflores.springrestapi.service.IUserService;
import se.fabricioflores.springrestapi.service.LocationService;

import java.util.List;
import java.util.Set;

import static org.geolatte.geom.builder.DSL.g;
import static org.geolatte.geom.builder.DSL.point;
import static org.geolatte.geom.crs.CoordinateReferenceSystems.WGS84;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration
@Import(SecurityConfig.class)
@WebMvcTest(controllers = LocationController.class)
public class LocationControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    LocationService locationService;

    @MockBean
    IUserService userService;

    @MockBean
    JwtUtil jwtUtil;


    @Test
    @DisplayName("Should respond with location list")
    void shouldRespondWithListOfLocations() throws Exception {
        Location location = new Location();
        Point<G2D> point = point(WGS84, g(23.3223, 12.23321));

        location.setName("Cool place");
        location.setDescription("Must come back here");
        location.setCoordinate(point);
        location.setAccessibility(Accessibility.PUBLIC);
        location.setCategories(Set.of());

        List<Location> locations = List.of(location);

        Mockito.when(locationService.getPublicLocations()).thenReturn(locations);

        String expected = objectMapper.writeValueAsString(locations);

        mockMvc
                .perform(get("/locations/public"))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(expected)
                );
    }
}