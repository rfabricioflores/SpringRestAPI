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
import se.fabricioflores.springrestapi.dto.GeoResponse;
import se.fabricioflores.springrestapi.service.GeoCodeService;
import se.fabricioflores.springrestapi.service.IUserService;

import static org.geolatte.geom.builder.DSL.g;
import static org.geolatte.geom.builder.DSL.point;
import static org.geolatte.geom.crs.CoordinateReferenceSystems.WGS84;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration
@Import(SecurityConfig.class)
@WebMvcTest(GeoCodeController.class)
public class GeoCodeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private GeoCodeService geoCodeService;

    @MockBean
    IUserService userService;

    @MockBean
    JwtUtil jwtUtil;


    @Test
    @DisplayName("Should respond with error from service when adress not found")
    void shouldHandleErrorWhenNoAdressFound() throws Exception {

        Point<G2D> point = point(WGS84, g(12.23321, 23.3223));

        Mockito
                .when(geoCodeService.convertCoordinateToAdress(point))
                .thenReturn(new GeoResponse(null, "error"));

        var expectedJson = objectMapper.createObjectNode();
        expectedJson.put("error", "error");

        String expected = objectMapper.writeValueAsString(expectedJson);

        mockMvc
                .perform(get("/geo?lat=23.3223&lon=12.23321"))
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(expected)
                );
    }
}
