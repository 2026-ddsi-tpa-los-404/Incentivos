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
@DiscriminatorValue("REVOLUCION_DONADORA")
public class RevolucionDonadora extends Mision {

    public RevolucionDonadora(String nombre, String insigniaId, CategoriaDonadorEnum categoriaDonadorFin, CategoriaDonadorEnum categoriaDonadorInicio, TipoMisionEnum tipoDeMision) {
        super(nombre, insigniaId, categoriaDonadorFin, categoriaDonadorInicio, tipoDeMision);
    }

    public RevolucionDonadora() {

    }

    @Override
    public boolean estaCompleta(List<DonacionDTO> donaciones, FachadaDonaciones fachadaDonaciones) {
        return donaciones.stream().filter(d->d.cantidad()>50).count() > 10;
    }
}
