package com.spontecorp.littleligues.jsfcontroller.torneo;

import com.spontecorp.littleligues.jpacontroller.LigaJpaController;
import com.spontecorp.littleligues.jpacontroller.extentions.TemporadaJpaControllerExt;
import com.spontecorp.littleligues.model.torneo.Temporada;
import com.spontecorp.littleligues.jsfcontroller.torneo.util.JsfUtil;
import com.spontecorp.littleligues.jsfcontroller.torneo.util.PaginationHelper;
import com.spontecorp.littleligues.model.liga.Liga;
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

@ManagedBean(name = "temporadaController")
@SessionScoped
public class TemporadaController implements Serializable {

    private Temporada current;
    private DataModel items = null;
    private TemporadaJpaControllerExt jpaController = null;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    
    private Liga liga;
    private boolean tableDisabled = true;

    public TemporadaController() {
    }

    public Temporada getSelected() {
        if (current == null) {
            current = new Temporada();
            selectedItemIndex = -1;
        }
        return current;
    }

    private TemporadaJpaControllerExt getJpaController() {
        if (jpaController == null) {
            jpaController = new TemporadaJpaControllerExt(LittleLiguesUtils.getEmf());
        }
        return jpaController;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {
                @Override
                public int getItemsCount() {
                    return getJpaController().getTemporadaCount();
                }

                @Override
                public DataModel createPageDataModel() {
                    if(liga == null){
                        return new ListDataModel(getJpaController().findTemporadaEntities(getPageSize(), getPageFirstItem()));
                    } else {
                        return new ListDataModel(getJpaController().findTemporadaEntitiesOnLiga(liga,getPageSize(), getPageFirstItem()));
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
        current = (Temporada) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Temporada();
        selectedItemIndex = -1;
        return "Create";
    }
    
    public String prepareConfig() {
        current = (Temporada) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "fase/List";
    }

    public String create() {
        try {
            getJpaController().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("TemporadaCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Temporada) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getJpaController().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("TemporadaUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }
    
    public String prepareDestroy() {
        current = (Temporada) getItems().getRowData();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("TemporadaDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    private void updateCurrentItem() {
        int count = getJpaController().getTemporadaCount();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getJpaController().findTemporadaEntities(1, selectedItemIndex).get(0);
        }
    }

    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }

    public void recreateModel() {
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
        tableDisabled = false;
        this.liga = liga;
        
        recreateModel();
    }

    public boolean isTableDisabled() {
        return tableDisabled;
    }
    
    public SelectItem[] getLigasAvalaibleSelectOne() {
        LigaJpaController ligaJpaController = new LigaJpaController(LittleLiguesUtils.getEmf());
        List<Liga> ligas = ligaJpaController.findLigaEntities();
        return com.spontecorp.littleligues.jsfcontroller.liga.util.JsfUtil.getSelectItems(ligas, true);
    }
    
    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(getJpaController().findTemporadaEntities(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(getJpaController().findTemporadaEntities(), true);
    }

    @FacesConverter(forClass = Temporada.class)
    public static class TemporadaControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            TemporadaController controller = (TemporadaController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "temporadaController");
            return controller.getJpaController().findTemporada(getKey(value));
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

        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Temporada) {
                Temporada o = (Temporada) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Temporada.class.getName());
            }
        }
    }
}
