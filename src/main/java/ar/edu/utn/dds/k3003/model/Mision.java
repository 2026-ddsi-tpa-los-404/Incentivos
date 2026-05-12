package ar.edu.utn.dds.k3003.model;

import ar.edu.utn.dds.k3003.catedra.dtos.donaciones.DonacionDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.CategoriaDonadorEnum;
import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.TipoMisionEnum;
import ar.edu.utn.dds.k3003.catedra.fachadas.FachadaDonaciones;

import java.util.List;

public abstract class Mision {
    String id;
    String nombre;
    String insigniaId;
    Boolean completada;
    CategoriaDonadorEnum categoriaDonadorInicio;
    CategoriaDonadorEnum categoriaDonadorFin;
    TipoMisionEnum tipoDeMision;

    public Mision(String id, String nombre, String insigniaId, CategoriaDonadorEnum categoriaDonadorFin, CategoriaDonadorEnum categoriaDonadorInicio,TipoMisionEnum tipoDeMision) {
        this.id = id;
        this.nombre = nombre;
        this.insigniaId = insigniaId;
        this.categoriaDonadorFin = categoriaDonadorFin;
        this.categoriaDonadorInicio = categoriaDonadorInicio;
        this.completada = false;
        this.tipoDeMision = tipoDeMision;
    }

    public abstract boolean estaCompleta(List<DonacionDTO> donaciones, FachadaDonaciones fachadaDonaciones);

    public void setId(String id) {
        this.id = id;
    }

    public void setCompletada(Boolean completada) {
        this.completada = completada;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getInsigniaId() {
        return insigniaId;
    }

    public Boolean getCompletada() {
        return completada;
    }

    public CategoriaDonadorEnum getCategoriaDonadorInicio() {
        return categoriaDonadorInicio;
    }

    public CategoriaDonadorEnum getCategoriaDonadorFin() {
        return categoriaDonadorFin;
    }

    public TipoMisionEnum getTipoDeMision() {
        return tipoDeMision;
    }
}

