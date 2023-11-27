package se.fabricioflores.springrestapi.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record AddUserReq(
        @NotNull String username,
        @NotNull String password,
        List<String> roles
) {
}
