package br.com.areahub.app.dto;

import jakarta.validation.constraints.NotBlank;

public record RegisterRequestDTO(@NotBlank String username, @NotBlank String email, @NotBlank String password) {

}
