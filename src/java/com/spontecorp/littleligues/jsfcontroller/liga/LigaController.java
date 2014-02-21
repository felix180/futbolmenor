package com.spontecorp.littleligues.jsfcontroller.liga;

import com.spontecorp.littleligues.jpacontroller.DireccionJpaController;
import com.spontecorp.littleligues.jpacontroller.LigaJpaController;
import com.spontecorp.littleligues.jpacontroller.LocalidadJpaController;
import com.spontecorp.littleligues.jsfcontroller.liga.util.JsfUtil;
import com.spontecorp.littleligues.jsfcontroller.liga.util.PaginationHelper;
import com.spontecorp.littleligues.model.liga.Direccion;
import com.spontecorp.littleligues.model.liga.Liga;
import com.spontecorp.littleligues.model.liga.Localidad;
import com.spontecorp.littleligues.utils.LittleLiguesUtils;
import java.io.Serializable;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ManagedBean(name = "ligaController")
@SessionScoped
public class LigaController implements Serializable {

    private Liga current;
    private Direccion direccion;
    private Localidad localidad;
    private DataModel items = null;
    private LigaJpaController jpaController = null;
    private LocalidadJpaController jpaLocalidadController = null;
    private DireccionJpaController jpaDireccionController = null;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private static Logger logger = LoggerFactory.getLogger(LigaController.class);

    public LigaController() {
    }

    public Liga getSelected() {
        if (current == null) {
            current = new Liga();
            direccion = new Direccion();
            localidad = new Localidad();
            selectedItemIndex = -1;
        } else {
            direccion = current.getDireccionId();
            localidad = direccion.getLocalidadId();
        }
        return current;
    }

    private LigaJpaController getJpaController() {
        if (jpaController == null) {
            jpaController = new LigaJpaController(LittleLiguesUtils.getEmf());
        }
        return jpaController;
    }

    private LocalidadJpaController getJpaLocalidadController() {
        if (jpaLocalidadController == null) {
            jpaLocalidadController = new LocalidadJpaController(LittleLiguesUtils.getEmf());
        }
        return jpaLocalidadController;
    }

    private DireccionJpaController getJpaDireccionController() {
        if (jpaDireccionController == null) {
            jpaDireccionController = new DireccionJpaController(LittleLiguesUtils.getEmf());
        }
        return jpaDireccionController;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    public Localidad getLocalidad() {
        return localidad;
    }

    public void setLocalidad(Localidad localidad) {
        this.localidad = localidad;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {
                @Override
                public int getItemsCount() {
                    return getJpaController().getLigaCount();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getJpaController().findLigaEntities(getPageSize(), getPageFirstItem()));
                }
            };
        }
        return pagination;
    }

    public String prepareList() {
        recreateModel();
        return "List";
    }

    public String prepareView() {
        current = (Liga) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Liga();
        direccion = new Direccion();
        localidad = new Localidad();
        direccion.setLocalidadId(localidad);
        current.setDireccionId(direccion);
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            direccion.setLocalidadId(this.localidad);

            getJpaDireccionController().create(direccion);
            current.setDireccionId(this.direccion);

            getJpaController().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("LigaCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Liga) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String prepareEditFromView() {
        return "Edit";
    }

    public String update() {
        try {
            direccion.setLocalidadId(this.localidad);

            getJpaDireccionController().edit(direccion);
            current.setDireccionId(this.direccion);

            getJpaController().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("LigaUpdated"));
            return "View";
        } catch (Exception e) {
            logger.error(ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"), e);
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }
    
    public String prepareDestroyFromView(){
        return "Delete";
    }
    
    public String prepareDestroy(){
        current = (Liga) getItems().getRowData();
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
            getJpaDireccionController().destroy(current.getDireccionId().getId());
            getJpaController().destroy(current.getId());
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("LigaDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    private void updateCurrentItem() {
        int count = getJpaController().getLigaCount();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getJpaController().findLigaEntities(1, selectedItemIndex).get(0);
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

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(getJpaController().findLigaEntities(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(getJpaController().findLigaEntities(), true);
    }

    @FacesConverter(forClass = Liga.class)
    public static class LigaControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            LigaController controller = (LigaController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "ligaController");
            return controller.getJpaController().findLiga(getKey(value));
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
            if (object instanceof Liga) {
                Liga o = (Liga) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Liga.class.getName());
            }
        }
    }
}
