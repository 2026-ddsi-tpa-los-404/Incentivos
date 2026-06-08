package ar.edu.utn.dds.k3003.model.misiones;

import ar.edu.utn.dds.k3003.catedra.dtos.donaciones.DonacionDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.CategoriaDonadorEnum;
import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.TipoMisionEnum;
import ar.edu.utn.dds.k3003.catedra.fachadas.FachadaDonaciones;
import ar.edu.utn.dds.k3003.model.Mision;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.util.List;

@Entity
@DiscriminatorValue("COMPLETITUD")
public class Completitud extends Mision {
    public Completitud(String nombre, String insigniaId, CategoriaDonadorEnum categoriaDonadorFin, CategoriaDonadorEnum categoriaDonadorInicio, TipoMisionEnum tipoDeMision) {
        super(nombre, insigniaId, categoriaDonadorFin, categoriaDonadorInicio,tipoDeMision);
    }

    public Completitud() {
    }

    @Override
    public boolean estaCompleta(List<DonacionDTO> donaciones, FachadaDonaciones fachadaDonaciones) {
        return donaciones.stream()
                .map(d -> fachadaDonaciones.buscarProductoPorID(d.productoID()).categoriaID())
                .distinct()
                .count() >= 3;
    }

}
