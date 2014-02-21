/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.littleligues.jsfcontroller;

import com.spontecorp.littleligues.jpacontroller.NoticiaJpaController;
import com.spontecorp.littleligues.model.Noticia;
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
public class PhotoStreamerNoticia extends PhotoStreamerAbs {

    private NoticiaJpaController jpaController = null;

    /**
     * Creates a new instance of PhotoStreamerBase
     */
    public PhotoStreamerNoticia() {
    }

    private NoticiaJpaController getJpaController() {
        if (jpaController == null) {
            jpaController = new NoticiaJpaController(Persistence.createEntityManagerFactory("LittleLigues2PU"));
        }
        return jpaController;
    }

//    @Override
//    public InputStream getInputStream() {
//        Noticia noticia = getJpaController().findNoticia(Integer.valueOf(getPhotoId()));
//        byte[] foto = noticia.getFoto();
//        if(foto != null){
//            return new ByteArrayInputStream(foto);
//        } else {
//            return new ByteArrayInputStream(new byte[0]);
//        }
//        //return  new ByteArrayInputStream(noticia.getFoto());
//    }

//    @Override
//    public String getPhotoId() {
//        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
//        return externalContext.getRequestParameterMap().get("photo_id");
//    }

    @Override
    public InputStream getInputStream() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getPhotoId() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
