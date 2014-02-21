package com.spontecorp.littleligues.jsfcontroller;

import com.spontecorp.littleligues.jpacontroller.CarruselHomeJpaController;
import com.spontecorp.littleligues.jsfcontroller.util.JsfUtil;
import com.spontecorp.littleligues.jsfcontroller.util.PaginationHelper;
import com.spontecorp.littleligues.model.CarruselHome;
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
import javax.persistence.Persistence;

@ManagedBean(name = "carruselHomeController")
@SessionScoped
public class CarruselHomeController implements Serializable {

    private CarruselHome current;
    private DataModel items = null;
    private CarruselHomeJpaController jpaController = null;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private CarruselHomeBean carruselHomeBean;

    public CarruselHomeController() {
    }

    public CarruselHome getSelected() {
        if (current == null) {
            current = new CarruselHome();
            selectedItemIndex = -1;
        }
        return current;
    }

    private CarruselHomeJpaController getJpaController() {
        if (jpaController == null) {
            jpaController = new CarruselHomeJpaController(Persistence.createEntityManagerFactory("LittleLigues2PU"));
        }
        return jpaController;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getJpaController().getCarruselHomeCount();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getJpaController().findCarruselHomeEntities(getPageSize(), getPageFirstItem()));
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
        current = (CarruselHome) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new CarruselHome();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            getJpaController().create(current);
            //carruselHomeBean.recreateLigaModel();
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("CarruselHomeCreated"));
            return prepareList();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (CarruselHome) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getJpaController().edit(current);
            //carruselHomeBean.recreateLigaModel();
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("CarruselHomeUpdated"));
            return "List";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (CarruselHome) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
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
            //carruselHomeBean.recreateLigaModel();
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("CarruselHomeDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    private void updateCurrentItem() {
        int count = getJpaController().getCarruselHomeCount();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getJpaController().findCarruselHomeEntities(1, selectedItemIndex).get(0);
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
        return JsfUtil.getSelectItems(getJpaController().findCarruselHomeEntities(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(getJpaController().findCarruselHomeEntities(), true);
    }

    @FacesConverter(forClass = CarruselHome.class)
    public static class CarruselHomeControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            CarruselHomeController controller = (CarruselHomeController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "carruselHomeController");
            return controller.getJpaController().findCarruselHome(getKey(value));
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
            if (object instanceof CarruselHome) {
                CarruselHome o = (CarruselHome) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + CarruselHomeController.class.getName());
            }
        }
    }
}
