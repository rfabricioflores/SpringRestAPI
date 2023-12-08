package se.fabricioflores.springrestapi.dto;

public record GeoResponse(
    Address address,
    String error
) {
}

record Address(
        String road,
        String village,
        String municipality,
        String county,
        String postcode,
        String country
) {}