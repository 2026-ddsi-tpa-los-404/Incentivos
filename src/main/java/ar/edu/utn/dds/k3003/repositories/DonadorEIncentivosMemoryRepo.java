 /*package ar.edu.utn.dds.k3003.repositories;

import ar.edu.utn.dds.k3003.model.Insignia;
import ar.edu.utn.dds.k3003.model.Mision;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DonadorEIncentivosMemoryRepo implements DonadorIncentivosRepo{


    private Map<String,List<Insignia>> insignasDeCadaDonador;


    private Map<String,Mision> misionesDeCadaDonador ;

    public DonadorEIncentivosMemoryRepo() {
        this.insignasDeCadaDonador = new HashMap<>();
        this.misionesDeCadaDonador = new HashMap<>();
    }

    @Override
    public void agregarInsignia(String donadorId, Insignia insignia) {

        if (!insignasDeCadaDonador.containsKey(donadorId)) {
            insignasDeCadaDonador.put(donadorId, new ArrayList<>());
        }


        insignasDeCadaDonador.get(donadorId).add(insignia);
    }

    @Override
    public List<Insignia> insigniasDelDonador(String donadorId) {

        return insignasDeCadaDonador.getOrDefault(donadorId, new ArrayList<>());


    }


    @Override
    public void agregarMision(String donadorId, Mision  mision) {
        misionesDeCadaDonador.put(donadorId,mision);
    }


    @Override
    public Mision misionDelDonador(String donadorId) {
        return misionesDeCadaDonador.getOrDefault(donadorId,null);
    }
}
*/
