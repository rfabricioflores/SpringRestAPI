package se.fabricioflores.springrestapi.dto;

import jakarta.validation.constraints.NotNull;

public record AddCategoryReq(
        @NotNull String name,
        @NotNull String symbol,
        @NotNull String description
) {
}
