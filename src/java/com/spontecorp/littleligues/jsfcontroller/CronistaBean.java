package com.spontecorp.littleligues.jsfcontroller;

import com.spontecorp.littleligues.jpacontroller.CronistaJpaController;
import com.spontecorp.littleligues.model.Cronista;
import com.spontecorp.littleligues.utils.LittleLiguesUtils;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import org.eclipse.persistence.exceptions.DatabaseException;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Casper
 */
@ManagedBean(name="columnistaBean")
@SessionScoped
public class CronistaBean implements Serializable{

    private transient CronistaJpaController jpaController = null;
    private transient DataModel<Cronista> items;
    private Cronista entity;
    private UploadedFile uploadedFile;
    private static final Logger logger = LoggerFactory.getLogger(CronistaBean.class);
    /**
     * Creates a new instance of CronistaBean
     */
    public CronistaBean() {
    }
    
    private CronistaJpaController getJpaController(){
        if(jpaController == null){
            jpaController = new CronistaJpaController(LittleLiguesUtils.getEmf());
        }
        return jpaController;
    }
    
    public Cronista getEntity() {
        if (entity == null) {
            entity = new Cronista();
        }
        return entity;
    }
    
    public DataModel<Cronista> getItems() {
        if (items == null) {
            items = new ListDataModel<>(getJpaController().findCronistaEntities());
        }
        return items;
    }
    
    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }
    
    public StreamedContent getStreamedImageById() {
        FacesContext context = FacesContext.getCurrentInstance();

        if (context.getRenderResponse()) {
            return new DefaultStreamedContent();
        } else {
            String id = LittleLiguesUtils.getRequestParameter("id");
            Cronista columnista = getJpaController().findCronista(Integer.valueOf(id));
            InputStream inputStream = new ByteArrayInputStream(columnista.getFoto());
            return new DefaultStreamedContent(inputStream, "image/jpeg");
        }
    }
    
    public SelectItem[] getItemsAvailableSelectOne() {
        return LittleLiguesUtils.getSelectItems(getJpaController().findCronistaEntities(), true);
    }

    public void upload() throws IOException {
        if (uploadedFile != null) {
            String contentType = uploadedFile.getContentType();

            byte[] bytes = uploadedFile.getContents();
            entity.setFoto(bytes);

            String mensaje = uploadedFile.getFileName() + "del tipo: " + contentType + " se ha cargado.";
            LittleLiguesUtils.addSuccessMessage(mensaje);
        }
    }
    
    public String prepareCreate() {
        logger.info("Llegó al método prepareCreate()");
        entity = new Cronista();
        return "/admin/columnista/Create";
    }
    
    public String create() {
        try {
            upload();
            getJpaController().create(entity);
            String mensaje = "Columnista creado(a) exitósamente";
            LittleLiguesUtils.addSuccessMessage(mensaje);
            return prepareCreate();
        } catch (DatabaseException e) {
            logger.error("Error de persistencia: ", e);
            String mensaje = "La foto cargada es muy grande, por favor comprimala max 98Kb";
            LittleLiguesUtils.addErrorMessage(e, mensaje);
            return null;
        } catch (Exception e) {
            logger.error("Error de persistencia: ", e);
            String mensaje = "Ha ocurrido un error: ";
            LittleLiguesUtils.addErrorMessage(e, mensaje);
            return null;
        }
    }
    
    public String prepareEdit() {
        entity = (Cronista) getItems().getRowData();
        return "Edit";
    }
    
    public String prepareEditFromView(){
        return "Edit";
    }
    
    public String update() {
        try {
            upload();
            getJpaController().edit(entity);
            String mensaje = "Columnista actualizado(a) con éxito";
            LittleLiguesUtils.addSuccessMessage(mensaje);
            return "List";
        } catch (Exception e) {
            String mensaje = "Ha ocurrido un error: ";
            LittleLiguesUtils.addErrorMessage(e, mensaje);
            return null;
        }
    }
    
    private void recreateModel() {
        items = null;
    }
    
    public String prepareDestroy() {
        entity = (Cronista) getItems().getRowData();
        return "Delete";
    }
    
    public String prepareDestroyFromView(){
        return "Delete";
    }

    public String destroy() {
        performDestroy();
        recreateModel();
        return "List";
    }

    private void performDestroy() {
        try {
            getJpaController().destroy(entity.getId());
            String mensaje = "Columnista eliminado(a) con éxito";
            LittleLiguesUtils.addSuccessMessage(mensaje);
        } catch (Exception e) {
            String mensaje = "Ha ocurrido un error: ";
            LittleLiguesUtils.addErrorMessage(e, mensaje);
        }
    }
    
    public String prepareList() {
        recreateModel();
        return "List";
    }
    
    public String prepareView() {
        entity = (Cronista) getItems().getRowData();
        return "View";
    }
    
    @FacesConverter(forClass = Cronista.class)
    public static class CronistaBeanConverter implements Converter{

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            CronistaBean controller = (CronistaBean) facesContext.getApplication().getELResolver(). 
                                                    getValue(facesContext.getELContext(), null, "columnistaBean");
            return controller.getJpaController().findCronista(getKey(value));
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Cronista) {
                Cronista o = (Cronista) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + CronistaBean.class.getName());
            }
        }
        
        private static java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }
        
        private static String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }
        
    }
    
    /*
     * @FacesConverter(forClass = Producto.class)
     public static class ProductoControllerConverter implements Converter {

     public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
     if (value == null || value.length() == 0) {
     return null;
     }
     ProductoController controller = (ProductoController) facesContext.getApplication().getELResolver().
     getValue(facesContext.getELContext(), null, "productoController");
     return controller.getJpaController().findProducto(getKey(value));
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
     if (object instanceof Producto) {
     Producto o = (Producto) object;
     return getStringKey(o.getId());
     } else {
     throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + ProductoController.class.getName());
     }
     }
     }
     */
}
