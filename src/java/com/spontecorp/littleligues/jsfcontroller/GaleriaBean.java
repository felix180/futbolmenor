package com.spontecorp.littleligues.jsfcontroller;

import com.spontecorp.littleligues.jpacontroller.FotoJpaController;
import com.spontecorp.littleligues.model.Foto;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.persistence.Persistence;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Casper
 */
@ManagedBean
@SessionScoped
public class GaleriaBean implements Serializable{
    
    private transient FotoJpaController jpaController = null;
    private List<Foto> lista;
    private transient DataModel<Foto> items; 
    private Foto entity;
    private UploadedFile uploadedFile;
    private static final Logger logger = LoggerFactory.getLogger(GaleriaBean.class);

    /**
     * Creates a new instance of GaleriaBean
     */
    public GaleriaBean() {
    }
    
    private FotoJpaController getJpaController(){
        if(jpaController == null){
            jpaController = new FotoJpaController(Persistence.createEntityManagerFactory("LittleLigues2PU"));
        }
        return jpaController;
    }

    public Foto getEntity() {
        if(entity == null){
            entity = new Foto();
        }
        return entity;
    }

    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public DataModel<Foto> getItems() {
        if(items == null){
            lista = getJpaController().findFotoEntities();
            items = new ListDataModel<>(lista);
        }
        return items;
    }
        
    public String prepareList() {
        recreateModel();
        return "List";
    }
    
    private void recreateModel() {
        items = null;
    }
    
    public void upload() throws IOException {
        if (uploadedFile != null) {
            String contentType = uploadedFile.getContentType();

            byte[] bytes = uploadedFile.getContents();
            entity.setImagen(bytes);

            String mensaje = uploadedFile.getFileName() + "del tipo: " + contentType + " se ha cargado.";
            FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, mensaje, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, facesMsg);
        }
    }
    
    public String prepareCreate() {
        entity = new Foto();
        return "/admin/galeria/Create";
    }
    
    public String create() {
        try {
            getJpaController().create(entity);
            String mensaje = "Foto para galería agregada exitósamente";
            FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, mensaje, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, facesMsg);
            return prepareCreate();
        } catch (Exception e) {
            logger.error("Error de persistencia: ", e);
            String mensaje = "Error de Persistencia";
            FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, mensaje, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, facesMsg);
            return null;
        }
    }
}
