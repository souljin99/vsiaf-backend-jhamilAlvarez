package com.vsiaf.reportes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record EstadoActivoRequest(
        @NotBlank(message = "El estado es obligatorio")
        @Pattern(regexp = "(?i)BUENO|REGULAR|MALO", message = "Estado valido: BUENO, REGULAR o MALO")
        String estado) {
}
