package ar.edu.utn.dds.k3003.model;

import ar.edu.utn.dds.k3003.catedra.dtos.donaciones.DonacionDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.CategoriaDonadorEnum;
import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.TipoMisionEnum;
import ar.edu.utn.dds.k3003.catedra.fachadas.FachadaDonaciones;

import java.util.List;

public class Completitud extends Mision{


    public Completitud(String id, String nombre, String insigniaId, CategoriaDonadorEnum categoriaDonadorFin, CategoriaDonadorEnum categoriaDonadorInicio, TipoMisionEnum tipoDeMision) {
        super(id, nombre, insigniaId, categoriaDonadorFin, categoriaDonadorInicio,tipoDeMision);
    }

    @Override
    public boolean estaCompleta(List<DonacionDTO> donaciones, FachadaDonaciones fachadaDonaciones) {
        return donaciones.stream()
                .map(d -> fachadaDonaciones.buscarProductoPorID(d.productoID()).categoriaID())
                .distinct()
                .count() >= 3;
    }

}
