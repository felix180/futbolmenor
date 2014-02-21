/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.littleligues.jsfcontroller;

import com.spontecorp.littleligues.jpacontroller.CronistaJpaController;
import com.spontecorp.littleligues.model.Cronista;
import com.spontecorp.littleligues.utils.LittleLiguesUtils;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Casper
 */
@ManagedBean
@SessionScoped
public class PhotoStreamerCronista extends PhotoStreamerAbs{
    private CronistaJpaController jpaController = null;

    /**
     * Creates a new instance of PhotoStreamerBase
     */
    public PhotoStreamerCronista() {
    }

    private CronistaJpaController getJpaController() {
        if (jpaController == null) {
            jpaController = new CronistaJpaController(LittleLiguesUtils.getEmf());
        }
        return jpaController;
    }

    @Override
    public InputStream getInputStream() {
        Cronista cronista = getJpaController().findCronista(Integer.valueOf(getPhotoId()));
        byte[] foto = cronista.getFoto();
        if (foto != null) {
            return new ByteArrayInputStream(foto);
        } else {
            return new ByteArrayInputStream(new byte[0]);
        }
    }

    @Override
    public String getPhotoId() {
        return LittleLiguesUtils.getRequestParameter("photo_id");
    }
}
