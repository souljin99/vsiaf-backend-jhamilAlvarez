package com.vsiaf.reportes.model;

import jakarta.persistence.*;

@Entity
@Table(name = "rep_responsable")
public class Responsable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "cod_resp", nullable = false, unique = true)
    private Integer codResp;
    @Column(nullable = false)
    private String nombre;
    private String cargo;

    protected Responsable() {}
    public Responsable(Integer codResp, String nombre, String cargo) {
        this.codResp = codResp; this.nombre = nombre; this.cargo = cargo;
    }
    public Long getId() { return id; }
    public Integer getCodResp() { return codResp; }
    public String getNombre() { return nombre; }
    public String getCargo() { return cargo; }
}
