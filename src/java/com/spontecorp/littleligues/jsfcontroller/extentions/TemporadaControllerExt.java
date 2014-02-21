package com.spontecorp.littleligues.jsfcontroller.extentions;

import com.spontecorp.littleligues.jpacontroller.CanchaJpaController;
import com.spontecorp.littleligues.jpacontroller.EquipoJpaController;
import com.spontecorp.littleligues.jpacontroller.extentions.CategoriaJpaControllerExt;
import com.spontecorp.littleligues.jsfcontroller.torneo.TemporadaController;
import com.spontecorp.littleligues.model.liga.Cancha;
import com.spontecorp.littleligues.model.liga.Categoria;
import com.spontecorp.littleligues.model.liga.Equipo;
import com.spontecorp.littleligues.model.torneo.Fase;
import com.spontecorp.littleligues.model.torneo.Grupo;
import com.spontecorp.littleligues.model.torneo.Jornada;
import com.spontecorp.littleligues.model.torneo.Llave;
import com.spontecorp.littleligues.model.torneo.Partido;
import com.spontecorp.littleligues.utils.LittleLiguesUtils;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jgcastillo
 */
@ManagedBean(name = "temporadaControllerExt")
@SessionScoped
public class TemporadaControllerExt extends TemporadaController {

    private int cantFases = 0;
    private String[] fasesStrings;
    private String faseName;
    private String[] tieneGrupos;
    
    //Objetos originales
    private Fase fase;
    private Jornada jornada;
    private Partido partido;
    
    //Manejo de las fases en la vista
    private List<String> fasesEnCombo;
    private String faseSelected = "-- Seleccione Fase --";
    private String faseConLlaveSelected = "-- Seleccione Fase --";
    private String faseSelectedFinal;
    private boolean enableCantFasesButton = false;
    
    //Manejo de los grupos en la vista
    private List<Grupo> grupos;
    private Map<String, Grupo> gruposConJornadas;
    private List<String> gruposEnFase;
    private String[] nombreGrupo;
    private Integer[] cantJornadas;
    private String grupoSelected;
    private boolean grupoListDiseable = true;
    private int gruposTotales = -1;
    private boolean confGrupos;
    
    //Manejo de las Llaves en la vista
    private List<Llave> llaves;
    private List<String> llavesEnFase;
    private String[] nombreLlave;
    private Integer[] cantPartidosXLlave;
    private int llavesTotales = -1;
    private boolean confLlaves;
    
    //Manejo de las jornadas en la vista
    private List<Jornada> jornadasXGrupo;
    private Map<Integer, Jornada> jornadaConPartidos;
    private List<Integer> jornadasEnGrupo;
    private String jornadaSelected;
    private boolean jornadaListDiseable = true;
    
    //Manejo de los partidos
    private int cantPartidos;
    private int cantPartidosEnLLave;
    private Partido[] partidos;
    private List<Partido> partidosXLlave;
    private String[] equipo1Selected;
    private String[] equipo2Selected;
    private Date[] fechaPartido;
    private String[] canchaSelected;
    private String jornadaConfigurada;
    private String[] partidosXjornada;
    
    //Manejo de las Categorias en la vista
    private List<Categoria> categorias;
    private String categoriaSelected;
    private Integer[] cantGruposLlaves;
    private boolean fasesReady;
    private boolean noHayGrupos = false;
    
    private List<Fase> fases;
    
    //Cache de las entidades
    private Map<String, Fase> fasesConGrupos = new HashMap<>();
    private Map<String, Fase> fasesConLlaves = new HashMap<>();
    private Map<String, Fase> mapFases = new HashMap<>();
    private Map<String, Grupo> mapGrupos = new HashMap<>();
    private Map<String, Llave> mapLlaves = new HashMap<>();
    private Map<String, Equipo> mapEquipos = new HashMap<>();
    private Map<String, Cancha> mapCanchas = new HashMap<>();
    private Map<String, Jornada> mapJornadas = new HashMap<>();
    
    //Data From DB
    private CategoriaJpaControllerExt categoriaJpaController = null;
    private EquipoJpaController equipoJpaController = null;
    private CanchaJpaController canchaJpaController = null;
    private static final Logger logger = LoggerFactory.getLogger(TemporadaControllerExt.class);

    public TemporadaControllerExt() {
    }

    @PostConstruct
    private void init() {
        initVariables();
    }

    private CategoriaJpaControllerExt getCategoriaJpaController() {
        if (categoriaJpaController == null) {
            categoriaJpaController = new CategoriaJpaControllerExt(LittleLiguesUtils.getEmf());
        }
        return categoriaJpaController;
    }

    private EquipoJpaController getEquipoJpaController() {
        if (equipoJpaController == null) {
            equipoJpaController = new EquipoJpaController(LittleLiguesUtils.getEmf());
        }
        return equipoJpaController;
    }

    private CanchaJpaController getCanchaJpaController() {
        if (canchaJpaController == null) {
            canchaJpaController = new CanchaJpaController(LittleLiguesUtils.getEmf());
        }
        return canchaJpaController;
    }

    /*
     * Inicio de los getter y setter
     */
    public int getCantFases() {
        return cantFases;
    }

    public void setCantFases(int cantFases) {
        this.cantFases = cantFases;
        System.out.println("Las fases son: " + this.cantFases);
    }

    public String[] getFasesStrings() {
        return fasesStrings;
    }

    public void setFasesStrings(String[] fasesStrings) {
        this.fasesStrings = fasesStrings;
    }

    public String[] getTieneGrupos() {
        return tieneGrupos;
    }

    public void setTieneGrupos(String[] tieneGrupos) {
        this.tieneGrupos = tieneGrupos;
    }

    public String getFaseName() {
        return faseName;
    }

    public void setFaseName(String faseName) {
        this.faseName = faseName;
    }

    public Integer[] getCantGruposLlaves() {
        return cantGruposLlaves;
    }

    public void setCantGruposLlaves(Integer[] cantGruposLlaves) {
        this.cantGruposLlaves = cantGruposLlaves;
    }

    public boolean isFasesReady() {
        return fasesReady;
    }
    
    public boolean isConfGrupos(){
        return confGrupos;
    }
    
    public boolean isConfLlaves(){
        return confLlaves;
    }

    public List<String> getFasesEnCombo() {
        return fasesEnCombo;
    }

    public void setFasesEnCombo(List<String> fasesEnCombo) {
        this.fasesEnCombo = fasesEnCombo;
    }

    public String getFaseSelected() {
        return faseSelected;
    }

    public void setFaseSelected(String faseSelected) {
        generateGroups(faseSelected);
        this.faseSelected = faseSelected;
    }
    
    public String getFaseConLlaveSelected() {
        return faseConLlaveSelected;
    }

    public void setFaseConLlaveSelected(String faseConLlaveSelected) {
        generateLlaves(faseConLlaveSelected);
        this.faseConLlaveSelected = faseConLlaveSelected;
    }

    public Map<String, Grupo> getGruposConJornadas() {
        return gruposConJornadas;
    }

    public List<String> getGruposEnFase() {
        return gruposEnFase;
    }

    public List<String> getLlavesEnFase() {
        return llavesEnFase;
    }

    public String[] getNombreGrupo() {
        return nombreGrupo;
    }

    public void setNombreGrupo(String[] nombreGrupo) {
        this.nombreGrupo = nombreGrupo;
    }

    public Integer[] getCantJornadas() {
        return cantJornadas;
    }

    public void setCantJornadas(Integer[] cantJornadas) {
        this.cantJornadas = cantJornadas;
    }

    public String[] getNombreLlave() {
        return nombreLlave;
    }

    public void setNombreLlave(String[] nombreLlave) {
        this.nombreLlave = nombreLlave;
    }

    public Integer[] getCantPartidosEnLlave() {
        return cantPartidosXLlave;
    }

    public void setCantPartidosEnLlave(Integer[] cantPartidosEnLlave) {
        this.cantPartidosXLlave = cantPartidosEnLlave;
    }

    public boolean isNoHayGrupos() {
        return noHayGrupos;
    }

    public boolean isEnableCantFasesButton() {
        return enableCantFasesButton;
    }

    public void setEnableCantFasesButton(boolean enableCantFasesButton) {
        this.enableCantFasesButton = enableCantFasesButton;
    }

    // Los getter y setters de la configuración final
    public String getFaseSelectedFinal() {
        return faseSelectedFinal;
    }

    public void setFaseSelectedFinal(String faseSelectedFinal) {
        this.faseSelectedFinal = faseSelectedFinal;
        this.grupoListDiseable = false;
    }

    public String getGrupoSelected() {
        return grupoSelected;
    }

    public void setGrupoSelected(String grupoSelected) {
        this.grupoSelected = grupoSelected;
        this.jornadaListDiseable = false;
    }

    public boolean isGrupoListDisable() {
        return grupoListDiseable;
    }

    public String getJornadaSelected() {
        return jornadaSelected;
    }

    public void setJornadaSelected(String jornadaSelected) {
        this.jornadaSelected = jornadaSelected;
    }

    public boolean isJornadaListDiseable() {
        return jornadaListDiseable;
    }

    public String getCategoriaSelected() {
        return categoriaSelected;
    }

    public void setCategoriaSelected(String categoriaSelected) {
        this.categoriaSelected = categoriaSelected;
    }

    public int getCantPartidos() {
        return cantPartidos;
    }

    public void setCantPartidos(int cantPartidos) {
        this.cantPartidos = cantPartidos;
    }

    public int getCantPartidosEnLLave() {
        return cantPartidosEnLLave;
    }

    public void setCantPartidosEnLLave(int cantPartidosEnLLave) {
        this.cantPartidosEnLLave = cantPartidosEnLLave;
    }

    public String[] getEquipo1Selected() {
        return equipo1Selected;
    }

    public void setEquipo1Selected(String[] equipo1Selected) {
        this.equipo1Selected = equipo1Selected;
    }

    public String[] getEquipo2Selected() {
        return equipo2Selected;
    }

    public void setEquipo2Selected(String[] equipo2Selected) {
        this.equipo2Selected = equipo2Selected;
    }

    public Date[] getFechaPartido() {
        return fechaPartido;
    }

    public void setFechaPartido(Date[] fechaPartido) {
        this.fechaPartido = fechaPartido;
    }

    public String[] getCanchaSelected() {
        return canchaSelected;
    }

    public void setCanchaSelected(String[] canchaSelected) {
        this.canchaSelected = canchaSelected;
    }

    public String getJornadaConfigurada() {
        this.jornadaConfigurada = faseSelectedFinal + "." + grupoSelected
                + "-" + jornadaSelected + "(" + categoriaSelected + ")";
        return jornadaConfigurada;
    }

    public Partido[] getPartidos() {
        return partidos;
    }

    // Parte final de la configuración
    public List<SelectItem> getFasesConGrupos() {
        List<SelectItem> fasesList = new ArrayList<>();
        fasesList.add(new SelectItem("-- Seleccione Fase --"));
        for (Map.Entry<String, Fase> item : fasesConGrupos.entrySet()) {
            fasesList.add(new SelectItem(item.getKey()));
        }
        return fasesList;
    }
    
    public List<SelectItem> getFasesConLlaves(){
        List<SelectItem> fasesList = new ArrayList<>();
        fasesList.add(new SelectItem("-- Seleccione Fase --"));
        for (Map.Entry<String, Fase> item : fasesConLlaves.entrySet()) {
            fasesList.add(new SelectItem(item.getKey()));
        }
        return fasesList;
    }

    public List<SelectItem> getGruposDefase() {
        List<SelectItem> grupoList = new ArrayList<>();
        grupoList.add(new SelectItem("-- Seleccione Grupo --"));

        if (!grupoListDiseable && (faseSelectedFinal != null)) {
            Fase faseSel = mapFases.get(faseSelectedFinal);
            List<Grupo> grupoInt = faseSel.getGrupoList();
            for (Grupo grp : grupoInt) {
                grupoList.add(new SelectItem(grp.getNombre()));
            }
        }
        return grupoList;
    }

    public List<SelectItem> getJornadasEnGrupo() {
        List<SelectItem> jornadaList = new ArrayList<>();
        jornadaList.add(new SelectItem("-- Seleccione Jornada --"));

        if (!jornadaListDiseable && (grupoSelected != null)) {
            Grupo grupoSel = mapGrupos.get(grupoSelected);
            for (Jornada jor : grupoSel.getJornadaList()) {
                jornadaList.add(new SelectItem(jor.getNumero().toString()));
            }
        }
        return jornadaList;
    }

    public List<SelectItem> getCategorias() {
        List<SelectItem> catList = new ArrayList<>();
        catList.add(new SelectItem("-- Seleccione Categoria --"));

        for (Categoria cat : categorias) {
            catList.add(new SelectItem(cat.getNombre()));
        }

        return catList;
    }

    public List<SelectItem> getEquipo1List() {
        List<SelectItem> equipo1List = new ArrayList<>();
        return makeEquiposList(equipo1List);
    }

    public List<SelectItem> getEquipo2List() {
        List<SelectItem> equipo2List = new ArrayList<>();
        return makeEquiposList(equipo2List);
    }

    private List<SelectItem> makeEquiposList(List<SelectItem> lista) {
        lista.add(new SelectItem("-- Seleccione Equipo --"));
        for (Map.Entry<String, Equipo> item : mapEquipos.entrySet()) {
            if (categoriaSelected.equals(item.getValue().getCategoriaId().getNombre())) {
                lista.add(new SelectItem(item.getValue().getClubId().getNombre()));
            }
        }
        return lista;
    }

    public List<SelectItem> getCanchasList() {
        List<SelectItem> canchasList = new ArrayList<>();
        canchasList.add(new SelectItem("-- Seleccione Cancha --"));
        for (Map.Entry<String, Cancha> item : mapCanchas.entrySet()) {
            canchasList.add(new SelectItem(item.getKey()));
        }
        return canchasList;
    }

    // Inicio de la lógica.....
    private void initVariables() {
        fasesReady = false;
        confGrupos = false;
        confLlaves = false;
        gruposConJornadas = new HashMap<>();
        readCategorias();
        readEquipos();
        readCanchas();
    }

    @Override
    public String prepareList() {
        cantFases = 0;
        super.recreateModel();
        return "List";
    }

    public void configFases(ValueChangeEvent event) {
        cantFases = (Integer) event.getNewValue();
        fasesStrings = new String[cantFases];
        tieneGrupos = new String[cantFases];
        cantGruposLlaves = new Integer[cantFases];
        enableCantFasesButton = true;
    }
    
    public void configGrupos(){
        this.confGrupos = true;
        System.out.println("Cambio el confGrupos a " + confGrupos);
    }
    
    public void configLlaves(){
        this.confLlaves = true;
        System.out.println("Cambio el confLlaves a " + confLlaves);
    }

    public void readFases() throws UnsupportedEncodingException {
        fases = new ArrayList<>();

        gruposTotales = 0;
        llavesTotales = 0;
        for (int i = 0; i < cantFases; i++) {
            fase = new Fase();
            fase.setNombre(fasesStrings[i]);

            grupos = new ArrayList<>();
            llaves = new ArrayList<>();
            if (tieneGrupos[i].equals("si")) {
                for (int j = 0; j < cantGruposLlaves[i]; j++) {
                    Grupo grupo = new Grupo();
                    grupos.add(grupo);
                    gruposTotales++;
                }
                fase.setGrupoList(grupos);
          //      fasesEnCombo.add(fase.getNombre());
                fasesConGrupos.put(fase.getNombre(), fase);
            } else {
                for(int j = 0; j < cantGruposLlaves[i]; j++){
                    Llave llave = new Llave();
                    llaves.add(llave);
                    llavesTotales++;
                }
                fase.setLlaveList(llaves);
                fasesConLlaves.put(fase.getNombre(), fase);
            }
            
            fases.add(fase);
            mapFases.put(fase.getNombre(), fase);

            super.getSelected().setFaseList(fases);
            System.out.println("Los grupos totales son " + gruposTotales);
        }

        this.fasesReady = true;
    }

    public void readGrupos() throws UnsupportedEncodingException {
        int i = 0;
        fase = fasesConGrupos.get(faseSelected);
        for (Grupo group : fase.getGrupoList()) {
            group.setNombre(nombreGrupo[i]);
            jornadasXGrupo = new ArrayList<>();
            for (int j = 0; j < cantJornadas[i]; j++) {
                jornada = new Jornada();
                jornada.setNumero(j + 1);
                jornadasXGrupo.add(jornada);
                mapJornadas.put(fase.getNombre() + "." + group.getNombre() + "-" + jornada.getNumero(), jornada);
            }
            group.setJornadaList(jornadasXGrupo);
            gruposTotales--;
            i++;
            System.out.println("Los grupos totales estan en: " + gruposTotales);
            mapGrupos.put(group.getNombre(), group);
        }
        if (gruposTotales == 0) {
            this.noHayGrupos = true;
            System.out.println("debe cambiar el valor de noHayGrupos: " + noHayGrupos);
        }
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Operación exitosa", "Grupos guardados para la fase " + fase.getNombre()));

    }
    
    // Arreglar este método
    public void readLlaves() throws UnsupportedEncodingException {
        int i = 0;
        fase = fasesConLlaves.get(faseConLlaveSelected);
        for (Llave llave : fase.getLlaveList()) {
            llave.setNombre(nombreGrupo[i]);
            partidosXLlave = new ArrayList<>();
            for (int j = 0; j < cantPartidosXLlave[i]; j++) {
                partido = new Partido();
                partidosXLlave.add(partido);
                // ver como se cambia esto
                //map.put(fase.getNombre() + "." + llave.getNombre() + "-" + jornada.getNumero(), jornada);
            }
            //llave.setList(jornadas);  <- colocar los partidos para la llave
            llavesTotales--;
            i++;
            System.out.println("Los llaves totales estan en: " + llavesTotales);
            mapLlaves.put(llave.getNombre(), llave);
        }
        if (llavesTotales == 0) {
            this.noHayGrupos = true;
            System.out.println("debe cambiar el valor de noHayGrupos: " + noHayGrupos);
        }
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Operación exitosa", "Llaves guardadas para la fase " + fase.getNombre()));

    }

    public void creaPartidos() {
        partidos = new Partido[cantPartidos];

        equipo1Selected = new String[cantPartidos];
        equipo2Selected = new String[cantPartidos];
        fechaPartido = new Date[cantPartidos];
        canchaSelected = new String[cantPartidos];

        System.out.println("La cantidad de partidos en el grupo es " + partidos.length);
    }
    
    public void creaPartidosEnLlave(){
        partidos = new Partido[cantPartidosEnLLave];
        
        equipo2Selected = new String[cantPartidosEnLLave];
        fechaPartido = new Date[cantPartidosEnLLave];
        canchaSelected = new String[cantPartidosEnLLave];

        System.out.println("La cantidad de partidos en la llave es " + partidos.length);
    }

    public void readPartidos() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Operación exitosa", "Configurados los partidos de esta jornada"));
        printCalendar();
    }

    private void printCalendar() {
        DateFormat fecha = new SimpleDateFormat("dd/MM/yyyy 'a las' h:mm a");
        System.out.println("Para la fase " + faseSelectedFinal);
        System.out.println("En el grupo " + grupoSelected);
        System.out.println("En la jornada " + jornadaSelected);
        System.out.println("De la categoria " + categoriaSelected + " se tienen los siguientes partidos:");
        for (int i = 0; i < cantPartidos; i++) {
            System.out.print(equipo1Selected[i] + " vs. " + equipo2Selected[i] + " en "
                    + canchaSelected[i] + " el dia: " + fecha.format(fechaPartido[i]) + "\n");
        }
    }

    private void generateGroups(String faceSelected) {
        System.out.println("Llegó a configurar los grupos");
        fase = fasesConGrupos.get(faceSelected);
        gruposEnFase = new ArrayList<>();
        grupos = fase.getGrupoList();
        for (Grupo item : grupos) {
            gruposEnFase.add("");
        }
        System.out.println("La fase " + fase.getNombre() + " tiene " + fase.getGrupoList().size() + " grupos.");
        nombreGrupo = new String[fase.getGrupoList().size()];
        cantJornadas = new Integer[fase.getGrupoList().size()];
    }
    
    private void generateLlaves(String faseConLlaveSelected) {
        System.out.println("Llegó a configurar las llaves");
        fase = fasesConLlaves.get(faseConLlaveSelected);
        llavesEnFase = new ArrayList<>();
        llaves = fase.getLlaveList();
        for (Llave item : llaves) {
            llavesEnFase.add("");
        }
        System.out.println("La fase " + fase.getNombre() + " tiene " + fase.getLlaveList().size() + " llaves.");
        nombreLlave = new String[fase.getLlaveList().size()];
        cantPartidosXLlave = new Integer[fase.getLlaveList().size()];
    }

    private void readCategorias() {
        categorias = getCategoriaJpaController().findCategoriaEntities();
    }

    private void readEquipos() {
        List<Equipo> equipos = getEquipoJpaController().findEquipoEntities();
        for (Equipo team : equipos) {
            String name = team.getClubId().getNombre() + team.getCategoriaId().getNombre();
            mapEquipos.put(name, team);
        }
    }

    private void readCanchas() {
        List<Cancha> canchas = getCanchaJpaController().findCanchaEntities();
        for (Cancha field : canchas) {
            mapCanchas.put(field.getNombre(), field);
        }
    }
}
