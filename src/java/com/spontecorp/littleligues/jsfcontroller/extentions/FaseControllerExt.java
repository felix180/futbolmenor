/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.littleligues.jsfcontroller.extentions;

import com.spontecorp.littleligues.jpacontroller.LigaJpaController;
import com.spontecorp.littleligues.jsfcontroller.torneo.FaseController;
import com.spontecorp.littleligues.model.liga.Liga;
import com.spontecorp.littleligues.model.torneo.Grupo;
import com.spontecorp.littleligues.model.torneo.Llave;
import com.spontecorp.littleligues.model.torneo.Temporada;
import com.spontecorp.littleligues.utils.LittleLiguesUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

/**
 *
 * @author jgcastillo
 */
@ManagedBean(name = "faseControllerExt")
@SessionScoped
public class FaseControllerExt extends FaseController {

    private LigaJpaController ligaJpaController = null;
    private Liga selectedLiga;
    private Temporada selectedTemporada;
    private boolean temporadaListDisable = true;
    //private String grupoLlave;
    //private Integer cantGruposLlaves;

    public FaseControllerExt() {
    }

    private LigaJpaController getLigaJpaController() {
        if (ligaJpaController == null) {
            ligaJpaController = new LigaJpaController(LittleLiguesUtils.getEmf());
        }
        return ligaJpaController;
    }

    public void temporadaChanged(ValueChangeEvent event){
        selectedTemporada = (Temporada)event.getNewValue();
    }
//    
//    public void grupoLlaveChanged(ValueChangeEvent event){
//        super.getSelected().setGrupoLlave(event.getNewValue().toString());
//    }
    
    public String agregarFase(){
//        List<Grupo> grupos = new ArrayList<>();
//        List<Llave> llaves = new ArrayList<>();
//        super.getSelected().setGrupoList(grupos);
//        super.getSelected().setLlaveList(llaves);
//        
//        if(super.getSelected().getGrupoLlave().equals("gr")){
//            for(int i = 0; i < super.getSelected().getCantGruposLlaves(); i++){
//                Grupo grp = new Grupo();
//                super.getSelected().getGrupoList().add(grp);
//            }
//        } else {
//            for(int i = 0; i < super.getSelected().getCantGruposLlaves(); i++){
//                Llave llave = new Llave();
//                super.getSelected().getLlaveList().add(llave);
//            }
//        }
        super.getSelected().setTemporadaId(selectedTemporada);
        return super.create();
    }
    
    public List<SelectItem> getLigas() {
        List<Liga> ligas = getLigaJpaController().findLigaEntities();
        List<SelectItem> selectLigas = new ArrayList<>();
        selectLigas.add(new SelectItem("---"));
        for (Liga liga : ligas) {
            selectLigas.add(new SelectItem(liga, liga.getNombre()));
        }
        return selectLigas;
    }

    public List<SelectItem> getTemporadas() {
        List<SelectItem> selectTemporadas = new ArrayList<>();
        selectTemporadas.add(new SelectItem("---"));
        if (!temporadaListDisable && (selectedLiga != null)) {
            List<Temporada> temporadas = selectedLiga.getTemporadaList();
            for(Temporada temporada : temporadas){
                selectTemporadas.add(new SelectItem(temporada, temporada.getNombre()));
            }
        }

        return selectTemporadas;
    }

    public Liga getSelectedLiga() {
        return selectedLiga;
    }

    public void setSelectedLiga(Liga selectedLiga) {
        this.selectedLiga = selectedLiga;
        temporadaListDisable = false;
    }

    public Temporada getSelectedTemporada() {
        return selectedTemporada;
    }

    public void setSelectedTemporada(Temporada selectedTemporada) {
        this.selectedTemporada = selectedTemporada;
    }

    public boolean isTemporadaListDisable() {
        return temporadaListDisable;
    }

    public void setTemporadaListDisable(boolean temporadaListDisable) {
        this.temporadaListDisable = temporadaListDisable;
    }

    
}
