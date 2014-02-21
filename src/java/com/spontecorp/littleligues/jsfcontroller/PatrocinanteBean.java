package com.spontecorp.littleligues.jsfcontroller;

import com.spontecorp.littleligues.jpacontroller.PatrocinanteJpaController;
import com.spontecorp.littleligues.model.Patrocinante;
import com.spontecorp.littleligues.utils.LittleLiguesUtils;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
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
@ManagedBean
@SessionScoped
public class PatrocinanteBean implements Serializable {

    private PatrocinanteJpaController jpaController = null;
    private DataModel<Patrocinante> items;
    private Patrocinante entity;
    private UploadedFile uploadedFile;
    private boolean activo = false;
    private static final Logger logger = LoggerFactory.getLogger(CronicaBean.class);

    /**
     * Creates a new instance of PatrocinanteBean
     */
    public PatrocinanteBean() {
    }

    public PatrocinanteJpaController getJpaController() {
        if (jpaController == null) {
            jpaController = new PatrocinanteJpaController(LittleLiguesUtils.getEmf());
        }
        return jpaController;
    }

    public DataModel<Patrocinante> getItems() {
        if(items == null){
            items = new ListDataModel<>(getJpaController().findPatrocinanteEntities()); 
        }
        return items;
    }

    public Patrocinante getEntity() {
        return entity;
    }

    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public boolean isActivo() {
        if (entity.getStatus() == LittleLiguesUtils.STATUS_ACTIVO) {
            activo = true;
        } else {
            activo = false;
        }
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
        if (activo) {
            entity.setStatus(LittleLiguesUtils.STATUS_ACTIVO);
        } else {
            entity.setStatus(LittleLiguesUtils.STATUS_INACTIVO);
        }
    }
    
    public StreamedContent getStreamedImageById() {
        FacesContext context = FacesContext.getCurrentInstance();

        if (context.getRenderResponse()) {
            return new DefaultStreamedContent();
        } else {
            String id = LittleLiguesUtils.getRequestParameter("id");
            Patrocinante patrocinante = getJpaController().findPatrocinante(Integer.valueOf(id));
            InputStream inputStream = new ByteArrayInputStream(patrocinante.getLogo());
            return new DefaultStreamedContent(inputStream, "image/jpeg");
        }
    }
    
    public void upload() throws IOException {
        if (uploadedFile != null) {
            String contentType = uploadedFile.getContentType();

            byte[] bytes = uploadedFile.getContents();
            entity.setLogo(bytes);

            String mensaje = uploadedFile.getFileName() + "del tipo: " + contentType + " se ha cargado.";
            LittleLiguesUtils.addSuccessMessage(mensaje);
        }
    }
    
    public String prepareList() {
        recreateModel();
        return "List";
    }
    
    private void recreateModel() {
        items = null;
    }
    
    public String prepareCreate() {
        entity = new Patrocinante();
        return "/admin/patrocinante/Create";
    }
    
    public String prepareEdit() {
        entity = (Patrocinante) getItems().getRowData();
        return "Edit";
    }
    
    public String prepareEditFromView() {
        return "Edit";
    }
    
    public String prepareView() {
        entity = (Patrocinante) getItems().getRowData();
        return "View";
    }
    
    public String prepareDestroy() {
        entity = (Patrocinante) getItems().getRowData();
        return "Delete";
    }
    
    public String prepareDestroyFromView() {
        return "Delete";
    }
    
    public String create() {
        try {
            upload();
            getJpaController().create(entity);
            String mensaje = "Patrocinante agregado exitósamente";
            LittleLiguesUtils.addSuccessMessage(mensaje);
            return prepareCreate();
        } catch (DatabaseException e) {
            logger.error("Error de persistencia: ", e);
            String mensaje = "El logo cargado es muy grande, por favor comprimala max 98Kb";
            LittleLiguesUtils.addErrorMessage(e, mensaje);
            return null;
        } catch (Exception e) {
            logger.error("Error de persistencia: ", e);
            String mensaje = "Ha ocurrido un error: ";
            LittleLiguesUtils.addErrorMessage(e, mensaje);
            return null;
        }
    }
    
    public String update() {
        try {
            upload();
            getJpaController().edit(entity);
            String mensaje = "Patrocinante actualizado con éxito";
            LittleLiguesUtils.addSuccessMessage(mensaje);
            return "List";
        } catch (Exception e) {
            String mensaje = "Ha ocurrido un error: ";
            LittleLiguesUtils.addErrorMessage(e, mensaje);
            return null;
        }
    }
    
    public String destroy() {
        performDestroy();
        recreateModel();
        return "List";
    }

    private void performDestroy() {
        try {
            getJpaController().destroy(entity.getId());
            String mensaje = "Patrocinante eliminado con éxito";
            LittleLiguesUtils.addSuccessMessage(mensaje);
        } catch (Exception e) {
            String mensaje = "Ha ocurrido un error: ";
            LittleLiguesUtils.addErrorMessage(e, mensaje);
        }
    }
}
