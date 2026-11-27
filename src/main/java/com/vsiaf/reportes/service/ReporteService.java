package com.vsiaf.reportes.service;

import com.vsiaf.reportes.dto.*;
import com.vsiaf.reportes.model.Activo;
import com.vsiaf.reportes.repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ReporteService {

    private final ActivoRepository activos;
    private final GrupoRepository grupos;
    private final OficinaRepository oficinas;
    private final ResponsableRepository responsables;
    private final String entidad;
    private final String unidad;

    public ReporteService(ActivoRepository activos, GrupoRepository grupos, OficinaRepository oficinas,
                          ResponsableRepository responsables,
                          @Value("${vsiaf.entidad}") String entidadCod,
                          @Value("${vsiaf.entidad-nombre}") String entidadNombre,
                          @Value("${vsiaf.unidad}") String unidadCod,
                          @Value("${vsiaf.unidad-nombre}") String unidadNombre) {
        this.activos = activos;
        this.grupos = grupos;
        this.oficinas = oficinas;
        this.responsables = responsables;
        this.entidad = entidadCod + " - " + entidadNombre;
        this.unidad = unidadCod + " - " + unidadNombre;
    }

    /** Listas para llenar los combos de filtro del frontend. */
    public FiltrosDisponibles filtros() {
        return new FiltrosDisponibles(
                grupos.findAllByOrderByCodGrupoAsc().stream().map(g -> new Opcion(g.getCodGrupo(), g.getNombre())).toList(),
                oficinas.findAllByOrderByCodOficAsc().stream().map(o -> new Opcion(o.getCodOfic(), o.getNombre())).toList(),
                responsables.findAllByOrderByCodRespAsc().stream().map(r -> new Opcion(r.getCodResp(), r.getNombre())).toList(),
                List.of("BUENO", "REGULAR", "MALO"));
    }

    /** Reporte general: un renglon por activo, con depreciacion y valor neto. */
    public List<ActivoRow> general(Filtro f) {
        LocalDate corte = corte(f);
        validar(f);
        return activos.findAllConRelaciones().stream()
                .filter(a -> f.codOfic() == null || a.getOficina().getCodOfic().equals(f.codOfic()))
                .filter(a -> f.codGrupo() == null || a.getGrupo().getCodGrupo().equals(f.codGrupo()))
                .filter(a -> f.codResp() == null || a.getResponsable().getCodResp().equals(f.codResp()))
                .filter(a -> f.estado() == null || f.estado().isBlank() || a.getEstado().equalsIgnoreCase(f.estado()))
                .filter(a -> f.desde() == null || !a.getFechaIncorporacion().isBefore(f.desde()))
                .filter(a -> f.hasta() == null || !a.getFechaIncorporacion().isAfter(f.hasta()))
                .filter(a -> !a.getFechaIncorporacion().isAfter(corte))
                .sorted(Comparator.comparing(Activo::getCodActivo))
                .map(a -> fila(a, corte))
                .toList();
    }

    public List<ResumenRow> porGrupo(Filtro f) {
        return agrupar(general(f), ActivoRow::codGrupo, ActivoRow::grupo);
    }

    public List<ResumenRow> porOficina(Filtro f) {
        return agrupar(general(f), ActivoRow::codOfic, ActivoRow::oficina);
    }

    public List<ResumenRow> porResponsable(Filtro f) {
        return agrupar(general(f), ActivoRow::codResp, ActivoRow::responsable);
    }

    public ResumenGeneral resumen(Filtro f) {
        List<ActivoRow> rows = general(f);
        return new ResumenGeneral(entidad, unidad, corte(f), rows.size(),
                suma(rows, ActivoRow::costo), suma(rows, ActivoRow::depreciacionAcumulada), suma(rows, ActivoRow::valorNeto));
    }

    /** Convierte un activo en renglon de reporte (a la fecha de hoy). */
    public ActivoRow aFila(Activo a) {
        return fila(a, LocalDate.now());
    }

    // ---------- utilidades ----------

    private LocalDate corte(Filtro f) {
        return f.corte() != null ? f.corte() : LocalDate.now();
    }

    private void validar(Filtro f) {
        if (f.desde() != null && f.hasta() != null && f.desde().isAfter(f.hasta())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "'desde' no puede ser posterior a 'hasta'");
        }
    }

    private ActivoRow fila(Activo a, LocalDate corte) {
        BigDecimal dep = depreciacion(a, corte);
        BigDecimal neto = a.getCosto().subtract(dep).setScale(2, RoundingMode.HALF_UP);
        return new ActivoRow(a.getCodActivo(), a.getDescripcion(),
                a.getGrupo().getCodGrupo(), a.getGrupo().getNombre(),
                a.getOficina().getCodOfic(), a.getOficina().getNombre(),
                a.getResponsable().getCodResp(), a.getResponsable().getNombre(),
                a.getFechaIncorporacion(), a.getEstado(),
                a.getCosto().setScale(2, RoundingMode.HALF_UP), a.getGrupo().getVidaUtil(), dep, neto);
    }

    /** Depreciacion lineal: costo * (tiempo transcurrido / vida util), sin pasar del costo. */
    private BigDecimal depreciacion(Activo a, LocalDate corte) {
        long dias = ChronoUnit.DAYS.between(a.getFechaIncorporacion(), corte);
        if (dias <= 0) return BigDecimal.ZERO.setScale(2);
        BigDecimal vidaDias = BigDecimal.valueOf(a.getGrupo().getVidaUtil()).multiply(BigDecimal.valueOf(365.25));
        BigDecimal ratio = BigDecimal.valueOf(dias).divide(vidaDias, 10, RoundingMode.HALF_UP);
        if (ratio.compareTo(BigDecimal.ONE) > 0) ratio = BigDecimal.ONE;
        return a.getCosto().multiply(ratio).setScale(2, RoundingMode.HALF_UP);
    }

    private List<ResumenRow> agrupar(List<ActivoRow> rows, Function<ActivoRow, Integer> cod,
                                     Function<ActivoRow, String> nombre) {
        Map<Integer, List<ActivoRow>> m = rows.stream()
                .collect(Collectors.groupingBy(cod, TreeMap::new, Collectors.toList()));
        return m.entrySet().stream().map(e -> {
            List<ActivoRow> l = e.getValue();
            return new ResumenRow(e.getKey(), nombre.apply(l.get(0)), l.size(),
                    suma(l, ActivoRow::costo), suma(l, ActivoRow::depreciacionAcumulada), suma(l, ActivoRow::valorNeto));
        }).toList();
    }

    private BigDecimal suma(List<ActivoRow> l, Function<ActivoRow, BigDecimal> f) {
        return l.stream().map(f).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP);
    }
}
