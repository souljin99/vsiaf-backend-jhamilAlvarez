package com.vsiaf.reportes.model;

import jakarta.persistence.*;

@Entity
@Table(name = "rep_grupo")
public class Grupo {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "cod_grupo", nullable = false, unique = true)
    private Integer codGrupo;
    @Column(nullable = false)
    private String nombre;
    @Column(name = "vida_util", nullable = false)
    private Integer vidaUtil;

    protected Grupo() {}
    public Grupo(Integer codGrupo, String nombre, Integer vidaUtil) {
        this.codGrupo = codGrupo; this.nombre = nombre; this.vidaUtil = vidaUtil;
    }
    public Long getId() { return id; }
    public Integer getCodGrupo() { return codGrupo; }
    public String getNombre() { return nombre; }
    public Integer getVidaUtil() { return vidaUtil; }
}
