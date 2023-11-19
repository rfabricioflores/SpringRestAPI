package se.fabricioflores.springrestapi.projection;

public record CategoryDetail(
        Long id,
        String name,
        String symbol,
        String description
) { }
