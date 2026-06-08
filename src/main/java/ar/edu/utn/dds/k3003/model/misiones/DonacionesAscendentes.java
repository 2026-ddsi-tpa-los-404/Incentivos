package ar.edu.utn.dds.k3003.model.misiones;

import ar.edu.utn.dds.k3003.catedra.dtos.donaciones.DonacionDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.CategoriaDonadorEnum;
import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.TipoMisionEnum;
import ar.edu.utn.dds.k3003.catedra.fachadas.FachadaDonaciones;
import ar.edu.utn.dds.k3003.model.Insignia;
import ar.edu.utn.dds.k3003.model.Mision;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.util.List;

@Entity
@DiscriminatorValue("DONACIONES_ASCENDENTES")
public class DonacionesAscendentes extends Mision {

    public DonacionesAscendentes(String nombre, Insignia insignia, CategoriaDonadorEnum categoriaDonadorFin, CategoriaDonadorEnum categoriaDonadorInicio, TipoMisionEnum tipoDeMision) {
        super(nombre, insignia, categoriaDonadorFin, categoriaDonadorInicio, tipoDeMision);
    }
    public DonacionesAscendentes() {

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
