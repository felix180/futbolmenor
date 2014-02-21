package com.spontecorp.littleligues.jsfcontroller.torneo;

import com.spontecorp.littleligues.jpacontroller.CategoriaJpaController;
import com.spontecorp.littleligues.jpacontroller.LigaJpaController;
import com.spontecorp.littleligues.jpacontroller.extentions.ClasificacionJpaControllerExt;
import com.spontecorp.littleligues.jpacontroller.extentions.FaseJpaControllerExt;
import com.spontecorp.littleligues.jpacontroller.extentions.GrupoJpaControllerExt;
import com.spontecorp.littleligues.jpacontroller.extentions.JornadaJpaControllerExt;
import com.spontecorp.littleligues.jpacontroller.extentions.TemporadaJpaControllerExt;
import com.spontecorp.littleligues.jsfcontroller.liga.util.JsfUtil;
import com.spontecorp.littleligues.model.liga.Categoria;
import com.spontecorp.littleligues.model.liga.Liga;
import com.spontecorp.littleligues.model.torneo.Clasificacion;
import com.spontecorp.littleligues.model.torneo.Fase;
import com.spontecorp.littleligues.model.torneo.Grupo;
import com.spontecorp.littleligues.model.torneo.Jornada;
import com.spontecorp.littleligues.model.torneo.Temporada;
import com.spontecorp.littleligues.utils.LittleLiguesUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

/**
 *
 * @author Andrés
 */
@ManagedBean(name = "clasificacionController")
@SessionScoped
public class ClasificacionController implements Serializable{

    private ClasificacionJpaControllerExt jpaController = null;
    private DataModel items = null;
    private Clasificacion current;
    
    private Liga liga;
    private Temporada temporada;
    private Fase fase;
    private Grupo grupo;
    private Jornada jornada;
    private Categoria categoria;
    private boolean clasificaTableShow = false;
    
    private boolean temporadaListDisabled = true;
    private boolean faseListDisabled = true;
    private boolean grupoListDisabled = true;
    private boolean jornadaListDisabled = true;
    private boolean categoriaListDisabled = true;
    
    private SelectItem[] categoriasAvalaibleSelectOne = null;
    private SelectItem[] jornadasAvalaibleSelectOne = null;
    
    
    public ClasificacionController() {
    }

    private ClasificacionJpaControllerExt getJpaController() {
        if (jpaController == null) {
            jpaController = new ClasificacionJpaControllerExt(LittleLiguesUtils.getEmf());
        }
        return jpaController;
    }

    public Clasificacion getCurrent() {
        return current;
    }

    public void setCurrent(Clasificacion current) {
        this.current = current;
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
        this.fase = fase;
        grupoListDisabled = false;
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
        categoriaListDisabled = false;
    }

    public Jornada getJornada() {
        return jornada;
    }

    public void setJornada(Jornada jornada) {
        this.jornada = jornada;
        recreateModel();
        clasificaTableShow = true;

    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
        jornadaListDisabled = false;
    }

    public boolean isTemporadaListDisabled() {
        return temporadaListDisabled;
    }

    public boolean isFaseListDisabled() {
        return faseListDisabled;
    }

    public boolean isGrupoListDisabled() {
        return grupoListDisabled;
    }

    public boolean isJornadaListDisabled() {
        return jornadaListDisabled;
    }

    public boolean isCategoriaListDisabled() {
        return categoriaListDisabled;
    }

    public boolean isClasificaTableShow() {
        return clasificaTableShow;
    }

    public DataModel getItems() {
        if(items == null){
            items = makeModelByCategoriaAndJornada();
        }
        return items;
    }
    
    private void recreateModel() {
        items = null;
    }

    private DataModel makeModelByCategoriaAndJornada() {
        List<Clasifica> clasificacion = new ArrayList<>();
        List<Object[]> calcula = getJpaController().findClasificacionByCategoria(categoria, jornada, grupo, liga);
        for (Object[] objs : calcula) {
            Clasifica clasifica = new Clasifica();
            clasifica.setEquipoName(objs[0].toString());
            clasifica.setJjugados(String.valueOf(objs[1]));
            clasifica.setJganados(String.valueOf(objs[2]));
            clasifica.setJempatados(String.valueOf(objs[3]));
            clasifica.setJperdidos(String.valueOf(objs[4]));
            clasifica.setGfavor(String.valueOf(objs[5]));
            clasifica.setGcontra(String.valueOf(objs[6]));
            clasifica.setDiferencia(String.valueOf(objs[7]));
            clasifica.setPuntos(String.valueOf(objs[8]));
            clasificacion.add(clasifica);
        }
        return new ListDataModel(clasificacion);
    }
    
    public void showTable(ActionEvent evt){
        recreateModel();
        clasificaTableShow = true;
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

    public SelectItem[] getGruposAvalaibleSelectOne() {
        GrupoJpaControllerExt grupoJpaController = new GrupoJpaControllerExt(LittleLiguesUtils.getEmf());
        SelectItem[] arreglo = null;
        if (!grupoListDisabled && (fase != null)) {
            List<Grupo> grupos = grupoJpaController.findGruposOnFase(fase);
            arreglo = JsfUtil.getSelectItems(grupos, true);
        }
        return arreglo;
    }

    public SelectItem[] getJornadasAvalaibleSelectOne() {
        if(jornadasAvalaibleSelectOne == null){
            jornadasAvalaibleSelectOne = makeJornadasList();
        }
        
        return jornadasAvalaibleSelectOne;
    }
    
    public SelectItem[] getCategoriasAvalaibleSelectOne() {
        if(categoriasAvalaibleSelectOne == null){
            categoriasAvalaibleSelectOne = makeCategoriaList();
        }
        return categoriasAvalaibleSelectOne;
    }
    
    private SelectItem[] makeCategoriaList(){
        CategoriaJpaController categoriaJpaController = new CategoriaJpaController(LittleLiguesUtils.getEmf());
        List<Categoria> categorias = categoriaJpaController.findCategoriaEntities();
        SelectItem[] arreglo = JsfUtil.getSelectItems(categorias, true);
        return arreglo;
    }
    
    private SelectItem[] makeJornadasList(){
        JornadaJpaControllerExt jornadaJpaController = new JornadaJpaControllerExt(LittleLiguesUtils.getEmf());
        SelectItem[] arreglo = null;
        if (!jornadaListDisabled && (grupo != null)) {
            List<Jornada> jornadas = jornadaJpaController.findJornadasOnGrupo(grupo);
            arreglo = JsfUtil.getSelectItems(jornadas, true);
        }
        return arreglo;
    }
    
    public class Clasifica{
        private String equipoName;
        private String jjugados;
        private String jganados;
        private String jempatados;
        private String jperdidos;
        private String gfavor;
        private String gcontra;
        private String diferencia;
        private String puntos;

        public String getEquipoName() {
            return equipoName;
        }

        public void setEquipoName(String equipoName) {
            this.equipoName = equipoName;
        }

        public String getJjugados() {
            return jjugados;
        }

        public void setJjugados(String jjugados) {
            this.jjugados = jjugados;
        }

        public String getJganados() {
            return jganados;
        }

        public void setJganados(String jganados) {
            this.jganados = jganados;
        }

        public String getJempatados() {
            return jempatados;
        }

        public void setJempatados(String jempatados) {
            this.jempatados = jempatados;
        }

        public String getJperdidos() {
            return jperdidos;
        }

        public void setJperdidos(String jperdidos) {
            this.jperdidos = jperdidos;
        }

        public String getGfavor() {
            return gfavor;
        }

        public void setGfavor(String gfavor) {
            this.gfavor = gfavor;
        }

        public String getGcontra() {
            return gcontra;
        }

        public void setGcontra(String gcontra) {
            this.gcontra = gcontra;
        }

        public String getDiferencia() {
            return diferencia;
        }

        public void setDiferencia(String diferencia) {
            this.diferencia = diferencia;
        }

        public String getPuntos() {
            return puntos;
        }

        public void setPuntos(String puntos) {
            this.puntos = puntos;
        }
        
    }
}
