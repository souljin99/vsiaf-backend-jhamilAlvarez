package com.vsiaf.reportes.controller;

import com.vsiaf.reportes.dto.ActivoRequest;
import com.vsiaf.reportes.dto.ActivoRow;
import com.vsiaf.reportes.dto.EstadoActivoRequest;
import com.vsiaf.reportes.service.ActivoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/activos")
@Tag(name = "Activos Fijos (alimentan los reportes)", description = "Registro de activos para que aparezcan en los reportes. "
        + "El codigo se genera solo. Grupo, oficina y responsable se envian por su codigo (ver /api/reportes/filtros).")
public class ActivoController {

    private final ActivoService service;

    public ActivoController(ActivoService service) {
        this.service = service;
    }

    @GetMapping("/{codActivo}")
    @Operation(summary = "ACTIVO - Consultar por codigo")
    public ActivoRow obtener(@PathVariable String codActivo) {
        return service.obtener(codActivo);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "ACTIVO - Nuevo + Guardar")
    public ActivoRow crear(@Valid @RequestBody ActivoRequest req) {
        return service.crear(req);
    }

    @PutMapping("/{codActivo}")
    @Operation(summary = "ACTIVO - Modificar + Guardar")
    public ActivoRow modificar(@PathVariable String codActivo, @Valid @RequestBody ActivoRequest req) {
        return service.modificar(codActivo, req);
    }

    @PatchMapping("/{codActivo}/estado")
    @Operation(summary = "ACTIVO - Cambiar estado (BUENO, REGULAR, MALO)")
    public ActivoRow cambiarEstado(@PathVariable String codActivo, @Valid @RequestBody EstadoActivoRequest req) {
        return service.cambiarEstado(codActivo, req.estado());
    }

    @DeleteMapping("/{codActivo}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "ACTIVO - Eliminar")
    public void eliminar(@PathVariable String codActivo) {
        service.eliminar(codActivo);
    }
}
