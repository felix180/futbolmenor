package com.spontecorp.littleligues.jsfcontroller.liga;

import com.spontecorp.littleligues.jpacontroller.ClubJpaController;
import com.spontecorp.littleligues.jpacontroller.DireccionJpaController;
import com.spontecorp.littleligues.jpacontroller.LocalidadJpaController;
import com.spontecorp.littleligues.jpacontroller.extentions.CanchaJpaControllerExt;
import com.spontecorp.littleligues.jsfcontroller.liga.util.JsfUtil;
import com.spontecorp.littleligues.jsfcontroller.liga.util.PaginationHelper;
import com.spontecorp.littleligues.model.liga.Cancha;
import com.spontecorp.littleligues.model.liga.Club;
import com.spontecorp.littleligues.model.liga.Direccion;
import com.spontecorp.littleligues.model.liga.Localidad;
import com.spontecorp.littleligues.utils.LittleLiguesUtils;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

@ManagedBean(name = "clubController")
@SessionScoped
public class ClubController implements Serializable {

    private Club current;
    private Localidad localidad;
    private Direccion direccion;
    private DataModel items = null;
    private List<String> selectedItems;
    private ClubJpaController jpaController = null;
    private DireccionJpaController direccionJpaController = null;
    private LocalidadJpaController localidadJpaController = null;
    private CanchaJpaControllerExt canchaJpaController = null;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private LittleLiguesUtils utils = new LittleLiguesUtils();
    public static final String SECTION_CLUBES = "clubes";
    private String IMAGE_RETURN = "";
    
    private UploadedFile uploadedFile;
    private StreamedContent dbImage;
    private boolean hayImagen;

    public ClubController() {
    }

    public Club getSelected() {
        if (current == null) {
            current = new Club();
            localidad = new Localidad();
            direccion = new Direccion();
            direccion.setLocalidadId(localidad);
            current.setDireccionId(direccion);
            selectedItemIndex = -1;
        }
        return current;
    }

    private ClubJpaController getJpaController() {
        if (jpaController == null) {
            jpaController = new ClubJpaController(LittleLiguesUtils.getEmf());
        }
        return jpaController;
    }

    private DireccionJpaController getDireccionJpaController() {
        if(direccionJpaController == null){
            direccionJpaController = new DireccionJpaController(LittleLiguesUtils.getEmf());
        }
        return direccionJpaController;
    }

    private LocalidadJpaController getLocalidadJpaController() {
        if (localidadJpaController == null) {
            localidadJpaController = new LocalidadJpaController(LittleLiguesUtils.getEmf());
        }
        return localidadJpaController;
    }

    private CanchaJpaControllerExt getCanchaJpaController(){
        if(canchaJpaController == null){
            canchaJpaController = new CanchaJpaControllerExt(LittleLiguesUtils.getEmf());
        }
        return canchaJpaController;
    }
    
    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    public Localidad getLocalidad() {
        return localidad;
    }
    
    public void setLocalidad(Localidad localidad) {
        this.localidad = localidad;
    }

    public List<String> getSelectedItems() {
        return selectedItems;
    }

    public void setSelectedItems(List<String> selectedItems) {
        this.selectedItems = selectedItems;
    }

    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }
    
    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {
                @Override
                public int getItemsCount() {
                    return getJpaController().getClubCount();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getJpaController().findClubEntities(getPageSize(), getPageFirstItem()));
                }
            };
        }
        return pagination;
    }

    public String prepareList() {
        recreateModel();
        return "List";
    }

    public String prepareView() {
        current = (Club) getItems().getRowData();
        direccion = current.getDireccionId();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Club();
        localidad = new Localidad();
        direccion = new Direccion();
        direccion.setLocalidadId(localidad);
        current.setDireccionId(direccion);
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            direccion.setLocalidadId(localidad);
            getDireccionJpaController().create(direccion);
            current.setDireccionId(direccion);
            addCanchas();
            getJpaController().create(current);
            
            //Cargo una imagen si existe
            update();
            
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ClubCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }
    
    public boolean isHayImagen() {
        return hayImagen;
    }
    
//    public void upload() throws IOException {
//        if (uploadedFile != null) {
//            String contentType = uploadedFile.getContentType();
//            byte[] bytes = uploadedFile.getContents();
//            current.setLogo(bytes);
//
//            String mensaje = uploadedFile.getFileName() + " del tipo: " + contentType + " se ha cargado.";
//            FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, mensaje, mensaje);
//            FacesContext.getCurrentInstance().addMessage(null, facesMsg);
//        }
//    }
    
    
    /**
     * Metodo para validar las extensiones, peso de la imagen y copiar imagen al
     * Directorio
     * @throws IOException 
     */
    public void upload() throws IOException {
        if (uploadedFile != null) {
            String nombreImagen = String.valueOf(current.getId());
            //Se carga el archivo
            boolean retorno = utils.saveFile(uploadedFile, nombreImagen, SECTION_CLUBES);
            if (retorno) {
                String extension = uploadedFile.getFileName().substring(uploadedFile.getFileName().lastIndexOf(".") + 1);
                nombreImagen = nombreImagen + "." + extension;
                current.setLogo(nombreImagen);
            }
        }
    }
    
//    public StreamedContent getStreamedImageById() {
//        FacesContext context = FacesContext.getCurrentInstance();
//        System.out.println("Llego a getStreamedImagedById");
//        if (context.getRenderResponse()) {
//            return new DefaultStreamedContent();
//        } else {
//            String id = context.getExternalContext().getRequestParameterMap().get("id");
//            System.out.println("El id encontrado es: " + id);
//            Club club = getJpaController().findClub(Integer.valueOf(id));
//            if(club.getLogo() != null){ 
//                InputStream inputStream = new ByteArrayInputStream(club.getLogo());
//                return new DefaultStreamedContent(inputStream, "image/jpeg");
//            }
//        }
//        return new DefaultStreamedContent();
//    }
    
    private void addCanchas(){
        List<Cancha> canchas = current.getCanchaList();
        try {
            for(String selectedItem : selectedItems){
                Cancha cancha = getCanchaJpaController().findCancha(selectedItem);
                canchas.add(cancha);
            }
            current.setCanchaList(canchas);
        } catch (Exception e) {
        }
    }
    
    private void updateCanchas(){
        List<Cancha> canchas = current.getCanchaList();
        try {
            for (String selectedItem : selectedItems) {
                Cancha cancha = getCanchaJpaController().findCancha(selectedItem);
                //System.out.println("la cancha seleccionada es: " + cancha.getNombre());
                canchas.add(cancha);
            }
            current.setCanchaList(canchas);
        } catch (Exception e){
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    public String prepareEdit() {
        current = (Club) getItems().getRowData();
        direccion = current.getDireccionId();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }
    
    public String prepareEditFromView(){
        return "Edit";
    }

    public String update() {
        try {
            upload();
            direccion.setLocalidadId(localidad);
            getDireccionJpaController().edit(direccion);
            current.setDireccionId(direccion);
            updateCanchas();
            getJpaController().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ClubUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }
    
    public String prepareDestroy(){
        current = (Club) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Delete";
    }
    
    public String prepareDestroyFromView(){
        return "Delete";
    }

    public String destroy() {
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }

    public String destroyAndView() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (selectedItemIndex >= 0) {
            return "View";
        } else {
            // all items were removed - go back to list
            recreateModel();
            return "List";
        }
    }

    private void performDestroy() {
        try {
            
            //Elimino la imagen actual si existe
            String nombreImagenActual = current.getLogo();
            if (nombreImagenActual != null) {
                utils.deleteFile(nombreImagenActual, SECTION_CLUBES);
            }
            
            getDireccionJpaController().destroy(current.getDireccionId().getId());
            getJpaController().destroy(current.getId());
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ClubDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    private void updateCurrentItem() {
        int count = getJpaController().getClubCount();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getJpaController().findClubEntities(1, selectedItemIndex).get(0);
        }
    }

    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }

    private void recreateModel() {
        items = null;
    }

    private void recreatePagination() {
        pagination = null;
    }

    public String getIMAGE_RETURN() {
        IMAGE_RETURN = utils.getIMAGE_RETURN() + utils.path_images_clubes;
        return IMAGE_RETURN;
    }

    public void setIMAGE_RETURN(String IMAGE_RETURN) {
        this.IMAGE_RETURN = IMAGE_RETURN;
    }
    
    public String next() {
        getPagination().nextPage();
        recreateModel();
        return "List";
    }

    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "List";
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(getJpaController().findClubEntities(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(getJpaController().findClubEntities(), true);
    }

    @FacesConverter(forClass = Club.class)
    public static class ClubControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ClubController controller = (ClubController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "clubController");
            return controller.getJpaController().findClub(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Club) {
                Club o = (Club) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Club.class.getName());
            }
        }
    }
}
