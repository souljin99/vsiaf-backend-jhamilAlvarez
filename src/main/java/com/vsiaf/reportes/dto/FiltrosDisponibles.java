package com.vsiaf.reportes.dto;

import java.util.List;

public record FiltrosDisponibles(List<Opcion> grupos, List<Opcion> oficinas, List<Opcion> responsables,
                                 List<String> estados) {
}
