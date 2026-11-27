package com.vsiaf.reportes.model;

import jakarta.persistence.*;

@Entity
@Table(name = "rep_oficina")
public class Oficina {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "cod_ofic", nullable = false, unique = true)
    private Integer codOfic;
    @Column(nullable = false)
    private String nombre;

    protected Oficina() {}
    public Oficina(Integer codOfic, String nombre) { this.codOfic = codOfic; this.nombre = nombre; }
    public Long getId() { return id; }
    public Integer getCodOfic() { return codOfic; }
    public String getNombre() { return nombre; }
}
