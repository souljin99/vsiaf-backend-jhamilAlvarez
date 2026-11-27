package com.vsiaf.reportes.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "rep_activo")
public class Activo {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "cod_activo", nullable = false, unique = true)
    private String codActivo;
    @Column(nullable = false)
    private String descripcion;
    @Column(name = "fecha_incorporacion", nullable = false)
    private LocalDate fechaIncorporacion;
    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal costo;
    @Column(nullable = false)
    private String estado; // BUENO, REGULAR, MALO

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "grupo_id")
    private Grupo grupo;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "oficina_id")
    private Oficina oficina;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "responsable_id")
    private Responsable responsable;

    protected Activo() {}
    public Activo(String codActivo, String descripcion, LocalDate fechaIncorporacion, BigDecimal costo,
                  String estado, Grupo grupo, Oficina oficina, Responsable responsable) {
        this.codActivo = codActivo; this.descripcion = descripcion; this.fechaIncorporacion = fechaIncorporacion;
        this.costo = costo; this.estado = estado; this.grupo = grupo; this.oficina = oficina; this.responsable = responsable;
    }
    public void actualizar(String descripcion, LocalDate fechaIncorporacion, BigDecimal costo, String estado,
                           Grupo grupo, Oficina oficina, Responsable responsable) {
        this.descripcion = descripcion; this.fechaIncorporacion = fechaIncorporacion; this.costo = costo;
        this.estado = estado; this.grupo = grupo; this.oficina = oficina; this.responsable = responsable;
    }
    public void setEstado(String estado) { this.estado = estado; }
    public Long getId() { return id; }
    public String getCodActivo() { return codActivo; }
    public String getDescripcion() { return descripcion; }
    public LocalDate getFechaIncorporacion() { return fechaIncorporacion; }
    public BigDecimal getCosto() { return costo; }
    public String getEstado() { return estado; }
    public Grupo getGrupo() { return grupo; }
    public Oficina getOficina() { return oficina; }
    public Responsable getResponsable() { return responsable; }
}
