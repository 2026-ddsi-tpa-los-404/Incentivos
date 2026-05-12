package ar.edu.utn.dds.k3003.model;

import ar.edu.utn.dds.k3003.catedra.dtos.donaciones.DonacionDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.CategoriaDonadorEnum;
import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.TipoMisionEnum;
import ar.edu.utn.dds.k3003.catedra.fachadas.FachadaDonaciones;

import java.util.List;

public class DonacionesAscendentes extends Mision{
    public DonacionesAscendentes(String id, String nombre, String insigniaId, CategoriaDonadorEnum categoriaDonadorFin, CategoriaDonadorEnum categoriaDonadorInicio, TipoMisionEnum tipoDeMision) {
        super(id, nombre, insigniaId, categoriaDonadorFin, categoriaDonadorInicio, tipoDeMision);
    }

    @Override
    public boolean estaCompleta(List<DonacionDTO> donaciones, FachadaDonaciones fachadaDonaciones) {
        if (donaciones.size() < 5) return false;

        int cont = 1;
        while (cont < 5) {
            DonacionDTO actual = donaciones.get(donaciones.size() - cont);
            DonacionDTO anterior = donaciones.get(donaciones.size() - cont - 1);

            if (actual.cantidad() <= anterior.cantidad()) {
                return false;
            }
            cont++;
        }
        return true;
    }
}
