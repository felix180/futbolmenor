package com.spontecorp.littleligues.jsfcontroller;



import com.spontecorp.littleligues.jpacontroller.extentions.CronicaJpaControllerExt;
import com.spontecorp.littleligues.model.Cronica;
import com.spontecorp.littleligues.model.Cronista;
import com.spontecorp.littleligues.utils.LittleLiguesUtils;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jgcastillo
 */
@ManagedBean
@RequestScoped
public class MainColumnistasBean {

    private CronicaJpaControllerExt jpaController = null;
    private DataModel<Cronica> items;
    private Cronica cronica;
    private Cronista cronista;

    private static final Logger logger = LoggerFactory.getLogger(MainColumnistasBean.class);
    
    /**
     * Creates a new instance of MainColumnistasBean
     */
    public MainColumnistasBean() {
    }

    public CronicaJpaControllerExt getJpaController() {
        if(jpaController == null){
            jpaController = new CronicaJpaControllerExt(LittleLiguesUtils.getEmf());
        }
        return jpaController;
    }

    public Cronica getCronica() {
        if(cronica == null){
            cronica = new Cronica();
        }
        return cronica;
    }
    
    public Cronista getCronista(){
        if(cronista == null){
            cronista = new Cronista();
        }
        return cronista;
    }
    
    public StreamedContent getStreamedImageByCronistaId() {
        FacesContext context = FacesContext.getCurrentInstance();

        if (context.getRenderResponse()) {
            return new DefaultStreamedContent();
        } else {
            String id = context.getExternalContext().getRequestParameterMap().get("id");
            InputStream inputStream = getInputStreamByCronista(id);
            return new DefaultStreamedContent(inputStream, "image/jpeg");
        }
    }
    
    public InputStream getInputStreamByCronista(String id) {
        Cronica cronicaObj = getJpaController().findCronica(Integer.valueOf(id));
        Cronista cronistaObj = cronicaObj.getCronistaId();
        byte[] foto = cronistaObj.getFoto();
        if (foto != null) {
            return new ByteArrayInputStream(foto);
        } else {
            return new ByteArrayInputStream(new byte[0]);
        }
    }
    
    public StreamedContent getStreamedImageByCronicaId() {
        FacesContext context = FacesContext.getCurrentInstance();

        if (context.getRenderResponse()) {
            return new DefaultStreamedContent();
        } else {
            String id = context.getExternalContext().getRequestParameterMap().get("id");
            InputStream inputStream = getInputStreamByCronica(id);
            return new DefaultStreamedContent(inputStream, "image/jpeg");
        }
    }
    
    public InputStream getInputStreamByCronica(String id) {
        Cronica cronicaObj = getJpaController().findCronica(Integer.valueOf(id));
        byte[] foto = cronicaObj.getFoto();
        if (foto != null) {
            return new ByteArrayInputStream(foto);
        } else {
            return new ByteArrayInputStream(new byte[0]);
        }
    }

    public DataModel<Cronica> getItems() {
        if(items == null){
            items = new ListDataModel<>(getJpaController().findCronicaEntitiesActives());
        }
        return items;
    }
    
    public String mostrarArticulo() {
        cronica = (Cronica) items.getRowData();
        LittleLiguesUtils.flashScope().put("mainColumnistasBean", this);
        return "articulo?faces-redirect=true";
    }
    
}
