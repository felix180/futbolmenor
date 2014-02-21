package com.spontecorp.littleligues.jsfcontroller.liga;

import com.spontecorp.littleligues.jpacontroller.CanchaJpaController;
import com.spontecorp.littleligues.jpacontroller.DireccionJpaController;
import com.spontecorp.littleligues.jpacontroller.LocalidadJpaController;
import com.spontecorp.littleligues.jsfcontroller.liga.util.JsfUtil;
import com.spontecorp.littleligues.jsfcontroller.liga.util.PaginationHelper;
import com.spontecorp.littleligues.model.liga.Cancha;
import com.spontecorp.littleligues.model.liga.Direccion;
import com.spontecorp.littleligues.model.liga.Localidad;
import com.spontecorp.littleligues.utils.LittleLiguesUtils;
import java.io.Serializable;
import java.util.ArrayList;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Casper
 */
@ManagedBean(name = "canchaController")
@SessionScoped
public class CanchaController implements Serializable{

    private Cancha current;
    private Localidad localidad;
    private Direccion direccion;
    private DataModel items = null;
    private CanchaJpaController jpaController = null;
    private DireccionJpaController direccionJpaController = null;
    private LocalidadJpaController localidadJpaController = null;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    
    private static final Logger logger = LoggerFactory.getLogger(CanchaController.class);
    /**
     * Creates a new instance of CanchaController
     */
    public CanchaController() {
    }
    
    public Cancha getSelected() {
        if (current == null) {
            current = new Cancha();
            localidad = new Localidad();
            direccion = new Direccion();
            direccion.setLocalidadId(localidad);
            current.setDireccionId(direccion);
            selectedItemIndex = -1;
        }
        return current;
    }

    public CanchaJpaController getJpaController() {
        if(jpaController == null){
            jpaController = new CanchaJpaController(LittleLiguesUtils.getEmf());
        }
        return jpaController;
    }
    
    public DireccionJpaController getDireccionJpaController() {
        if (direccionJpaController == null) {
            direccionJpaController = new DireccionJpaController(LittleLiguesUtils.getEmf());
        }
        return direccionJpaController;
    }

    public LocalidadJpaController getLocalidadJpaController() {
        if (localidadJpaController == null) {
            localidadJpaController = new LocalidadJpaController(LittleLiguesUtils.getEmf());
        }
        return localidadJpaController;
    }
    
    public Direccion getDireccion() {
        direccion = current.getDireccionId();
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    public Localidad getLocalidad() {
        localidad = current.getDireccionId().getLocalidadId();
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
                    return getJpaController().getCanchaCount();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getJpaController().findCanchaEntities(getPageSize(), getPageFirstItem()));
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
        current = (Cancha) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Cancha();
        localidad = new Localidad();
        direccion = new Direccion();
        direccion.setLocalidadId(localidad);
        current.setDireccionId(direccion);
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            direccion.setLocalidadId(localidad);
            getDireccionJpaController().create(direccion);
            current.setDireccionId(direccion);
            getJpaController().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("CanchaCreated"));
            return prepareCreate();
        } catch (Exception e) {
            logger.error(ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"), e);
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Cancha) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }
    
    public String prepareEditFromView() {
        return "Edit";
    }

    public String update() {
        try {
            direccion.setLocalidadId(localidad);
            getDireccionJpaController().edit(direccion);
            current.setDireccionId(direccion);
            getJpaController().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("CanchaUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareDestroy() {
        current = (Cancha) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Delete";
    }

    public String destroy() {
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }
    
    public String prepareDestroyFromView(){
        return "Delete";
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
            getDireccionJpaController().destroy(current.getDireccionId().getId());
            getJpaController().destroy(current.getId());
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("CanchaDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    private void updateCurrentItem() {
        int count = getJpaController().getCanchaCount();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getJpaController().findCanchaEntities(1, selectedItemIndex).get(0);
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
        return JsfUtil.getSelectItems(getJpaController().findCanchaEntities(), false);
    }
    
    public List<String> getItemsAvailableSelelectManyStr(){
        List<Cancha> canchas = getCanchasAvailable();
        List<String> canchaList = new ArrayList();
        for(Cancha cancha : canchas){
            canchaList.add(cancha.getNombre());
        }
        return canchaList;
    }
    
    public List<Cancha> getCanchasAvailable(){
        return getJpaController().findCanchaEntities();
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(getJpaController().findCanchaEntities(), true);
    }
    
    @FacesConverter(forClass = Cancha.class)
    public static class CanchaControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            CanchaController controller = (CanchaController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "canchaController");
            return controller.getJpaController().findCancha(getKey(value));
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
            if (object instanceof Cancha) {
                Cancha o = (Cancha) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Cancha.class.getName());
            }
        }
    }
}
