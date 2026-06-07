package ar.edu.utn.dds.k3003.model;

import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.MisionDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@Entity
@Table(name = "donador_incentivos")
public class DonadorIncentivos {

    @Id
    @GeneratedValue()
    private Long id;
    private String donadorID;

    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(
            name = "donador_insignias",
            joinColumns = @JoinColumn(name = "donador_incentivos_id"),
            inverseJoinColumns = @JoinColumn(name = "insignia_id")
    )
    private List<Insignia> insigniasDonador = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "mision_id")
    private Mision misionActual;

    public DonadorIncentivos(){}

    public DonadorIncentivos(String donadorId){
        this.donadorID = donadorId;
    }

}
