package com.spontecorp.littleligues.jsfcontroller.torneo;


import com.spontecorp.littleligues.jpacontroller.LigaJpaController;
import com.spontecorp.littleligues.jpacontroller.extentions.FaseJpaControllerExt;
import com.spontecorp.littleligues.jpacontroller.extentions.GrupoJpaControllerExt;
import com.spontecorp.littleligues.jpacontroller.extentions.TemporadaJpaControllerExt;
import com.spontecorp.littleligues.jsfcontroller.liga.util.JsfUtil;
import com.spontecorp.littleligues.jsfcontroller.liga.util.PaginationHelper;
import com.spontecorp.littleligues.model.liga.Liga;
import com.spontecorp.littleligues.model.torneo.Fase;
import com.spontecorp.littleligues.model.torneo.Grupo;
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

@ManagedBean(name = "grupoController")
@SessionScoped
public class GrupoController implements Serializable {

    private Grupo current;
    private DataModel items = null;
    private GrupoJpaControllerExt jpaController = null;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    
    private Liga liga;
    private Temporada temporada;
    private Fase fase;
    private boolean temporadaListDisabled = true;
    private boolean faseListDisabled = true;
    private boolean tableDisabled = true;
    private List<Grupo> gruposFiltered;

    public GrupoController() {
    }

    public Grupo getSelected() {
        if (current == null) {
            current = new Grupo();
            fase = new Fase();
            temporada = new Temporada();
            liga = new Liga();

            temporada.setLigaId(liga);
            fase.setTemporadaId(temporada);
            current.setFaseId(fase);
            
            selectedItemIndex = -1;
        }
        return current;
    }

    private GrupoJpaControllerExt getJpaController() {
        if (jpaController == null) {
            jpaController = new GrupoJpaControllerExt(LittleLiguesUtils.getEmf());
        }
        return jpaController;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {
                @Override
                public int getItemsCount() {
                    return getJpaController().getGrupoCount();
                }

                @Override
                public DataModel createPageDataModel() {
                    if(fase == null){
                        return new ListDataModel(getJpaController().findGrupoEntities(getPageSize(), getPageFirstItem()));
                    } else {
                        return new ListDataModel(getJpaController().findGrupoEntitiesOnFase(fase,getPageSize(), getPageFirstItem()));
                    }
                }
            };
        }
        return pagination;
    }

    public String prepareList() {
        recreateModel();
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "List?faces-redirect=true";
    }

    public String prepareView() {
        current = (Grupo) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Grupo();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            getJpaController().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("GrupoCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Grupo) getItems().getRowData();
        
        liga = current.getFaseId().getTemporadaId().getLigaId();
        temporadaListDisabled = false;
        temporada = current.getFaseId().getTemporadaId();
        faseListDisabled = false;
        
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }
    
    public String prepareEditFromView() {
        return "Edit";
    }

    public String update() {
        try {
            getJpaController().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("GrupoUpdated"));
            return prepareList();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareDestroy() {
        current = (Grupo) getItems().getRowData();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("GrupoDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    private void updateCurrentItem() {
        int count = getJpaController().getGrupoCount();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getJpaController().findGrupoEntities(1, selectedItemIndex).get(0);
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
        tableDisabled = false;
        this.fase = fase;
        
        recreateModel();
    }

    public boolean isTemporadaListDisabled() {
        return temporadaListDisabled;
    }

    public boolean isFaseListDisabled() {
        return faseListDisabled;
    }

    public boolean isTableDisabled() {
        return tableDisabled;
    }

    public List<Grupo> getGruposFiltered() {
        return gruposFiltered;
    }

    public void setGruposFiltered(List<Grupo> gruposFiltered) {
        this.gruposFiltered = gruposFiltered;
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
    
    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(getJpaController().findGrupoEntities(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(getJpaController().findGrupoEntities(), true);
    }

    @FacesConverter(forClass = Grupo.class)
    public static class GrupoControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            GrupoController controller = (GrupoController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "grupoController");
            return controller.getJpaController().findGrupo(getKey(value));
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
            if (object instanceof Grupo) {
                Grupo o = (Grupo) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Grupo.class.getName());
            }
        }
    }
}
