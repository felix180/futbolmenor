package com.spontecorp.littleligues.jsfcontroller;

import com.spontecorp.littleligues.jpacontroller.CronicaJpaController;
import com.spontecorp.littleligues.model.Cronica;
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
 * @author jgcastillo
 */
@ManagedBean(name = "columnaBean")
@SessionScoped
public class CronicaBean implements Serializable {

    private transient CronicaJpaController jpaController = null;
    private transient DataModel<Cronica> items;
    private Cronica entity;
    private UploadedFile uploadedFile;
    private boolean activo = false;
    private static final Logger logger = LoggerFactory.getLogger(CronicaBean.class);

    /**
     * Creates a new instance of CronicaBean
     */
    public CronicaBean() {
    }

    public CronicaJpaController getJpaController() {
        if (jpaController == null) {
            jpaController = new CronicaJpaController(LittleLiguesUtils.getEmf());
        }
        return jpaController;
    }

    public Cronica getEntity() {
        if (entity == null) {
            entity = new Cronica();
        }
        return entity;
    }

    public DataModel<Cronica> getItems() {
        if (items == null) {
            items = new ListDataModel<>(getJpaController().findCronicaEntities());
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
            Cronica columna = getJpaController().findCronica(Integer.valueOf(id));
            InputStream inputStream = new ByteArrayInputStream(columna.getFoto());
            return new DefaultStreamedContent(inputStream, "image/jpeg");
        }
    }

    public boolean isActivo() {
        if (entity.getActiva() != null) {
            if (entity.getActiva() == LittleLiguesUtils.ARTICULO_ACTIVO) {
                activo = true;
            } else {
                activo = false;
            }
        } else {
            activo = false;
        }
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
        if (activo) {
            entity.setActiva(LittleLiguesUtils.ARTICULO_ACTIVO);
        } else {
            entity.setActiva(LittleLiguesUtils.ARTICULO_INACTIVO);
        }
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
        entity = new Cronica();
        return "/admin/columna/Create";
    }

    public String create() {
        try {
            upload();

            getJpaController().create(entity);
            String mensaje = "Columna creada exitósamente";
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
        entity = (Cronica) getItems().getRowData();
        return "Edit";
    }

    public String prepareEditFromView() {
        return "Edit";
    }

    public String update() {
        try {
            upload();
            getJpaController().edit(entity);
            String mensaje = "Columna actualizada con éxito";
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
        entity = (Cronica) getItems().getRowData();
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
            String mensaje = "Columna eliminada con éxito";
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
        entity = (Cronica) getItems().getRowData();
        return "View";
    }
}
