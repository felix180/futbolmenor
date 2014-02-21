/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.littleligues.jsfcontroller;

import com.spontecorp.littleligues.jpacontroller.CarruselHomeJpaController;
import com.spontecorp.littleligues.jpacontroller.extentions.*;
import com.spontecorp.littleligues.model.CarruselHome;
import com.spontecorp.littleligues.model.liga.Categoria;
import com.spontecorp.littleligues.model.liga.Equipo;
import com.spontecorp.littleligues.model.liga.Liga;
import com.spontecorp.littleligues.model.torneo.*;
import com.spontecorp.littleligues.utils.LittleLiguesUtils;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

/**
 *
 * @author zuleidyb
 */
@ManagedBean(name = "carruselHomeBean")
@SessionScoped
public final class CarruselHomeBean implements Serializable {

    private transient LigaJpaControllerExt jpaController = null;
    private transient DataModel<Liga> ligas;
    private List<Grupo> listaGrupos = null;
    private List<Llave> listaLlaves = null;
    private List<Partido> listaPartidos = null;
    List<Partido> listaPartidos1 = new ArrayList();
    List<Partido> listaPartidos2 = new ArrayList();
    private List<Equipo> listaEquipos = null;
    private List<Categoria> categorias = null;
    private List<Fase> listaFases = null;
    private Liga entity;
    private List<CarruselHome> carruselHome;
    private Temporada lastTemporadaLiga;
    private Fase fase = null;
    private Grupo grupo;
    private Llave llave;
    private Jornada jornada;
    private Categoria categoria;
    private int totalGrupos = 0;
    private int totalLlaves = 0;
    private int totalFases = 0;
    private int idFase = -1;
    private int idGrupo = -1;
    private int idLlave = -1;
    private int idCategoria = -1;
    private int idEquipo = -1;
    private String activeTab = null;
    private static Map<String, Integer> listFases;
    private static Map<Integer, Integer> listJornadas;
    private Integer selectedFase = null;
    private Integer selectedJornada = null;
    private int totalListaPartidos1 = 0;
    private int totalListaPartidos2 = 0;

    public CarruselHomeBean() {
        getCarruselHome();
        getListaPartidosParte1();
        getListaPartidosParte2();
    }

    private LigaJpaControllerExt getJpaController() {
        if (jpaController == null) {
            jpaController = new LigaJpaControllerExt(LittleLiguesUtils.getEmf());
        }
        return jpaController;
    }

    /**
     * Listado de Ligas
     *
     * @return
     */
    public DataModel<Liga> getLigas() {
        if (ligas == null) {
            ligas = new ListDataModel<>(getJpaController().findLigaEntities());
        }
        return ligas;
    }

    /**
     * Liga Actual
     *
     * @return
     */
    public Liga getEntity() {
        if (entity == null) {
            entity = new Liga();
        }
        return entity;
    }

    private void recreateModel() {
        entity = null;
    }

    /**
     *
     */
    public void recreateLigaModel() {
        lastTemporadaLiga = null;
        listaGrupos = null;
        listaLlaves = null;
        selectedFase = null;
        selectedJornada = null;
        listJornadas = null;
        listFases = null;
        listaPartidos = null;
        listaEquipos = null;
        fase = null;
        jornada = null;
        idFase = -1;
        idGrupo = -1;
        idLlave = -1;
        idCategoria = -1;
        idEquipo = -1;
        totalListaPartidos1 = 0;
        totalListaPartidos2 = 0;
        listaPartidos1 = new ArrayList();
        listaPartidos2 = new ArrayList();
        entity = null;
    }

    /**
     * Metodo para obtener la última temporada ordenada por fecha fin Orden DESC
     *
     * @return
     */
    public List<Categoria> getCarruselHome() {
        carruselHome = null;
        entity = null;
        lastTemporadaLiga = null;

        CarruselHomeJpaController carruselHomeJpaController = new CarruselHomeJpaController(LittleLiguesUtils.getEmf());
        LigaJpaControllerExt ligaJpaController = new LigaJpaControllerExt(LittleLiguesUtils.getEmf());

        if (carruselHome == null) {
            carruselHome = carruselHomeJpaController.findCarruselHomeEntities();
        }

        if (carruselHome.size() > 0) {
            entity = carruselHome.get(0).getLigaId();
        } else {
            recreateLigaModel();
        }

        FaseJpaControllerExt faseJpaController = new FaseJpaControllerExt(LittleLiguesUtils.getEmf());
        CategoriaJpaControllerExt categoriaJpaController = new CategoriaJpaControllerExt(LittleLiguesUtils.getEmf());

        List<Fase> fases = null;

        //Capturo la Categoria seleccionada
        FacesContext context = FacesContext.getCurrentInstance();
        int idSelectedCategoria = context.getExternalContext().getRequestParameterMap().get("idSelectedCategoria") != null ? Integer.parseInt(context.getExternalContext().getRequestParameterMap().get("idSelectedCategoria")) : -1;

        //Capturo el id del Grupo seleccionado
        int idSelectedGrupo = context.getExternalContext().getRequestParameterMap().get("idSelectedGrupo") != null ? Integer.parseInt(context.getExternalContext().getRequestParameterMap().get("idSelectedGrupo")) : -1;

        //Capturo el id de la Llave seleccionada
        int idSelectedLlave = context.getExternalContext().getRequestParameterMap().get("idSelectedLlave") != null ? Integer.parseInt(context.getExternalContext().getRequestParameterMap().get("idSelectedLlave")) : -1;

        if (carruselHome.size() < 0) {
            idSelectedGrupo = -1;
        }

        if (idSelectedGrupo > 0) {
            selectedFase = null;
            selectedJornada = null;
            listJornadas = null;
            listFases = null;
            idGrupo = idSelectedGrupo;
        }

        if (idSelectedLlave > 0) {
            listFases = null;
            idLlave = idSelectedLlave;
        }

        //Verifico si el Grupo pertenece a la Fase q pertenece a la Temporada de la Liga correspondiente, 
        //sino idSelectedGrupo = -1 

//        CarruselHomeJpaController carruselJpaController = new CarruselHomeJpaController(LittleLiguesUtils.getEmf());
//        boolean existGrupo = carruselJpaController.findGrupo(idSelectedGrupo, entity);
//        
//        if(!existGrupo){
//            idSelectedGrupo = -1;
//        }

        //Lista de Categorias
        categorias = categoriaJpaController.findListCategoria();

        //Asigno Categoria por defecto
        if (idSelectedCategoria == -1 && idCategoria == -1) {
            //Si la Liga es igual a LIEX Femenino
            //La Categoría por defecto es Infantil A
            if (entity != null) {
                if (entity.getId() == 9) {
                    categoria = categorias.get(4);
                } else {
                    categoria = categorias.get(0);
                }
            } else {
                categoria = categorias.get(0);
            }
        } else {
            categoria = categoriaJpaController.findCategoria(idSelectedCategoria);
        }

        if (idSelectedCategoria == -1 && idCategoria != -1) {
            categoria = categoriaJpaController.findCategoria(idCategoria);
        }

        idCategoria = categoria.getId();
        idSelectedCategoria = idCategoria;

        //Busco la ultima Temporada de la Liga
        if (lastTemporadaLiga == null && entity != null) {
            lastTemporadaLiga = getJpaController().findLastTemporadaLiga(entity);
        }

        if (lastTemporadaLiga != null) {
            //Listo Fases de la ultima Temporada
            fases = faseJpaController.findFasesOnTemporada(lastTemporadaLiga);

            if (fases.size() > 0) {
                //Lista de Fases a mostrar en el JSF
                listFases = new LinkedHashMap<String, Integer>();
                for (int i = 0; i < fases.size(); i++) {
                    listFases.put(fases.get(i).getNombre(), fases.get(i).getId());
                }

                //Asigno Fase por defecto
                if (selectedFase == null && fases.size() > 0) {
                    fase = fases.get(0);
                }

                GrupoJpaControllerExt grupoJpaController = new GrupoJpaControllerExt(LittleLiguesUtils.getEmf());
                LlaveJpaControllerExt llaveJpaController = new LlaveJpaControllerExt(LittleLiguesUtils.getEmf());
                JornadaJpaControllerExt jornadaJpaController = new JornadaJpaControllerExt(LittleLiguesUtils.getEmf());
                List<Jornada> jornadas = null;

                //Lista de Grupos de la Fase seleccionada
                if (fase != null && listaGrupos == null) {
                    listaGrupos = grupoJpaController.findGruposOnFase(fase);
                }

                //Lista de LLaves de la Fase seleccionada
                if (fase != null && listaLlaves == null) {
                    listaLlaves = llaveJpaController.findLlavesOnFase(fase);
                }

                if (listaGrupos != null) {
                    if (listaGrupos.size() > 0) {
                        totalGrupos = listaGrupos.size();
                        //Asigno Grupo por defecto
                        if (idSelectedGrupo == -1 && idGrupo == -1) {
                            grupo = listaGrupos.get(0);
                        } else {
                            idSelectedGrupo = idGrupo;
                            grupo = grupoJpaController.findGrupo(idSelectedGrupo);
                        }

                        idGrupo = grupo.getId();
                        idSelectedGrupo = idGrupo;

                        //Listo las Jornadas para el Grupo seleccionado
                        jornadas = jornadaJpaController.findJornadaEntitiesOnGrupo(grupo);

                        //Lista de Jornadas a mostrar en el JSF
                        listJornadas = new LinkedHashMap<Integer, Integer>();
                        for (int i = 0; i < jornadas.size(); i++) {
                            listJornadas.put(jornadas.get(i).getNumero(), jornadas.get(i).getId());
                        }

                        //Asigno la Jornada por defecto
                        if (selectedJornada == null && jornadas.size() > 0) {
                            
                            jornada = jornadas.get(0);
                        }

                    } else {
                        selectedJornada = null;
                        jornada = null;
                    }
                }

                if (listaLlaves != null) {
                    if (listaLlaves.size() > 0) {
                        totalLlaves = listaLlaves.size();
                        //Asigno Llave por Defecto
                        if (idSelectedLlave == -1 && idLlave == -1) {
                            llave = listaLlaves.get(0);
                        } else {
                            idSelectedLlave = idLlave;
                            llave = llaveJpaController.findLlave(idSelectedLlave);
                        }
                        idLlave = llave.getId();
                        idSelectedLlave = idLlave;
                    }
                } else {
                    llave = null;
                }

            } else {
                recreateLigaModel();
            }

        }
        return categorias;
    }

    /**
     * Recargo la pagina posicionándola en Carrusel
     *
     * @throws IOException
     */
    public void reloadIndexLigas() throws IOException {
        getCarruselHome();
        FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml#carrusel");
    }

    /**
     * Listado de Fases de la Temporada seleccionada
     *
     * @param x
     */
    public void faseChanged(ValueChangeEvent x) throws IOException {
        FaseJpaControllerExt faseJpaController = new FaseJpaControllerExt(LittleLiguesUtils.getEmf());
        //assign new value to selectedFase
        selectedFase = (Integer) x.getNewValue();
        fase = faseJpaController.findFase(selectedFase);
        listaGrupos = null;
        listaLlaves = null;
        selectedJornada = null;
        listJornadas = null;
        reloadIndexLigas();
    }

    /**
     * Listado de Jornadas del Grupo seleccionado
     *
     * @param x
     */
    public void jornadaChanged(ValueChangeEvent x) throws IOException {
        JornadaJpaControllerExt jornadaJpaController = new JornadaJpaControllerExt(LittleLiguesUtils.getEmf());
        //assign new value to selectedJornada
        selectedJornada = (Integer) x.getNewValue();
        jornada = jornadaJpaController.findJornada(selectedJornada);
        idGrupo = grupo.getId();
        reloadIndexLigas();
    }

    /**
     * Lista de Grupos de la Fase Seleccionada
     *
     * @return
     */
    public List<Grupo> getListaGrupos() {
        return listaGrupos;
    }

    /**
     * Lista de Llaves de la Fase Seleccionada
     *
     * @return
     */
    public List<Llave> getListaLlaves() {
        return listaLlaves;
    }

    /**
     * Lista de Partidos Parte 1 para la Categoria y Jornada seleccionada
     *
     * @return
     */
    public List<Partido> getListaPartidosParte1() {
        PartidoJpaControllerExt partidoJpaController = new PartidoJpaControllerExt(LittleLiguesUtils.getEmf());
        listaPartidos = null;
        listaPartidos1 = new ArrayList();

        if (categoria != null) {
            if (totalGrupos > 0 && jornada != null) {
                listaPartidos = partidoJpaController.findPartidoEntitiesOnCategoriaWithinJornada(categoria, jornada);
            } else if (totalLlaves > 0 && llave != null) {
                listaPartidos = partidoJpaController.findPartidoEntitiesOnCategoriaWithinLlave(categoria, llave);
            }
        }

        //Lógica para obtener Lista Parte 1 y completar espacios vacios en el carrusel de Resultados
        //5 es el número de Resultados por Tablas
        if (listaPartidos != null) {
            if (listaPartidos.size() < 6) {
                for (int i = 0; i < listaPartidos.size(); i++) {
                    listaPartidos1.add(listaPartidos.get(i));
                }
                totalListaPartidos1 = listaPartidos1.size();
            } else {
                if (listaPartidos.size() > 5) {
                    for (int j = 0; j < 5; j++) {
                        listaPartidos1.add(listaPartidos.get(j));
                    }
                    totalListaPartidos1 = listaPartidos1.size();
                }
            }

        } else {
            totalListaPartidos1 = 0;
        }
        return listaPartidos1;
    }

    /**
     * Lista de Partidos Parte 2 para la Categoria y Jornada seleccionada
     *
     * @return
     */
    public List<Partido> getListaPartidosParte2() {
        PartidoJpaControllerExt partidoJpaController = new PartidoJpaControllerExt(LittleLiguesUtils.getEmf());
        listaPartidos = null;
        listaPartidos2 = new ArrayList();

        if (categoria != null) {
            if (totalGrupos > 0 && jornada != null) {
                listaPartidos = partidoJpaController.findPartidoEntitiesOnCategoriaWithinJornada(categoria, jornada);
            } else if (totalLlaves > 0 && llave != null) {
                listaPartidos = partidoJpaController.findPartidoEntitiesOnCategoriaWithinLlave(categoria, llave);
            }
        }

        //Lógica para obtener Lista Parte 2 y completar espacios vacios en el carrusel de Resultados
        //5 es el número de Resultados por Tablas
        if (listaPartidos != null) {
            if (listaPartidos.size() < 6) {
                totalListaPartidos2 = 0;
            } else {
                if (listaPartidos.size() > 5) {
                    for (int j = 0; j < listaPartidos.size(); j++) {
                        if (j > 4) {
                            listaPartidos2.add(listaPartidos.get(j));
                        }
                    }
                    totalListaPartidos2 = listaPartidos2.size();
                }
            }

        } else {
            totalListaPartidos2 = 0;
        }
        return listaPartidos2;
    }

    public Integer getSelectedFase() {
        return selectedFase;
    }

    public void setSelectedFase(Integer selectedFase) {
        this.selectedFase = selectedFase;
    }

    public Integer getSelectedJornada() {
        return selectedJornada;
    }

    public void setSelectedJornada(Integer selectedJornada) {
        this.selectedJornada = selectedJornada;
    }

    public Fase getFase() {
        return fase;
    }

    public void setFase(Fase fase) {
        this.fase = fase;
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    public Llave getLlave() {
        return llave;
    }

    public void setLlave(Llave llave) {
        this.llave = llave;
    }

    public Jornada getJornada() {
        return jornada;
    }

    public void setJornada(Jornada jornada) {
        this.jornada = jornada;
    }

    public int getTotalGrupos() {
        return totalGrupos;
    }

    public void setTotalGrupos(int totalGrupos) {
        this.totalGrupos = totalGrupos;
    }

    public int getTotalLlaves() {
        return totalLlaves;
    }

    public void setTotalLlaves(int totalLlaves) {
        this.totalLlaves = totalLlaves;
    }

    public int getTotalFases() {
        return totalFases;
    }

    public void setTotalFases(int totalFases) {
        this.totalFases = totalFases;
    }

    public int getIdFase() {
        return idFase;
    }

    public void setIdFase(int idFase) {
        this.idFase = idFase;
    }

    public int getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(int idGrupo) {
        this.idGrupo = idGrupo;
    }

    public int getIdLlave() {
        return idLlave;
    }

    public void setIdLlave(int idLlave) {
        this.idLlave = idLlave;
    }

    public List<Fase> getListaFases() {
        return listaFases;
    }

    public Map<String, Integer> getFasesInMap() {
        return listFases;
    }

    public Map<Integer, Integer> getJornadasInMap() {
        return listJornadas;
    }

    public List<Categoria> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<Categoria> categorias) {
        this.categorias = categorias;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public int getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(int idEquipo) {
        this.idEquipo = idEquipo;
    }

    public String getActiveTab() {
        return activeTab;
    }

    public void setActiveTab(String activeTab) {
        this.activeTab = activeTab;
    }

    public List<Partido> getListaPartidos1() {
        return listaPartidos1;
    }

    public void setListaPartidos1(List<Partido> listaPartidos1) {
        this.listaPartidos1 = listaPartidos1;
    }

    public List<Partido> getListaPartidos2() {
        return listaPartidos2;
    }

    public void setListaPartidos2(List<Partido> listaPartidos2) {
        this.listaPartidos2 = listaPartidos2;
    }

    public int getTotalListaPartidos1() {
        return totalListaPartidos1;
    }

    public void setTotalListaPartidos1(int totalListaPartidos1) {
        this.totalListaPartidos1 = totalListaPartidos1;
    }

    public int getTotalListaPartidos2() {
        return totalListaPartidos2;
    }

    public void setTotalListaPartidos2(int totalListaPartidos2) {
        this.totalListaPartidos2 = totalListaPartidos2;
    }
}
