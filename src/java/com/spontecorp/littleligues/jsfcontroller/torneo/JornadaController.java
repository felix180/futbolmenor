package com.spontecorp.littleligues.jsfcontroller.torneo;

import com.spontecorp.littleligues.jpacontroller.LigaJpaController;
import com.spontecorp.littleligues.jpacontroller.extentions.FaseJpaControllerExt;
import com.spontecorp.littleligues.jpacontroller.extentions.GrupoJpaControllerExt;
import com.spontecorp.littleligues.jpacontroller.extentions.JornadaJpaControllerExt;
import com.spontecorp.littleligues.jpacontroller.extentions.TemporadaJpaControllerExt;
import com.spontecorp.littleligues.jsfcontroller.torneo.util.JsfUtil;
import com.spontecorp.littleligues.jsfcontroller.torneo.util.PaginationHelper;
import com.spontecorp.littleligues.model.liga.Liga;
import com.spontecorp.littleligues.model.torneo.Fase;
import com.spontecorp.littleligues.model.torneo.Grupo;
import com.spontecorp.littleligues.model.torneo.Jornada;
import com.spontecorp.littleligues.model.torneo.Temporada;
import com.spontecorp.littleligues.utils.LittleLiguesUtils;
import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

@ManagedBean(name = "jornadaController")
@SessionScoped
public class JornadaController implements Serializable {

    private Jornada current;
    private DataModel items = null;
    private JornadaJpaControllerExt jpaController = null;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    
    private Liga liga;
    private Temporada temporada;
    private Fase fase;
    private Grupo grupo;
    private boolean temporadaListDisabled = true;
    private boolean faseListDisabled = true;
    private boolean grupoListDisabled = true;
    private boolean tableDisabled = true;
    private List<Jornada> jornadasFiltered;

    public JornadaController() {
    }

    public Liga getLiga() {
        return liga;
    }

    public void setLiga(Liga liga) {
        temporadaListDisabled = false;
        this.liga = liga;
    }

    public Temporada getTemporada() {
        return temporada;
    }

    public void setTemporada(Temporada temporada) {
        faseListDisabled = false;
        this.temporada = temporada;
    }

    public Fase getFase() {
        return fase;
    }

    public void setFase(Fase fase) {
        grupoListDisabled = false;
        this.fase = fase;
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        tableDisabled = false;
        current.setGrupoId(grupo);
        this.grupo = grupo;

        recreateModel();
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

    public boolean isTableDisabled() {
        return tableDisabled;
    }

    public List<Jornada> getJornadasFiltered() {
        return jornadasFiltered;
    }

    public void setJornadasFiltered(List<Jornada> jornadasFiltered) {
        this.jornadasFiltered = jornadasFiltered;
    }

    public SelectItem[] getLigasAvalaibleSelectOne() {
        LigaJpaController ligaJpaController = new LigaJpaController(LittleLiguesUtils.getEmf());
        List<Liga> ligas = ligaJpaController.findLigaEntities();
        return com.spontecorp.littleligues.jsfcontroller.liga.util.JsfUtil.getSelectItems(ligas, true);
    }

    public SelectItem[] getTemporadasAvalaibleSelectOne() {
        TemporadaJpaControllerExt temporadaJpaController = new TemporadaJpaControllerExt(LittleLiguesUtils.getEmf());
        SelectItem[] arreglo = null;
        if (!temporadaListDisabled && (liga != null)) {
            List<Temporada> temporadas = temporadaJpaController.findTemporadasOnLiga(liga);
            arreglo = com.spontecorp.littleligues.jsfcontroller.liga.util.JsfUtil.getSelectItems(temporadas, true);
        }
        return arreglo;
    }

    public SelectItem[] getFasesAvalaibleSelectOne() {
        FaseJpaControllerExt faseJpaController = new FaseJpaControllerExt(LittleLiguesUtils.getEmf());
        SelectItem[] arreglo = null;
        if (!faseListDisabled && (temporada != null)) {
            List<Fase> fases = faseJpaController.findFasesOnTemporada(temporada);
            arreglo = com.spontecorp.littleligues.jsfcontroller.liga.util.JsfUtil.getSelectItems(fases, true);
        }
        return arreglo;
    }
    
    public SelectItem[] getFasesWithGrupoAvalaibleSelectOne() {
        FaseJpaControllerExt faseJpaController = new FaseJpaControllerExt(LittleLiguesUtils.getEmf());
        SelectItem[] arreglo = null;
        if (!faseListDisabled && (temporada != null)) {
            List<Fase> fases = faseJpaController.findFaseEntitiesWithGruposOnTemporada(temporada);
            arreglo = com.spontecorp.littleligues.jsfcontroller.liga.util.JsfUtil.getSelectItems(fases, true);
        }
        return arreglo;
    }

    public SelectItem[] getGruposAvalaibleSelectOne() {
        GrupoJpaControllerExt grupoJpaController = new GrupoJpaControllerExt(LittleLiguesUtils.getEmf());
        SelectItem[] arreglo = null;
        if (!grupoListDisabled && (fase != null)) {
            List<Grupo> grupos = grupoJpaController.findGruposOnFase(fase);
            arreglo = com.spontecorp.littleligues.jsfcontroller.liga.util.JsfUtil.getSelectItems(grupos, true);
        }
        return arreglo;
    }

    public Jornada getSelected() {
        if (current == null) {
            current = new Jornada();
            grupo = new Grupo();
            fase = new Fase();
            temporada = new Temporada();
            liga = new Liga();
            
            temporada.setLigaId(liga);
            fase.setTemporadaId(temporada);
            grupo.setFaseId(fase);
            current.setGrupoId(grupo);
            selectedItemIndex = -1;
        }
        return current;
    }

    public void setSelected(Jornada jornada) {
        current = jornada;
    }

    private JornadaJpaControllerExt getJpaController() {
        if (jpaController == null) {
            jpaController = new JornadaJpaControllerExt(LittleLiguesUtils.getEmf());
        }
        return jpaController;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {
                @Override
                public int getItemsCount() {
                    return getJpaController().getJornadaCount();
                }

                @Override
                public DataModel createPageDataModel() {
                    if(grupo == null){
                        return new ListDataModel(getJpaController().findJornadaEntities(getPageSize(), getPageFirstItem()));
                    } else {
                        return new ListDataModel(getJpaController().findJornadaEntitiesOnGrupo(current.getGrupoId(), getPageSize(), getPageFirstItem()));
                    }
                }
            };
        }
        return pagination;
    }
    
    public String prepareList() {
        recreateModel();
        return "List?faces-redirect=true";
    }

//    public String prepareView() {
//        current = (Jornada) getItems().getRowData();
//        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
//        return "View";
//    }
    
    public String prepareCreate() {
        current = new Jornada();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            current.setIsCurrent(0);
            getJpaController().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("JornadaCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        FacesContext context = FacesContext.getCurrentInstance();
        int jornadaId = context.getExternalContext().getRequestParameterMap().get("jornadaId") != null ? Integer.parseInt(context.getExternalContext().getRequestParameterMap().get("jornadaId")) : -1;
        
        //current = (Jornada) getItems().getRowData();
        current = getJpaController().findJornada(jornadaId);
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }
    
    public String prepareEditFromView() {
        return "Edit";
    }

    public String update() {
        try {
            //Listo todas los Jornadas del Grupo
            List<Jornada> allJornadaGroup = getJpaController().findJornadaEntitiesOnGrupo(current.getGrupoId());
            //Seteo las Jornadas como NO ACTUAL
            for(Jornada jornada : allJornadaGroup){
                jornada.setIsCurrent(0);
                getJpaController().edit(jornada);
            }
            //Actualizo la Jornada Actual
            getJpaController().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("JornadaUpdated"));
            return "List";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareDestroy() {
        current = (Jornada) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Delete";
    }
    
    public String prepareDestroyFromView() {
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("JornadaDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    private void updateCurrentItem() {
        int count = getJpaController().getJornadaCount();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getJpaController().findJornadaEntities(1, selectedItemIndex).get(0);
        }
    }

    public DataModel getItems() {
        recreateModel();
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
    
    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(getJpaController().findJornadaEntities(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(getJpaController().findJornadaEntities(), true);
    }

    @FacesConverter(forClass = Jornada.class)
    public static class JornadaControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            JornadaController controller = (JornadaController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "jornadaController");
            return controller.getJpaController().findJornada(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Jornada) {
                Jornada o = (Jornada) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Jornada.class.getName());
            }
        }
    }
}
