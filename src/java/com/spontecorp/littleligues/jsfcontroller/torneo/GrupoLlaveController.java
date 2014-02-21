package com.spontecorp.littleligues.jsfcontroller.torneo;

import com.spontecorp.littleligues.jpacontroller.LigaJpaController;
import com.spontecorp.littleligues.jpacontroller.extentions.FaseJpaControllerExt;
import com.spontecorp.littleligues.jpacontroller.extentions.GrupoJpaControllerExt;
import com.spontecorp.littleligues.jpacontroller.extentions.LlaveJpaControllerExt;
import com.spontecorp.littleligues.jpacontroller.extentions.TemporadaJpaControllerExt;
import com.spontecorp.littleligues.jsfcontroller.liga.util.JsfUtil;
import com.spontecorp.littleligues.jsfcontroller.liga.util.PaginationHelper;
import com.spontecorp.littleligues.model.liga.Liga;
import com.spontecorp.littleligues.model.torneo.Fase;
import com.spontecorp.littleligues.model.torneo.Grupo;
import com.spontecorp.littleligues.model.torneo.Llave;
import com.spontecorp.littleligues.model.torneo.Temporada;
import com.spontecorp.littleligues.utils.LittleLiguesUtils;
import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

/**
 *
 * @author Andrés
 */
@ManagedBean(name = "grupoLlaveController")
@SessionScoped
public class GrupoLlaveController implements Serializable{

    private Grupo grupo;
    private Llave llave;
    private DataModel grupoItems = null;
    private DataModel llaveItems = null;
    private GrupoJpaControllerExt jpaGrupoController = null;
    private LlaveJpaControllerExt jpaLlaveController = null;
    private PaginationHelper paginationGrupo;
    private PaginationHelper paginationLlave;
    private int selectedItemIndex;
    private Liga liga;
    private Temporada temporada;
    private Fase fase;
    private boolean temporadaListDisabled = true;
    private boolean faseListDisabled = true;
    private boolean tableDisabled = true;
    private String grupoLlaveSelection = "1";
    private boolean disableGrupoSelection = false;
    private String nombreGrupo;
    private String nombreLlave;

    public GrupoLlaveController() {
    }

    public Grupo getGrupoSelected() {
        if (grupo == null) {
            grupo = new Grupo();
            fase = new Fase();
            temporada = new Temporada();
            liga = new Liga();

            temporada.setLigaId(liga);
            fase.setTemporadaId(temporada);
            grupo.setFaseId(fase);

            selectedItemIndex = -1;
        }
        return grupo;
    }

    public Llave getLlaveSelected() {
        if (llave == null) {
            llave = new Llave();
            fase = new Fase();
            temporada = new Temporada();
            liga = new Liga();

            temporada.setLigaId(liga);
            fase.setTemporadaId(temporada);
            llave.setFaseId(fase);

            selectedItemIndex = -1;
        }
        return llave;
    }

    private GrupoJpaControllerExt getGrupoJpaController() {
        if (jpaGrupoController == null) {
            jpaGrupoController = new GrupoJpaControllerExt(LittleLiguesUtils.getEmf());
        }
        return jpaGrupoController;
    }

    private LlaveJpaControllerExt getLlaveJpaController() {
        if (jpaLlaveController == null) {
            jpaLlaveController = new LlaveJpaControllerExt(LittleLiguesUtils.getEmf());
        }
        return jpaLlaveController;
    }

    public PaginationHelper getGrupoPagination() {
        if (paginationGrupo == null) {
            paginationGrupo = new PaginationHelper(10) {
                @Override
                public int getItemsCount() {
                    return getGrupoJpaController().getGrupoCount();
                }

                @Override
                public DataModel createPageDataModel() {
                    if (fase == null) {
                        return new ListDataModel(getGrupoJpaController().findGrupoEntities(getPageSize(), getPageFirstItem()));
                    } else {
                        return new ListDataModel(getGrupoJpaController().findGrupoEntitiesOnFase(fase, getPageSize(), getPageFirstItem()));
                    }
                }
            };
        }
        return paginationGrupo;
    }

    public PaginationHelper getLlavePagination() {
        if (paginationLlave == null) {
            paginationLlave = new PaginationHelper(10) {
                @Override
                public int getItemsCount() {
                    return getLlaveJpaController().getLlaveCount();
                }

                @Override
                public DataModel createPageDataModel() {
                    if (fase == null) {
                        return new ListDataModel(getLlaveJpaController().findLlaveEntities(getPageSize(), getPageFirstItem()));
                    } else {
                        return new ListDataModel(getLlaveJpaController().findLlaveEntitiesOnFase(fase, getPageSize(), getPageFirstItem()));
                    }
                }
            };
        }
        return paginationLlave;
    }

    public String prepareList() {
        recreateGrupoModel();
        recreateLlaveModel();
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "List?faces-redirect=true";
    }

    public String prepareCreate() {
        selectedItemIndex = -1;
        return "Create";
    }

    public String prepareGrupoDestroy() {
        grupo = (Grupo) getGrupoItems().getRowData();
        selectedItemIndex = paginationGrupo.getPageFirstItem() + getGrupoItems().getRowIndex();
        return "DeleteGrupo";
    }

    public String prepareLlaveDestroy() {
        llave = (Llave) getLlaveItems().getRowData();
        selectedItemIndex = paginationLlave.getPageFirstItem() + getLlaveItems().getRowIndex();
        return "DeleteLlave";
    }
    
    public String destroyGrupo() {
        performGrupoDestroy();
        recreateGrupoPagination();
        recreateGrupoModel();
        return "List";
    }

    public String destroyLlave() {
        performLlaveDestroy();
        recreateLlavePagination();
        recreateLlaveModel();
        return "List";
    }
    
    private void recreateGrupoModel() {
        grupoItems = null;
    }

    private void recreateLlaveModel() {
        llaveItems = null;
    }

    private void recreateGrupoPagination() {
        paginationGrupo = null;
    }

    private void recreateLlavePagination() {
        paginationLlave = null;
    }

    public String grupoNext() {
        getGrupoPagination().nextPage();
        recreateGrupoModel();
        return "List";
    }

    public String grupoPrevious() {
        getGrupoPagination().previousPage();
        recreateGrupoModel();
        return "List";
    }

    public String llaveNext() {
        getLlavePagination().nextPage();
        recreateLlaveModel();
        return "List";
    }

    public String llavePrevious() {
        getLlavePagination().previousPage();
        recreateLlaveModel();
        return "List";
    }

    public String create() {
        try {
            if (grupoLlaveSelection.equals("1")) {
                grupo = new Grupo();
                
                grupo.setFaseId(fase);
                grupo.setNombre(nombreGrupo);
                
                getGrupoJpaController().create(grupo);
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("GrupoCreated"));
            } else {
                llave = new Llave();
                
                llave.setFaseId(fase);
                llave.setNombre(nombreLlave);
                
                getLlaveJpaController().create(llave);
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("GrupoLlaveCreated"));
            }
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }
    
    private void performGrupoDestroy() {
        try {
            getGrupoJpaController().destroy(grupo.getId());
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("GrupoDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    private void performLlaveDestroy() {
        try {
            getLlaveJpaController().destroy(llave.getId());
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("GrupoLlaveDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }
    
    public DataModel getGrupoItems() {
        if (grupoItems == null) {
            grupoItems = getGrupoPagination().createPageDataModel();
        }
        return grupoItems;
    }

    public DataModel getLlaveItems() {
        if (llaveItems == null) {
            llaveItems = getLlavePagination().createPageDataModel();
        }
        return llaveItems;
    }

    public Liga getLiga() {
        return liga;
    }

    public void setLiga(Liga liga) {
        this.liga = liga;
        temporadaListDisabled = false;
    }

    public Temporada getTemporada() {
        return temporada;
    }

    public void setTemporada(Temporada temporada) {
        this.temporada = temporada;
        faseListDisabled = false;
    }

    public Fase getFase() {
        return fase;
    }

    public void setFase(Fase fase) {
        tableDisabled = false;
        this.fase = fase;
        recreateGrupoModel();
        recreateLlaveModel();
    }

    public String getNombreGrupo() {
        return nombreGrupo;
    }

    public void setNombreGrupo(String nombreGrupo) {
        this.nombreGrupo = nombreGrupo;
    }

    public String getNombreLlave() {
        return nombreLlave;
    }

    public void setNombreLlave(String nombreLlave) {
        this.nombreLlave = nombreLlave;
    }

    public boolean isTemporadaListDisabled() {
        return temporadaListDisabled;
    }

    public boolean isFaseListDisabled() {
        return faseListDisabled;
    }

    public boolean isTableDisabled() {
        return tableDisabled;
    }

    public String getGrupoLlaveSelection() {
        return grupoLlaveSelection;
    }

    public void setGrupoLlaveSelection(String grupoLlaveSelection) {
        this.grupoLlaveSelection = grupoLlaveSelection;
        if (grupoLlaveSelection.equalsIgnoreCase("1")) {
            disableGrupoSelection = false;
        } else {
            disableGrupoSelection = true;
        }
    }

    public boolean isDisableGrupoSelection() {
        return disableGrupoSelection;
    }

    public SelectItem[] getLigasAvalaibleSelectOne() {
        LigaJpaController ligaJpaController = new LigaJpaController(LittleLiguesUtils.getEmf());
        List<Liga> ligas = ligaJpaController.findLigaEntities();
        return JsfUtil.getSelectItems(ligas, true);
    }

    public SelectItem[] getTemporadasAvalaibleSelectOne() {
        TemporadaJpaControllerExt temporadaJpaController = new TemporadaJpaControllerExt(LittleLiguesUtils.getEmf());
        SelectItem[] arreglo = null;
        if (!temporadaListDisabled && (liga != null)) {
            List<Temporada> temporadas = temporadaJpaController.findTemporadasOnLiga(liga);
            arreglo = JsfUtil.getSelectItems(temporadas, true);
        }
        return arreglo;
    }

    public SelectItem[] getFasesAvalaibleSelectOne() {
        FaseJpaControllerExt faseJpaController = new FaseJpaControllerExt(LittleLiguesUtils.getEmf());
        SelectItem[] arreglo = null;
        if (!faseListDisabled && (temporada != null)) {
            List<Fase> fases = faseJpaController.findFasesOnTemporada(temporada);
            arreglo = JsfUtil.getSelectItems(fases, true);
        }
        return arreglo;
    }
}
