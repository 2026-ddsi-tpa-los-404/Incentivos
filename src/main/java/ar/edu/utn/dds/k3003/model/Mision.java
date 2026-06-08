package ar.edu.utn.dds.k3003.model;

import ar.edu.utn.dds.k3003.catedra.dtos.donaciones.DonacionDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.CategoriaDonadorEnum;
import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.TipoMisionEnum;
import ar.edu.utn.dds.k3003.catedra.fachadas.FachadaDonaciones;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "mision")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo", discriminatorType = DiscriminatorType.STRING)
public abstract class Mision {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String nombre;

    @ManyToOne
    @JoinColumn(name = "insignia_id")
    Insignia insignia;

    Boolean completada;
    @Enumerated(EnumType.STRING)
    CategoriaDonadorEnum categoriaDonadorInicio;
    @Enumerated(EnumType.STRING)
    CategoriaDonadorEnum categoriaDonadorFin;
    @Enumerated(EnumType.STRING)
    TipoMisionEnum tipoDeMision;

    public Mision() {
    }

    public Mision(String nombre, Insignia insignia, CategoriaDonadorEnum categoriaDonadorFin, CategoriaDonadorEnum categoriaDonadorInicio, TipoMisionEnum tipoDeMision) {
        this.nombre = nombre;
        this.insignia = insignia;
        this.categoriaDonadorFin = categoriaDonadorFin;
        this.categoriaDonadorInicio = categoriaDonadorInicio;
        this.completada = false;
        this.tipoDeMision = tipoDeMision;
    }

    public abstract boolean estaCompleta(List<DonacionDTO> donaciones, FachadaDonaciones fachadaDonaciones);

    public void setId(Long id) {
        this.id = id;
    }

    public void setCompletada(Boolean completada) {
        this.completada = completada;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public Insignia  getInsignia() {
        return insignia;
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

