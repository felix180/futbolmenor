/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.littleligues.jsfcontroller;

import com.spontecorp.littleligues.jpacontroller.extentions.*;
import com.spontecorp.littleligues.model.liga.Categoria;
import com.spontecorp.littleligues.model.liga.Liga;
import com.spontecorp.littleligues.model.torneo.Grupo;
import com.spontecorp.littleligues.model.torneo.Jornada;
import com.spontecorp.littleligues.model.torneo.Partido;
import com.spontecorp.littleligues.utils.LittleLiguesUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Zuleidyb
 */
@ManagedBean(name="jornadaCalendarBean")
@ViewScoped

public class JornadaCalendarBean implements Serializable{

    private int idLiga;
    private int idCategoria;
    private int idGrupo;
    private int idJornada;
    List<Partido> partidoList = new ArrayList<>();
    private int totalPartidoList;
    
    public JornadaCalendarBean() {
        
        FacesContext context = FacesContext.getCurrentInstance();
        idLiga = context.getExternalContext().getRequestParameterMap().get("idLiga") != null ? Integer.parseInt(context.getExternalContext().getRequestParameterMap().get("idLiga")) : -1;
        idCategoria = context.getExternalContext().getRequestParameterMap().get("idCategoria") != null ? Integer.parseInt(context.getExternalContext().getRequestParameterMap().get("idCategoria")) : -1;
        idGrupo = context.getExternalContext().getRequestParameterMap().get("idGrupo") != null ? Integer.parseInt(context.getExternalContext().getRequestParameterMap().get("idGrupo")) : -1;
        idJornada = context.getExternalContext().getRequestParameterMap().get("idJornada") != null ? Integer.parseInt(context.getExternalContext().getRequestParameterMap().get("idJornada")) : -1;
        
        LigaJpaControllerExt ligaJpaController = new LigaJpaControllerExt(LittleLiguesUtils.getEmf());
        CategoriaJpaControllerExt categoriaJpaController = new CategoriaJpaControllerExt(LittleLiguesUtils.getEmf());
        GrupoJpaControllerExt grupoJpaController = new GrupoJpaControllerExt(LittleLiguesUtils.getEmf());
        JornadaJpaControllerExt jornadaJpaController = new JornadaJpaControllerExt(LittleLiguesUtils.getEmf());
        
        Liga liga = ligaJpaController.findLiga(idLiga);
        Categoria categoria = categoriaJpaController.findCategoria(idCategoria);
        Grupo grupo = grupoJpaController.findGrupo(idGrupo);
        Jornada jornada = jornadaJpaController.findJornada(idJornada);
        
        partidoList = getListaPartidos(categoria, jornada, idGrupo);
        totalPartidoList = partidoList.size();
   
    }
    
    /**
     * Metodo para obtener la Lista de Partidos de cada Categoria por cada Jornada
     * @param categoria
     * @param jornada
     * @param totalGrupos
     * @return 
     */
    public final List<Partido> getListaPartidos(Categoria categoria, Jornada jornada, int totalGrupos) {
        PartidoJpaControllerExt partidoJpaController = new PartidoJpaControllerExt(LittleLiguesUtils.getEmf());
        List<Partido> partidos = null;
        if (categoria != null) {
            if (totalGrupos > 0) {
                partidos = partidoJpaController.findPartidoEntitiesOnCategoriaWithinJornada(categoria, jornada);
            } //else if (totalLlaves > 0) {
              //  listaPartidos = partidoJpaController.findPartidoEntitiesOnCategoriaWithinLlave(categoria, llave);
              //}
        }
        return partidos;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public int getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(int idGrupo) {
        this.idGrupo = idGrupo;
    }

    public int getIdJornada() {
        return idJornada;
    }

    public void setIdJornada(int idJornada) {
        this.idJornada = idJornada;
    }

    public int getIdLiga() {
        return idLiga;
    }

    public void setIdLiga(int idLiga) {
        this.idLiga = idLiga;
    }

    public List<Partido> getPartidoList() {
        return partidoList;
    }

    public void setPartidoList(List<Partido> partidoList) {
        this.partidoList = partidoList;
    }

    public int getTotalPartidoList() {
        return totalPartidoList;
    }

    public void setTotalPartidoList(int totalPartidoList) {
        this.totalPartidoList = totalPartidoList;
    }
   
    
}
