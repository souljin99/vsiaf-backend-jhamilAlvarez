package com.vsiaf.reportes.service;

import com.vsiaf.reportes.dto.ActivoRequest;
import com.vsiaf.reportes.dto.ActivoRow;
import com.vsiaf.reportes.model.*;
import com.vsiaf.reportes.repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ActivoService {

    private final ActivoRepository activos;
    private final GrupoRepository grupos;
    private final OficinaRepository oficinas;
    private final ResponsableRepository responsables;
    private final ReporteService reportes;
    private final String prefijo;

    public ActivoService(ActivoRepository activos, GrupoRepository grupos, OficinaRepository oficinas,
                         ResponsableRepository responsables, ReporteService reportes,
                         @Value("${vsiaf.unidad}") String unidad) {
        this.activos = activos;
        this.grupos = grupos;
        this.oficinas = oficinas;
        this.responsables = responsables;
        this.reportes = reportes;
        this.prefijo = unidad + "-";
    }

    @Transactional(readOnly = true)
    public ActivoRow obtener(String codActivo) {
        return reportes.aFila(buscar(codActivo));
    }

    @Transactional
    public synchronized ActivoRow crear(ActivoRequest req) {
        Activo a = new Activo(siguienteCodigo(), req.descripcion().trim(), req.fechaIncorporacion(),
                req.costo(), req.estado().toUpperCase(),
                grupo(req.codGrupo()), oficina(req.codOfic()), responsable(req.codResp()));
        return reportes.aFila(activos.save(a));
    }

    @Transactional
    public ActivoRow modificar(String codActivo, ActivoRequest req) {
        Activo a = buscar(codActivo);
        a.actualizar(req.descripcion().trim(), req.fechaIncorporacion(), req.costo(), req.estado().toUpperCase(),
                grupo(req.codGrupo()), oficina(req.codOfic()), responsable(req.codResp()));
        return reportes.aFila(activos.save(a));
    }

    @Transactional
    public ActivoRow cambiarEstado(String codActivo, String estado) {
        Activo a = buscar(codActivo);
        a.setEstado(estado.toUpperCase());
        return reportes.aFila(activos.save(a));
    }

    @Transactional
    public void eliminar(String codActivo) {
        activos.delete(buscar(codActivo));
    }

    // ---------- utilidades ----------

    private Activo buscar(String codActivo) {
        return activos.findByCodActivoConRelaciones(codActivo).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Activo " + codActivo + " no encontrado"));
    }

    private Grupo grupo(Integer cod) {
        return grupos.findByCodGrupo(cod).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.BAD_REQUEST, "El grupo " + cod + " no existe"));
    }

    private Oficina oficina(Integer cod) {
        return oficinas.findByCodOfic(cod).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.BAD_REQUEST, "La oficina " + cod + " no existe"));
    }

    private Responsable responsable(Integer cod) {
        return responsables.findByCodResp(cod).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.BAD_REQUEST, "El responsable " + cod + " no existe"));
    }

    private String siguienteCodigo() {
        int max = activos.findAll().stream().map(Activo::getCodActivo).mapToInt(this::sufijo).max().orElse(0);
        return String.format("%s%04d", prefijo, max + 1);
    }

    private int sufijo(String codigo) {
        try {
            return Integer.parseInt(codigo.substring(codigo.lastIndexOf('-') + 1));
        } catch (Exception e) {
            return 0;
        }
    }
}
