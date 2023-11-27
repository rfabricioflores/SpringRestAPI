package se.fabricioflores.springrestapi.dto;

import jakarta.validation.constraints.NotNull;

public record UserRegisterReq(
        @NotNull String username,
        @NotNull String password
) {
}
