/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.littleligues.jsfcontroller;

import java.io.InputStream;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 *
 * @author Casper
 */
@ManagedBean
@SessionScoped
public abstract class PhotoStreamerAbs {

    private static final Logger logger = LoggerFactory.getLogger(PhotoStreamerAbs.class);
    private static StreamedContent defaultFileContent;
    private StreamedContent fileContent;
    //private NoticiaJpaController jpaController = null;
    
    static {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = contextClassLoader.getResourceAsStream("#{resource['images:Photo - 0.png']}");
        defaultFileContent = new DefaultStreamedContent(inputStream, "image/png");
    }
    
//    private NoticiaJpaController getJpaController() {
//        if (jpaController == null) {
//            jpaController = new NoticiaJpaController(Persistence.createEntityManagerFactory("LittleLigues2PU"));
//        }
//        return jpaController;
//    }
    
    /**
     * Creates a new instance of PhotoStreamer
     */
    public PhotoStreamerAbs() {
    }
    
    public StreamedContent getFileContent() {
        logger.trace("Entered method getFileContent.");
        //ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        //String photoId = externalContext.getRequestParameterMap().get("photo_id");
        String photoId = getPhotoId();
        
        if (photoId == null || photoId.equals("")) {
            fileContent = defaultFileContent;
            logger.info("Id was null or empty. Retrieved default file content.");
        } else {
            int parsedId = Integer.parseInt(photoId);
            if (parsedId < 0 || parsedId > 15) {  // revisar esto cuando esté en producción
                fileContent = defaultFileContent;
                logger.info("Invalid Id. Retrieved default file content.");
            }
            //ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            //InputStream inputStream = contextClassLoader.getResourceAsStream("resources/images/Photo - " + parsedId + ".png");
            
//            Noticia noticia = getJpaController().findNoticia(Integer.valueOf(photoId));
//            InputStream inputStream = new ByteArrayInputStream(noticia.getFoto());

            
            fileContent = new DefaultStreamedContent(getInputStream(), "image/jpeg");
            logger.info("Retrieved file content for image {}.", parsedId);
        }
        logger.trace("Exited method getFileContent.");
        return fileContent;
    }
    
    public void setFileContent(StreamedContent fileContent) {
        this.fileContent = fileContent;
    }
    
    public abstract InputStream getInputStream();
    
    public abstract String getPhotoId();
}
