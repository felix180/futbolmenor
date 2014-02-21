/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.littleligues.jsfcontroller.torneo;

import com.spontecorp.littleligues.jpacontroller.CategoriaJpaController;
import com.spontecorp.littleligues.jpacontroller.LigaJpaController;
import com.spontecorp.littleligues.jpacontroller.extentions.ClasificacionJpaControllerExt;
import com.spontecorp.littleligues.jpacontroller.extentions.EquipoJpaControllerExt;
import com.spontecorp.littleligues.jpacontroller.extentions.FaseJpaControllerExt;
import com.spontecorp.littleligues.jpacontroller.extentions.GrupoJpaControllerExt;
import com.spontecorp.littleligues.jpacontroller.extentions.JornadaJpaControllerExt;
import com.spontecorp.littleligues.jpacontroller.extentions.LlaveJpaControllerExt;
import com.spontecorp.littleligues.jpacontroller.extentions.PartidoJpaControllerExt;
import com.spontecorp.littleligues.jpacontroller.extentions.TemporadaJpaControllerExt;
import com.spontecorp.littleligues.jsfcontroller.liga.util.JsfUtil;
import com.spontecorp.littleligues.jsfcontroller.liga.util.PaginationHelper;
import com.spontecorp.littleligues.model.liga.Categoria;
import com.spontecorp.littleligues.model.liga.Equipo;
import com.spontecorp.littleligues.model.liga.Liga;
import com.spontecorp.littleligues.model.torneo.Clasificacion;
import com.spontecorp.littleligues.model.torneo.Fase;
import com.spontecorp.littleligues.model.torneo.Grupo;
import com.spontecorp.littleligues.model.torneo.Jornada;
import com.spontecorp.littleligues.model.torneo.Llave;
import com.spontecorp.littleligues.model.torneo.Partido;
import com.spontecorp.littleligues.model.torneo.Temporada;
import com.spontecorp.littleligues.utils.LittleLiguesUtils;
import com.spontecorp.littleligues.utils.StatusPartidoEnum;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

/**
 *
 * @author Andrés
 */
@ManagedBean(name = "resultadoController")
@SessionScoped
public class ResultadoController implements Serializable {

    private Partido current;
    private DataModel items = null;
    private PartidoJpaControllerExt jpaController = null;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private Liga liga;
    private Temporada temporada;
    private Fase fase;
    private Grupo grupo;
    private Jornada jornada;
    private Llave llave;
    private Categoria categoria;
    private boolean temporadaListDisabled = true;
    private boolean faseListDisabled = true;
    private boolean grupoListDisabled = true;
    private boolean jornadaListDisabled = true;
    private boolean llaveListDisabled = true;
    private boolean categoriaListDisable = true;
    private boolean equipoListDisable = true;
    private boolean partidoTableShow = false;
    private final int LOCAL = 1;
    private final int VISITANTE = 0;

    public ResultadoController() {
    }

    public Partido getSelected() {
        if (current == null) {
            current = new Partido();
            selectedItemIndex = -1;
        }
        return current;
    }

    private PartidoJpaControllerExt getJpaController() {
        if (jpaController == null) {
            jpaController = new PartidoJpaControllerExt(LittleLiguesUtils.getEmf());
        }
        return jpaController;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {
                @Override
                public int getItemsCount() {
                    return getJpaController().getPartidoCount();
                }

                @Override
                public DataModel createPageDataModel() {
                    ListDataModel<Partido> partidos = null;
                    if (llave != null && categoria != null) {
                        partidos = new ListDataModel(getJpaController().findPartidoEntitiesOnCategoriaWithinLlave(categoria, llave));
                    }
                    if (jornada != null && categoria != null) {
                        partidos = new ListDataModel(getJpaController().findPartidoEntitiesOnCategoriaWithinJornada(categoria, jornada));
                    }

                    return partidos;
                }
            };
        }
        return pagination;
    }

    public String prepareList() {
        recreateModel();
        //FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "List";
    }

    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
//        disableCombos();
        return items;
    }

    private void recreateModel() {
        items = null;
    }

    private void recreatePagination() {
        pagination = null;
    }

    public String next() {
        getPagination().nextPage();
        recreateModel();
        return "List";
    }

    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "List";
    }

    private void disableCombos() {
        temporadaListDisabled = true;
        faseListDisabled = true;
        grupoListDisabled = true;
        jornadaListDisabled = true;
        llaveListDisabled = true;
        categoriaListDisable = true;
    }

    public Partido getCurrent() {
        return current;
    }

    public void setCurrent(Partido current) {
        this.current = current;
    }

    public Liga getLiga() {
        return liga;
    }

    public void setLiga(Liga liga) {
        this.liga = liga;
        temporadaListDisabled = false;
    }

    public Temporada getTemporada() {
        return temporada;
    }

    public void setTemporada(Temporada temporada) {
        this.temporada = temporada;
        faseListDisabled = false;
    }

    public Fase getFase() {
        return fase;
    }

    public void setFase(Fase fase) {
        this.fase = fase;
        grupoListDisabled = false;
        jornadaListDisabled = true;
        llaveListDisabled = false;
        categoriaListDisable = false;
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
        jornadaListDisabled = false;
    }

    public Jornada getJornada() {
        return jornada;
    }

    public void setJornada(Jornada jornada) {
        this.jornada = jornada;
        categoriaListDisable = false;
    }

    public Llave getLlave() {
        return llave;
    }

    public void setLlave(Llave llave) {
        this.llave = llave;
        categoriaListDisable = false;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public boolean isTemporadaListDisabled() {
        return temporadaListDisabled;
    }

    public boolean isFaseListDisabled() {
        return faseListDisabled;
    }

    public boolean isGrupoListDisabled() {
        return grupoListDisabled;
    }

    public boolean isJornadaListDisabled() {
        return jornadaListDisabled;
    }

    public boolean isLlaveListDisabled() {
        return llaveListDisabled;
    }

    public boolean isCategoriaListDisable() {
        return categoriaListDisable;
    }

    public boolean isPartidoTableShow() {
        return partidoTableShow;
    }

    public boolean isEquipoListDisable() {
        return equipoListDisable;
    }

    public void showTable(ActionEvent evt) {
        recreateModel();
        partidoTableShow = true;
    }

    public String fakeNavigation() {
        return null;
    }

    public SelectItem[] getLigasAvalaibleSelectOne() {
        LigaJpaController ligaJpaController = new LigaJpaController(LittleLiguesUtils.getEmf());
        List<Liga> ligas = ligaJpaController.findLigaEntities();
        return JsfUtil.getSelectItems(ligas, true);
    }

    public SelectItem[] getTemporadasAvalaibleSelectOne() {
        TemporadaJpaControllerExt temporadaJpaController = new TemporadaJpaControllerExt(LittleLiguesUtils.getEmf());
        SelectItem[] arreglo = null;
        if (!temporadaListDisabled && (liga != null)) {
            List<Temporada> temporadas = temporadaJpaController.findTemporadasOnLiga(liga);
            arreglo = JsfUtil.getSelectItems(temporadas, true);
        }
        return arreglo;
    }

    public SelectItem[] getFasesAvalaibleSelectOne() {
        FaseJpaControllerExt faseJpaController = new FaseJpaControllerExt(LittleLiguesUtils.getEmf());
        SelectItem[] arreglo = null;
        if (!faseListDisabled && (temporada != null)) {
            List<Fase> fases = faseJpaController.findFasesOnTemporada(temporada);
            arreglo = JsfUtil.getSelectItems(fases, true);
        }
        return arreglo;
    }

    public SelectItem[] getGruposAvalaibleSelectOne() {
        GrupoJpaControllerExt grupoJpaController = new GrupoJpaControllerExt(LittleLiguesUtils.getEmf());
        SelectItem[] arreglo = null;
        if (!grupoListDisabled && (fase != null)) {
            List<Grupo> grupos = grupoJpaController.findGruposOnFase(fase);
            arreglo = JsfUtil.getSelectItems(grupos, true);
        }
        return arreglo;
    }

    public SelectItem[] getJornadasAvalaibleSelectOne() {
        JornadaJpaControllerExt jornadaJpaController = new JornadaJpaControllerExt(LittleLiguesUtils.getEmf());
        SelectItem[] arreglo = null;
        if (!jornadaListDisabled && (grupo != null)) {
            List<Jornada> jornadas = jornadaJpaController.findJornadasOnGrupo(grupo);
            arreglo = JsfUtil.getSelectItems(jornadas, true);
        }
        return arreglo;
    }

    public List<SelectItem> getLlavesAvalaibleSelectOne() {
        LlaveJpaControllerExt llaveJpaController = new LlaveJpaControllerExt(LittleLiguesUtils.getEmf());
        //SelectItem[] arreglo = null;
        List<SelectItem> arreglo = new ArrayList<>();
        if (!llaveListDisabled && (fase != null)) {
            List<Llave> llaves = llaveJpaController.findLlavesOnFase(fase);
            //arreglo = JsfUtil.getSelectItems(llaves, true);
            for (Llave llaveObj : llaves) {
                arreglo.add(new SelectItem(llaveObj, llaveObj.toString()));
            }
        }
        return arreglo;
    }

    public SelectItem[] getCategoriasAvalaibleSelectOne() {
        CategoriaJpaController categoriaJpaController = new CategoriaJpaController(LittleLiguesUtils.getEmf());
        List<Categoria> categorias = categoriaJpaController.findCategoriaEntities();
        SelectItem[] arreglo = JsfUtil.getSelectItems(categorias, true);
        return arreglo;
    }

    public SelectItem[] getEquiposAvalaibleSelectOne() {
        EquipoJpaControllerExt equipoJpaController = new EquipoJpaControllerExt(LittleLiguesUtils.getEmf());
        SelectItem[] arreglo = null;
        if (!equipoListDisable && (categoria != null)) {
            List<Equipo> equipos = equipoJpaController.findEquiposOnCategoria(categoria, liga);
            arreglo = JsfUtil.getSelectItems(equipos, true);
        }
        return arreglo;
    }

    public SelectItem[] getStatusAvalailableSelectOne() {
        StatusPartidoEnum[] statusEnum = StatusPartidoEnum.values();
        SelectItem[] arreglo = new SelectItem[statusEnum.length + 1];
        arreglo[0] = new SelectItem("", "---");
        for (int i = 0; i < statusEnum.length; i++) {
            arreglo[i + 1] = new SelectItem(statusEnum[i].valor(), statusEnum[i].etiqueta());
        }
        return arreglo;
    }

    public String prepareEdit() {
        current = (Partido) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            if (current.getStatus() == StatusPartidoEnum.SUSPENDIDO.valor()) {
                current.setGolEq1(null);
                current.setGolEq2(null);
            }
            getJpaController().edit(current);
            if (jornada != null && (current.getStatus() != StatusPartidoEnum.SUSPENDIDO.valor())) {
                doClasificacion();
            }
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("PartidoUpdated"));
            return "List";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    // Lógica para guardar clasificación
    private void doClasificacion() {
        ClasificacionJpaControllerExt clasificaJpaController = new ClasificacionJpaControllerExt(LittleLiguesUtils.getEmf());
        try {
            int goleq1 = current.getGolEq1();
            int goleq2 = current.getGolEq2();

            if (clasificaJpaController.findClasificacionEntityByEquipoAndJornada(current.getEquipo1Id(), jornada) != null) {
                // si ya existe se edita
                Clasificacion clasifica1 = clasificaJpaController.findClasificacionEntityByEquipoAndJornada(current.getEquipo1Id(), jornada);

                clasifica1.setGolesFavor(goleq1);
                clasifica1.setGolesContra(goleq2);
                clasifica1.setDiferencia(goleq1 - goleq2);

                if (goleq1 == goleq2) {
                    clasifica1.setEmpatados(1);
                    clasifica1.setPuntos(1);
                    clasifica1.setGanados(0);
                    clasifica1.setPerdidos(0);
                } else if (goleq1 > goleq2) {
                    clasifica1.setGanados(1);
                    clasifica1.setPuntos(3);
                    clasifica1.setPerdidos(0);
                    clasifica1.setEmpatados(0);
                } else {
                    clasifica1.setGanados(0);
                    clasifica1.setPuntos(0);
                    clasifica1.setPerdidos(1);
                    clasifica1.setEmpatados(0);
                }
                clasificaJpaController.edit(clasifica1);

            } else { // de lo contrario se crea
                Clasificacion clasifica1 = new Clasificacion();

                clasifica1.setEquipoId(current.getEquipo1Id());
                clasifica1.setJornadaId(jornada);
                clasifica1.setJugados(1);
                clasifica1.setEqlocal(LOCAL);
                clasifica1.setGolesFavor(goleq1);
                clasifica1.setGolesContra(goleq2);
                clasifica1.setDiferencia(goleq1 - goleq2);

                if (goleq1 == goleq2) {
                    clasifica1.setEmpatados(1);
                    clasifica1.setPuntos(1);
                    clasifica1.setGanados(0);
                    clasifica1.setPerdidos(0);
                } else if (goleq1 > goleq2) {
                    clasifica1.setGanados(1);
                    clasifica1.setPuntos(3);
                    clasifica1.setPerdidos(0);
                    clasifica1.setEmpatados(0);
                } else {
                    clasifica1.setGanados(0);
                    clasifica1.setPuntos(0);
                    clasifica1.setPerdidos(1);
                    clasifica1.setEmpatados(0);
                }

                clasificaJpaController.create(clasifica1);
            }

            // Se repite para el equipo2
            if (clasificaJpaController.findClasificacionEntityByEquipoAndJornada(current.getEquipo2Id(), jornada) != null) {
                // si ya existe se edita
                Clasificacion clasifica2 = clasificaJpaController.findClasificacionEntityByEquipoAndJornada(current.getEquipo2Id(), jornada);

                clasifica2.setGolesFavor(goleq2);
                clasifica2.setGolesContra(goleq1);
                clasifica2.setDiferencia(goleq2 - goleq1);

                if (goleq1 == goleq2) {
                    clasifica2.setEmpatados(1);
                    clasifica2.setPuntos(1);
                    clasifica2.setGanados(0);
                    clasifica2.setPerdidos(0);
                } else if (goleq1 > goleq2) {
                    clasifica2.setPerdidos(1);
                    clasifica2.setPuntos(0);
                    clasifica2.setGanados(0);
                    clasifica2.setEmpatados(0);
                } else {
                    clasifica2.setPerdidos(0);
                    clasifica2.setPuntos(3);
                    clasifica2.setGanados(1);
                    clasifica2.setEmpatados(0);
                }
                clasificaJpaController.edit(clasifica2);
            } else { // de lo contrario se crea
                Clasificacion clasifica2 = new Clasificacion();

                clasifica2.setEquipoId(current.getEquipo2Id());
                clasifica2.setJornadaId(jornada);
                clasifica2.setJugados(1);
                clasifica2.setEqlocal(VISITANTE);
                clasifica2.setGolesFavor(goleq2);
                clasifica2.setGolesContra(goleq1);
                clasifica2.setDiferencia(goleq2 - goleq1);

                if (goleq1 == goleq2) {
                    clasifica2.setEmpatados(1);
                    clasifica2.setPuntos(1);
                    clasifica2.setGanados(0);
                    clasifica2.setPerdidos(0);
                } else if (goleq1 > goleq2) {
                    clasifica2.setPerdidos(1);
                    clasifica2.setPuntos(0);
                    clasifica2.setGanados(0);
                    clasifica2.setEmpatados(0);
                } else {
                    clasifica2.setPerdidos(0);
                    clasifica2.setPuntos(3);
                    clasifica2.setGanados(1);
                    clasifica2.setEmpatados(0);
                }

                clasificaJpaController.create(clasifica2);
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error de BD", "Detalle: " + e.getMessage()));
        }

    }

    public List<Object[]> getClasificationByCategory() {
        ClasificacionJpaControllerExt clasificaJpaController = new ClasificacionJpaControllerExt(LittleLiguesUtils.getEmf());
        List<Object[]> lista = clasificaJpaController.findClasificacionByCategoria(categoria, jornada);

        return lista;
    }
}
