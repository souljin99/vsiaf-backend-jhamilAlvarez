package com.vsiaf.reportes.controller;

import com.vsiaf.reportes.dto.*;
import com.vsiaf.reportes.service.ReporteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reportes")
@Tag(name = "Generar Reportes", description = "Reportes de activos fijos: general, por grupo contable, por oficina y por responsable. "
        + "Todos aceptan filtros opcionales (codOfic, codGrupo, codResp, estado, desde, hasta, corte con formato yyyy-MM-dd).")
public class ReporteController {

    private final ReporteService service;

    public ReporteController(ReporteService service) {
        this.service = service;
    }

    @GetMapping("/filtros")
    @Operation(summary = "REPORTES - Opciones para los filtros (grupos, oficinas, responsables, estados)")
    public FiltrosDisponibles filtros() {
        return service.filtros();
    }

    @GetMapping("/general")
    @Operation(summary = "REPORTES - Reporte general de activos fijos")
    public List<ActivoRow> general(
            @RequestParam(required = false) Integer codOfic,
            @RequestParam(required = false) Integer codGrupo,
            @RequestParam(required = false) Integer codResp,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate corte) {
        return service.general(new Filtro(codOfic, codGrupo, codResp, estado, desde, hasta, corte));
    }

    @GetMapping("/por-grupo")
    @Operation(summary = "REPORTES - Resumen por grupo contable")
    public List<ResumenRow> porGrupo(
            @RequestParam(required = false) Integer codOfic,
            @RequestParam(required = false) Integer codGrupo,
            @RequestParam(required = false) Integer codResp,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate corte) {
        return service.porGrupo(new Filtro(codOfic, codGrupo, codResp, estado, desde, hasta, corte));
    }

    @GetMapping("/por-oficina")
    @Operation(summary = "REPORTES - Resumen por oficina")
    public List<ResumenRow> porOficina(
            @RequestParam(required = false) Integer codOfic,
            @RequestParam(required = false) Integer codGrupo,
            @RequestParam(required = false) Integer codResp,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate corte) {
        return service.porOficina(new Filtro(codOfic, codGrupo, codResp, estado, desde, hasta, corte));
    }

    @GetMapping("/por-responsable")
    @Operation(summary = "REPORTES - Resumen por responsable")
    public List<ResumenRow> porResponsable(
            @RequestParam(required = false) Integer codOfic,
            @RequestParam(required = false) Integer codGrupo,
            @RequestParam(required = false) Integer codResp,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate corte) {
        return service.porResponsable(new Filtro(codOfic, codGrupo, codResp, estado, desde, hasta, corte));
    }

    @GetMapping("/resumen")
    @Operation(summary = "REPORTES - Totales generales (cantidad, costo, depreciacion, valor neto)")
    public ResumenGeneral resumen(
            @RequestParam(required = false) Integer codOfic,
            @RequestParam(required = false) Integer codGrupo,
            @RequestParam(required = false) Integer codResp,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate corte) {
        return service.resumen(new Filtro(codOfic, codGrupo, codResp, estado, desde, hasta, corte));
    }

    @GetMapping("/general/csv")
    @Operation(summary = "REPORTES - Descargar el reporte general en CSV (abre en Excel)")
    public ResponseEntity<byte[]> generalCsv(
            @RequestParam(required = false) Integer codOfic,
            @RequestParam(required = false) Integer codGrupo,
            @RequestParam(required = false) Integer codResp,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate corte) {

        List<ActivoRow> rows = service.general(new Filtro(codOfic, codGrupo, codResp, estado, desde, hasta, corte));

        StringBuilder sb = new StringBuilder("\uFEFF"); // BOM para que Excel respete las tildes
        sb.append("Cod. Activo,Descripcion,Grupo,Oficina,Responsable,Fecha incorporacion,Estado,")
          .append("Costo,Vida util (anios),Deprec. acumulada,Valor neto\n");
        for (ActivoRow r : rows) {
            sb.append(csv(r.codActivo())).append(',').append(csv(r.descripcion())).append(',')
              .append(csv(r.grupo())).append(',').append(csv(r.oficina())).append(',')
              .append(csv(r.responsable())).append(',').append(r.fechaIncorporacion()).append(',')
              .append(csv(r.estado())).append(',').append(r.costo()).append(',')
              .append(r.vidaUtil()).append(',').append(r.depreciacionAcumulada()).append(',')
              .append(r.valorNeto()).append('\n');
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"reporte_activos.csv\"")
                .contentType(new MediaType("text", "csv", StandardCharsets.UTF_8))
                .body(sb.toString().getBytes(StandardCharsets.UTF_8));
    }

    private static String csv(String v) {
        if (v == null) return "";
        if (v.contains(",") || v.contains("\"") || v.contains("\n")) {
            return "\"" + v.replace("\"", "\"\"") + "\"";
        }
        return v;
    }
}
