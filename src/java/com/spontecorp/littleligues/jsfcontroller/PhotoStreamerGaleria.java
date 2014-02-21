package com.spontecorp.littleligues.jsfcontroller;

import com.spontecorp.littleligues.jpacontroller.FotoJpaController;
import com.spontecorp.littleligues.model.Foto;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.Persistence;

/**
 *
 * @author Casper
 */
@ManagedBean
@SessionScoped
public class PhotoStreamerGaleria extends PhotoStreamerAbs{

    private FotoJpaController jpaController;
    /**
     * Creates a new instance of PhotoStreamerGaleria
     */
    public PhotoStreamerGaleria() {
    }

    private FotoJpaController getJpaController() {
        if (jpaController == null) {
            jpaController = new FotoJpaController(Persistence.createEntityManagerFactory("LittleLigues2PU"));
        }
        return jpaController;
    }

    @Override
    public InputStream getInputStream() {
        Foto foto = getJpaController().findFoto(Integer.valueOf(getPhotoId()));
        return new ByteArrayInputStream(foto.getImagen());
    }

    @Override
    public String getPhotoId() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        return externalContext.getRequestParameterMap().get("photo_id");
    }
}
