package com.spontecorp.littleligues.jsfcontroller;

import com.spontecorp.littleligues.jpacontroller.VideoJpaController;
import com.spontecorp.littleligues.jpacontroller.extentions.NoticiaJpaControllerExt;
import com.spontecorp.littleligues.jsfcontroller.util.PaginationHelper;
import com.spontecorp.littleligues.model.Noticia;
import com.spontecorp.littleligues.model.Video;
import com.spontecorp.littleligues.utils.LittleLiguesUtils;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jgcastillo
 */
@ManagedBean(name = "mainPageBean")
@SessionScoped
public final class MainPageBean implements Serializable{

    private transient NoticiaJpaControllerExt jpaController = null;
    private transient DataModel<Noticia> items;
    private transient DataModel<Noticia> noticiasSlide;
    private transient DataModel<Noticia> noticiasHome;
    private transient DataModel<Video> videos;
    private Noticia entity;
    private Noticia mainNoticia = null;
    private UploadedFile uploadedFile;
    private StreamedContent fileContent;
    private static StreamedContent defaultFileContent;
    private PaginationHelper pagination;
    private static final Logger logger = LoggerFactory.getLogger(NoticiaBean.class);   
    private int cantidadNoticias = 3;

    /**
     * Creates a new instance of MainPageBean
     */
    static {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = contextClassLoader.getResourceAsStream("#{resource['images:Photo-0.png']}");
        defaultFileContent = new DefaultStreamedContent(inputStream, "image/png");
    }

    public MainPageBean() {
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

    /**
     * Paginador Noticias Seccion Noticias
     * @return 
     */
    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(cantidadNoticias) {
                @Override
                public int getItemsCount() {
                    return getJpaController().getOthersNoticiaCount();
                }
                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getJpaController().findOthersNoticiasEntities(getPageSize(), getPageFirstItem()));
                }
            };
        }
        return pagination;
    }
    
    public String next() {
        getPagination().nextPage();
        recreateModel();
        return "listaNoticias";
    }

    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "listaNoticias";
    }
    
    private void recreateModel(){
        items = null;
        noticiasSlide = null;
        noticiasHome = null; 
        videos = null;
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
    
    /**
     * Lista de Videos asociados a la Liga Seleccionada Ordenados por fecha
     * Orden DESC
     *
     * @return
     */
    public DataModel<Video> getVideos() {
        VideoJpaController videoJpaController = new VideoJpaController(LittleLiguesUtils.getEmf());
        if (videos == null) {
            List<Video> lista = videoJpaController.findVideoEntities(2,0);
            videos = new ListDataModel<>(lista);
        }
        return videos;
    }

//    public StreamedContent getStreamedImageById() {
//        FacesContext context = FacesContext.getCurrentInstance();
//
//        if (context.getRenderResponse()) {
//            return new DefaultStreamedContent();
//        } else {
//            String id = context.getExternalContext().getRequestParameterMap().get("id");
//            InputStream inputStream = getInputStream(id);
//            return new DefaultStreamedContent(inputStream, "image/jpeg");
//        }
//    }

//    public StreamedContent getFileContent() {
//        logger.trace("Entered method getFileContent.");
//        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
//        String photoId = externalContext.getRequestParameterMap().get("photo_id");
//
//        if (photoId == null || photoId.equals("")) {
//            fileContent = defaultFileContent;
//            logger.info("Id was null or empty. Retrieved default file content.");
//        } else {
//            int parsedId = Integer.parseInt(photoId);
//            if (parsedId < 0 || parsedId > 15) {  // revisar esto cuando esté en producción
//                fileContent = defaultFileContent;
//                logger.info("Invalid Id. Retrieved default file content.");
//            }
//
//            InputStream inputStream = getInputStream(photoId);
//
//            fileContent = new DefaultStreamedContent(inputStream, "image/jpeg");
//            logger.info("Retrieved file content for image {}.", parsedId);
//        }
//        logger.trace("Exited method getFileContent.");
//        return fileContent;
//    }

    public Noticia getMainNoticia() {
        // mainNoticia = getJpaController().findNoticiaByPrincipal(LittleLiguesUtils.PAG_PRINCIPAL);
        return mainNoticia;
    }

    /**
     * Listado Seccion Noticias (que no esten marcadas como principal y no esten asociadas a una Liga)
     * @return 
     */
    public DataModel<Noticia> getItems() {
        if (items == null) {
        //items = new ListDataModel<>(getJpaController().findNoticiaEntities());
            items = getPagination().createPageDataModel();
        }
        return items;
    }

    /**
     * Noticias Slide Home 4 Noticias ordenadas por fecha desc
     *
     * @return
     */
    public DataModel<Noticia> getNoticiasSlide() {
        recreateModel();
        if (noticiasSlide == null) {
            noticiasSlide = new ListDataModel<>(getJpaController().findNoticiaSlideEntities());
        }
        return noticiasSlide;
    }

    /**
     * Noticias Home ordenadas por fecha desc (listadas a partir de la 5)
     *
     * @return
     */
    public DataModel<Noticia> getNoticiasHome() {
        recreateModel();
        if (noticiasHome == null) {
            noticiasHome = new ListDataModel<>(getJpaController().findNoticiaHomeEntities());
        }
        return noticiasHome;
    }
    
    public String verMasSlide() {
        entity = (Noticia) noticiasSlide.getRowData();
        LittleLiguesUtils.flashScope().put("mainPageBean", this);
        return "noticias?faces-redirect=true";
    }

    public String verMasHome() {
        entity = (Noticia) noticiasHome.getRowData();
        LittleLiguesUtils.flashScope().put("mainPageBean", this);
        return "noticias?faces-redirect=true";
    }
    
    public String verMas() {
        entity = (Noticia) items.getRowData();
        LittleLiguesUtils.flashScope().put("mainPageBean", this);
        return "noticias?faces-redirect=true";
    }

    public String verMain() {
        entity = mainNoticia;
        LittleLiguesUtils.flashScope().put("mainPageBean", this);
        return "noticias?faces-redirect=true";
    }

//    public InputStream getInputStream(String id) {
//        Noticia noticia = getJpaController().findNoticia(Integer.valueOf(id));
//        byte[] foto = noticia.getFoto();
//        if (foto != null) {
//            return new ByteArrayInputStream(foto);
//        } else {
//            return new ByteArrayInputStream(new byte[0]);
//        }
//    }
}
