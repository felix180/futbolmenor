package com.spontecorp.littleligues.jsfcontroller;

import com.spontecorp.littleligues.jpacontroller.extentions.NoticiaJpaControllerExt;
import com.spontecorp.littleligues.model.Noticia;
import com.spontecorp.littleligues.utils.LittleLiguesUtils;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import org.eclipse.persistence.exceptions.DatabaseException;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Casper
 */
@ManagedBean(name = "noticiaBean")
@SessionScoped
public final class NoticiaBean implements Serializable {

    private transient NoticiaJpaControllerExt jpaController = null;
    private transient DataModel<Noticia> items;
    private List<Noticia> lista;
    private Noticia entity;
    private Noticia mainNoticia = null;
    private UploadedFile uploadedFile;
    private boolean hayImagen;
    private boolean pagPrincipal = false;
    private static final int PAG_PRINCIPAL = 1;
    private static final int NO_PAG_PRINCIPAL = 0;
    private static final Logger logger = LoggerFactory.getLogger(NoticiaBean.class);
    private LittleLiguesUtils utils = new LittleLiguesUtils();
    public static final String SECTION_NOTICIAS = "noticias";
    private String IMAGE_RETURN = "";

    /**
     * Creates a new instance of NoticiaBean
     */
    public NoticiaBean() {
        if (mainNoticia == null) {
            getMainNoticia();
        }
    }

    private NoticiaJpaControllerExt getJpaController() {
        if (jpaController == null) {
            jpaController = new NoticiaJpaControllerExt(LittleLiguesUtils.getEmf());
        }
        return jpaController;
    }

    public Noticia getEntity() {
        if (entity == null) {
            entity = new Noticia();
        }
        return entity;
    }

    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public boolean isPagPrincipal() {
        return pagPrincipal;
    }

    public void setPagPrincipal(boolean pagPrincipal) {
        this.pagPrincipal = pagPrincipal;
    }

    public Noticia getMainNoticia() {
        // mainNoticia = getJpaController().findNoticiaByPrincipal(LittleLiguesUtils.PAG_PRINCIPAL);
        return mainNoticia;
    }

    public boolean isHayImagen() {
        return hayImagen;
    }

    /**
     * Metodo para validar las extensiones, peso de la imagen y copiar imagen al
     * Directorio
     *
     * @throws IOException
     */
    public void upload() throws IOException {
        if (uploadedFile != null) {
            String nombreImagen = String.valueOf(entity.getId());
            //Se carga el archivo
            boolean retorno = utils.saveFile(uploadedFile, nombreImagen, SECTION_NOTICIAS);
            if (retorno) {
                String extension = uploadedFile.getFileName().substring(uploadedFile.getFileName().lastIndexOf(".") + 1);
                nombreImagen = nombreImagen + "." + extension;
                entity.setFoto(nombreImagen);
            }
        }
    }

    public String returnImage() {
        return utils.getIMAGE_RETURN();
    }

    public String prepareCreate() {
        entity = new Noticia();
        recreateModel();
        return "/admin/noticia/Create";
    }

    public String create() {
        try {
            //Valido si es Principal
            if (isPagPrincipal()) {
                entity.setPrincipal(PAG_PRINCIPAL);
            } else {
                entity.setPrincipal(NO_PAG_PRINCIPAL);
            }
            
            getJpaController().create(entity);
            //Cargo una imagen si existe
            update();

            String mensaje = "Noticia creada exitósamente";
            FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, mensaje, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, facesMsg);
            return prepareCreate();
        } catch (DatabaseException e) {
            logger.error("Error de persistencia: ", e);
            String mensaje = "La foto cargada es muy grande, por favor comprimala max 98Kb";
            FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, mensaje, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, facesMsg);
            return null;
        } catch (Exception e) {
            logger.error("Error de persistencia: ", e);
            String mensaje = "Ha ocurrido un error: " + e;
            FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, mensaje, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, facesMsg);
            return null;
        }
    }

    public String prepareEdit() {
        entity = (Noticia) getItems().getRowData();
        if(entity.getPrincipal() == PAG_PRINCIPAL){
            pagPrincipal = true;
        } else {
            pagPrincipal = false;
        }
        return "Edit";
    }

    public String prepareEditFromDelete() {
        return "Edit";
    }

    public String update() {
        try {
            //Valido si es Principal
            if (isPagPrincipal()) {
                entity.setPrincipal(PAG_PRINCIPAL);
            } else {
                entity.setPrincipal(NO_PAG_PRINCIPAL);
            }
            //Cargo una imagen si existe
            upload();

            getJpaController().edit(entity);
            String mensaje = "La noticia fue actualizada con éxito";
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
        entity = (Noticia) getItems().getRowData();
        return "Delete";
    }

    public String prepareDestroyFromEdit() {
        return "Delete";
    }

    public String destroy() {
        performDestroy();
        recreateModel();
        return "List";
    }

    private void performDestroy() {
        try {

            //Elimino la imagen actual si existe
            String nombreImagenActual = entity.getFoto();
            if (nombreImagenActual != null) {
                utils.deleteFile(nombreImagenActual, SECTION_NOTICIAS);
            }
            getJpaController().destroy(entity.getId());

            String mensaje = "La noticia fue eliminada con éxito";
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

    public DataModel<Noticia> getItems() {
        if (items == null) {
            lista = getJpaController().findNoticiaEntities();
            items = new ListDataModel<>(lista);
        }
        return items;
    }

    private void recreateModel() {
        items = null;
        pagPrincipal = false;
    }

    public boolean isHayCometario() {
        if (entity.getComentario() != null && entity.getComentario().length() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public String verMas() {
        entity = (Noticia) items.getRowData();
        LittleLiguesUtils.flashScope().put("noticiaBean", this);
        return "noticias?faces-redirect=true";
    }

    public String verMain() {
        entity = mainNoticia;
        LittleLiguesUtils.flashScope().put("noticiaBean", this);
        return "noticias?faces-redirect=true";
    }

    private void fotoPagPrincipal() {
        try {
            //  Noticia old_Noticia = jpaController.findNoticiaByPrincipal(LittleLiguesUtils.PAG_PRINCIPAL);
            //  old_Noticia.setPrincipal(LittleLiguesUtils.NO_PAG_PRINCIPAL);
            //  jpaController.edit(old_Noticia);
            //    entity.setPrincipal(LittleLiguesUtils.PAG_PRINCIPAL);
        } catch (Exception e) {
        }

    }

    public String getIMAGE_RETURN() {
        IMAGE_RETURN = utils.getIMAGE_RETURN() + utils.path_images_noticias;
        return IMAGE_RETURN;
    }

    public void setIMAGE_RETURN(String IMAGE_RETURN) {
        this.IMAGE_RETURN = IMAGE_RETURN;
    }
}
