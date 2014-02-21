package com.spontecorp.littleligues.jsfcontroller;

import com.spontecorp.littleligues.jpacontroller.VideoJpaController;
import com.spontecorp.littleligues.model.Video;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
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
@ManagedBean(name = "videoBean")
@SessionScoped
public class VideoBean implements Serializable{

    private transient VideoJpaController jpaController = null;
    private transient DataModel<Video> items;
    private List<Video> lista;
    private Video entity;
    private UploadedFile uploadedFile;
    private static final Logger logger = LoggerFactory.getLogger(VideoBean.class);
    private String urlString = null;
    private boolean editable = false;
    private boolean cambiarImagen = false;
    
    /**
     * Creates a new instance of VideoBean
     */
    public VideoBean() {
    }
    
    @PostConstruct
    public void init(){
        getItems();
    }
    
    private VideoJpaController getJpaController() {
        if (jpaController == null) {
            jpaController = new VideoJpaController(Persistence.createEntityManagerFactory("LittleLigues2PU"));
        }
        return jpaController;
    }

    public Video getEntity() {
        if (entity == null) {
            entity = new Video();
        }
        return entity;
    }
    
    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public String getUrlString() {
        return urlString;
    }

    public void setUrlString(String urlString) {
        int w = urlString.indexOf("watch?v=");
        String newUrl = "";
        if( w > 0){
            newUrl = urlString.substring(0, w) + "v/" + urlString.substring(w + 8, urlString.length());
        }else{
            newUrl = urlString;
        }
        this.urlString = newUrl;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public boolean isCambiarImagen() {
        return cambiarImagen;
    }

    public void setCambiarImagen(boolean cambiarImagen) {
        this.cambiarImagen = cambiarImagen;
    }
   
    public String prepareCreate() {
        entity = new Video();
        return "/admin/video/Create";
    }
    
    public String create() {
        try {
            entity.setUrl(urlString);
            getJpaController().create(entity);
            String mensaje = "Video cargado exitósamente";
            FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, mensaje, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, facesMsg);
            return prepareList();
        } catch (Exception e) {
            logger.error("Error de persistencia: ", e);
            String mensaje = "Error de Persistencia";
            FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, mensaje, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, facesMsg);
            return null;
        }
    }
    
    public String prepareEdit(){
        entity = (Video)getItems().getRowData();
        return "Edit";
    }
    
    public String prepareEditFromView() {
        return "Edit";
    }
    
    public String update() {
        try {
            setUrlString(entity.getUrl());
            entity.setUrl(urlString);
            getJpaController().edit(entity);
            String mensaje = "Video actualizado con éxito";
            FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, mensaje, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, facesMsg);
            return "List";
        } catch (Exception e) {
            String mensaje = "Ha ocurrido un error: " + e;
            FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, mensaje, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, facesMsg);
            return null;
        }
    }
    
    public String prepareDestroy() {
        entity = (Video) getItems().getRowData();
        return "Delete";
    }
    
    public String prepareDestroyFromView() {
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
            String mensaje = "El video fue eliminado con éxito";
            FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, mensaje, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, facesMsg);
        } catch (Exception e) {
            String mensaje = "Ha ocurrido un error: " + e;
            FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, mensaje, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, facesMsg);
        }
    }
    
    public String prepareList() {
        recreateModel();
        return "List";
    }
   
    public DataModel<Video> getItems() {
        if (items == null) {
            lista = getJpaController().findVideoEntities();
            items = new ListDataModel<>(lista);
        }
        return items;
    }

    private void recreateModel() {
        urlString = null;
        items = null;
    }
    
    public String verMas() {
        entity = (Video) items.getRowData();
        return "video_detalle";
    }
}
