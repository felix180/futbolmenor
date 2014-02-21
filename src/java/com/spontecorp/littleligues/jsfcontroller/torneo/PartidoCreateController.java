package com.spontecorp.littleligues.jsfcontroller.torneo;

import com.spontecorp.littleligues.jpacontroller.CategoriaJpaController;
import com.spontecorp.littleligues.jpacontroller.LigaJpaController;
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
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

@ManagedBean(name = "partidoCreateController")
@SessionScoped
public class PartidoCreateController implements Serializable {

    private Partido current;
    private DataModel items = null;
    private PartidoJpaControllerExt jpaController = null;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private Liga liga;
    private Temporada temporada;
    private boolean temporadaListDisabled = true;
    private Fase fase;
    private boolean faseListDisabled = true;
    private Grupo grupo;
    private boolean grupoListDisabled = true;
    private Jornada jornada;
    private boolean jornadaListDisabled = true;
    private Llave llave;
    private boolean llaveListDisabled = true;
    private Categoria categoria;
    private boolean equipoListDisable = true;
    private boolean categoriaListDisabled = true;
    private boolean partidoTableShow = false;
    
    public PartidoCreateController() {
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
        //recreateModel();
        return "List";
    }

    public String prepareView() {
        current = (Partido) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Partido();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            current.setStatus(0);
            getJpaController().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("PartidoCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Partido) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getJpaController().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("PartidoUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }
    
    public String prepareDestroy(){
        current = (Partido) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Delete";
    }

    public String destroy() {
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }

    public String destroyAndView() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (selectedItemIndex >= 0) {
            return "View";
        } else {
            // all items were removed - go back to list
            recreateModel();
            return "List";
        }
    }

    private void performDestroy() {
        try {
            getJpaController().destroy(current.getId());
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("PartidoDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    private void updateCurrentItem() {
        int count = getJpaController().getPartidoCount();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getJpaController().findPartidoEntities(1, selectedItemIndex).get(0);
        }
    }

    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
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
    
    public void showTable(ActionEvent evt){
        recreateModel();
        partidoTableShow = true;
    }
    
    public void updateEquipos(ActionEvent evt){
        getEquiposAvalaibleSelectOne();
        equipoListDisable = false;
    }
    
    public void equipo1ChangeListener(ValueChangeEvent evt){
            if(evt.getNewValue() instanceof Equipo){
                current.setFakeEq1(null);
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Debe seleccionar un equipo"));
            }
    }
    
    public void equipo2ChangeListener(ValueChangeEvent evt) {
        if (evt.getNewValue() instanceof Equipo) {
            current.setFakeEq2(null);
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Debe seleccionar un equipo"));
        }
    }
    
    // Aqui comienzan los getter y setter
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
        categoriaListDisabled = false;
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
        categoriaListDisabled = false;
    }

    public Llave getLlave() {
        return llave;
    }

    public void setLlave(Llave llave) {
        this.llave = llave;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
        current.setCategoriaId(categoria);
        equipoListDisable = false;
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

    public boolean isEquipoListDisable() {
        return equipoListDisable;
    }

    public boolean isCategoriaListDisabled() {
        return categoriaListDisabled;
    }

    public boolean isPartidoTableShow() {
        return partidoTableShow;
    }

    
    // Aqui estan la data para los comboxes
    
//    public void ligasChanged(ValueChangeEvent evt){
//        liga = (Liga)evt.getNewValue();
//    }
    
    public void categoriaChanged(ValueChangeEvent evt){
        categoria = (Categoria) evt.getNewValue();
        equipoListDisable = false;
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
            for(Llave llaveObj : llaves){
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
        if (!equipoListDisable && (categoria != null) && (liga != null)) {
            List<Equipo> equipos = equipoJpaController.findEquiposOnCategoria(categoria, liga);
            arreglo = JsfUtil.getSelectItems(equipos, true);
        }
        return arreglo;
    }
    
    public SelectItem[] getStatusAvalailableSelectOne(){
        StatusPartidoEnum[] statusEnum = StatusPartidoEnum.values();
        SelectItem[] arreglo = new SelectItem[statusEnum.length + 1];
        arreglo[0] = new SelectItem("", "---");
        for(int i = 0; i < statusEnum.length; i++){
            arreglo[i+1] = new SelectItem(statusEnum[i].valor(), statusEnum[i].etiqueta());
        }
        return arreglo;
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(getJpaController().findPartidoEntities(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(getJpaController().findPartidoEntities(), true);
    }

    @FacesConverter(forClass = Partido.class)
    public static class PartidoControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            PartidoCreateController controller = (PartidoCreateController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "partidoController");
            return controller.getJpaController().findPartido(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuffer sb = new StringBuffer();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Partido) {
                Partido o = (Partido) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Partido.class.getName());
            }
        }
    }
}
