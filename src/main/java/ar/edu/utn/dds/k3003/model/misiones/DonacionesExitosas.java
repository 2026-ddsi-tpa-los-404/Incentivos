package ar.edu.utn.dds.k3003.model.misiones;

import ar.edu.utn.dds.k3003.catedra.dtos.donaciones.DonacionDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.donaciones.EstadoDonacionEnum;
import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.CategoriaDonadorEnum;
import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.TipoMisionEnum;
import ar.edu.utn.dds.k3003.catedra.fachadas.FachadaDonaciones;
import ar.edu.utn.dds.k3003.model.Insignia;
import ar.edu.utn.dds.k3003.model.Mision;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.util.List;

@Entity
@DiscriminatorValue("DONACIONES_EXITOSAS")
public class DonacionesExitosas extends Mision {

    public DonacionesExitosas(String nombre, Insignia insignia, CategoriaDonadorEnum categoriaDonadorFin, CategoriaDonadorEnum categoriaDonadorInicio, TipoMisionEnum tipoDeMision) {
        super(nombre, insignia, categoriaDonadorFin, categoriaDonadorInicio,tipoDeMision);
    }

    public DonacionesExitosas() {

    }

    @Override
    public boolean estaCompleta(List<DonacionDTO> donaciones, FachadaDonaciones fachadaDonaciones) {
        return donaciones.stream().filter(d->d.estado()== EstadoDonacionEnum.ACEPTADA).count() >= 20;
    }
}
