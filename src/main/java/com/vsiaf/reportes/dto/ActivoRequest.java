package com.vsiaf.reportes.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/** Datos para registrar o modificar un activo. El codigo (SIS-0001...) se genera solo. */
public record ActivoRequest(
        @NotBlank(message = "La descripcion es obligatoria")
        @Size(max = 120, message = "La descripcion admite maximo 120 caracteres")
        String descripcion,

        @NotNull(message = "La fecha de incorporacion es obligatoria")
        @PastOrPresent(message = "La fecha de incorporacion no puede ser futura")
        LocalDate fechaIncorporacion,

        @NotNull(message = "El costo es obligatorio")
        @DecimalMin(value = "0.01", message = "El costo debe ser mayor a 0")
        @Digits(integer = 12, fraction = 2, message = "El costo admite hasta 2 decimales")
        BigDecimal costo,

        @NotBlank(message = "El estado es obligatorio")
        @Pattern(regexp = "(?i)BUENO|REGULAR|MALO", message = "Estado valido: BUENO, REGULAR o MALO")
        String estado,

        @NotNull(message = "codGrupo es obligatorio") Integer codGrupo,
        @NotNull(message = "codOfic es obligatorio") Integer codOfic,
        @NotNull(message = "codResp es obligatorio") Integer codResp) {
}
