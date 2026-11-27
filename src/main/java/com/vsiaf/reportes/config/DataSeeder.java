package com.vsiaf.reportes.config;

import com.vsiaf.reportes.model.*;
import com.vsiaf.reportes.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

/** Carga datos de ejemplo la primera vez (solo si las tablas estan vacias). */
@Component
public class DataSeeder implements CommandLineRunner {

    private final GrupoRepository grupos;
    private final OficinaRepository oficinas;
    private final ResponsableRepository responsables;
    private final ActivoRepository activos;

    public DataSeeder(GrupoRepository grupos, OficinaRepository oficinas,
                      ResponsableRepository responsables, ActivoRepository activos) {
        this.grupos = grupos;
        this.oficinas = oficinas;
        this.responsables = responsables;
        this.activos = activos;
    }

    @Override
    public void run(String... args) {
        if (activos.count() > 0 || grupos.count() > 0) return;

        Grupo edif = grupos.save(new Grupo(1, "EDIFICACIONES", 40));
        Grupo mue = grupos.save(new Grupo(2, "MUEBLES Y ENSERES DE OFICINA", 10));
        Grupo maq = grupos.save(new Grupo(3, "MAQUINARIA EN GENERAL", 8));
        Grupo com = grupos.save(new Grupo(5, "EQUIPO DE COMUNICACIONES", 8));
        Grupo cmp = grupos.save(new Grupo(15, "EQUIPOS DE COMPUTACION", 4));

        Oficina dir = oficinas.save(new Oficina(1, "DIRECCION DE CARRERA"));
        Oficina lab = oficinas.save(new Oficina(2, "LABORATORIO DE COMPUTACION"));
        Oficina sec = oficinas.save(new Oficina(3, "SECRETARIA"));

        Responsable r1 = responsables.save(new Responsable(1, "JUAN PEREZ", "Director de Carrera"));
        Responsable r2 = responsables.save(new Responsable(2, "MARIA ROJAS", "Secretaria"));
        Responsable r3 = responsables.save(new Responsable(3, "CARLOS MAMANI", "Encargado de Laboratorio"));

        activos.save(new Activo("SIS-0001", "ESCRITORIO DE MADERA 1.40 M", LocalDate.of(2018, 3, 12), bd("1850.00"), "BUENO", mue, dir, r1));
        activos.save(new Activo("SIS-0002", "SILLON EJECUTIVO GIRATORIO", LocalDate.of(2019, 5, 20), bd("920.00"), "BUENO", mue, dir, r1));
        activos.save(new Activo("SIS-0003", "ARMARIO METALICO 2 PUERTAS", LocalDate.of(2016, 8, 1), bd("1400.00"), "REGULAR", mue, sec, r2));
        activos.save(new Activo("SIS-0004", "COMPUTADORA DE ESCRITORIO CORE I5", LocalDate.of(2023, 2, 14), bd("6200.00"), "BUENO", cmp, lab, r3));
        activos.save(new Activo("SIS-0005", "COMPUTADORA DE ESCRITORIO CORE I5", LocalDate.of(2023, 2, 14), bd("6200.00"), "BUENO", cmp, lab, r3));
        activos.save(new Activo("SIS-0006", "COMPUTADORA PORTATIL", LocalDate.of(2021, 9, 30), bd("7800.00"), "REGULAR", cmp, dir, r1));
        activos.save(new Activo("SIS-0007", "IMPRESORA LASER", LocalDate.of(2020, 1, 15), bd("2500.00"), "MALO", cmp, sec, r2));
        activos.save(new Activo("SIS-0008", "PROYECTOR MULTIMEDIA", LocalDate.of(2022, 4, 10), bd("4300.00"), "BUENO", com, lab, r3));
        activos.save(new Activo("SIS-0009", "ROUTER INALAMBRICO", LocalDate.of(2024, 7, 5), bd("980.00"), "BUENO", com, lab, r3));
        activos.save(new Activo("SIS-0010", "TELEFONO IP", LocalDate.of(2017, 11, 22), bd("450.00"), "REGULAR", com, sec, r2));
        activos.save(new Activo("SIS-0011", "AIRE ACONDICIONADO 18000 BTU", LocalDate.of(2019, 12, 3), bd("5200.00"), "BUENO", maq, lab, r3));
        activos.save(new Activo("SIS-0012", "GENERADOR ELECTRICO 3 KVA", LocalDate.of(2015, 6, 18), bd("8900.00"), "REGULAR", maq, dir, r1));
        activos.save(new Activo("SIS-0013", "AMBIENTE DE LABORATORIO (EDIFICACION)", LocalDate.of(2010, 1, 10), bd("150000.00"), "BUENO", edif, lab, r3));
    }

    private static BigDecimal bd(String v) {
        return new BigDecimal(v);
    }
}
