/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.littleligues.utils;

import com.spontecorp.littleligues.model.Noticia;
import java.awt.image.BufferedImage;
import java.awt.image.ImagingOpException;
import java.io.*;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.model.SelectItem;
import javax.imageio.ImageIO;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.imgscalr.Scalr;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Casper
 */
public class LittleLiguesUtils {

    private static final String PERSISTENCE_UNIT = "LittleLigues2PU";
    public static final int PAG_PRINCIPAL = 1;
    public static final int NO_PAG_PRINCIPAL = 0;
    public static final int ARTICULO_ACTIVO = 1;
    public static final int ARTICULO_INACTIVO = 0;
    public static final String ISO8859 = "ISO-8859-1";
    public static final String UTF8 = "UTF-8";
    public static final int STATUS_ACTIVO = 1;
    public static final int STATUS_INACTIVO = 1;
    
    //RutaProduccion
    public static final String STORAGE_ROOT = "E:/apache-tomcat-6.0.35/webapps/imagenesfm/";
    private String IMAGE_RETURN = "http://futbolmenor.dyndns.org:8080/imagenesfm/";
    
    //RutaDesarrolloOficina
//    public static final String STORAGE_ROOT = "C:/apache-tomcat-6.0.35/webapps/imagenesfm/";
//    private String IMAGE_RETURN = "http://localhost:8080/imagenesfm/";
    
    //RutaDesarrolloCasa
//    public static final String STORAGE_ROOT = "C:/Users/zuleidyb/Desktop/imagenesfm/";
//    private String IMAGE_RETURN = "http://localhost:8085/imagenesfm/";
    
    public String path_images_noticias = "noticias/";
    public String path_images_clubes = "clubes/";
    public static final String SECTION_NOTICIAS = "noticias";
    public static final String SECTION_CLUBES = "clubes";
    public String path = null;
    // 350KB Tamaño maximo permitido
    public long maxImageBytes = 350000;
    // peso en bytes del arhivo o imagen redimensionada
    private long FileLength = 0;
    //Maximun Values For media
    public static final int MAX_IMG_WIDTH = 800;
    public static final int MAX_IMG_HEIGHT = 600;
    public static final int MAX_IMG_THUMWIDTH = 200;
    public static final int MAX_IMG_THUMHEIGHT = 150;
    //ancho de la imagen despues de redimensionada
    private int width = 0;
    //alto de la imagen despues de redimensionada
    private int height = 0;
    //ancho del thumbs la imagen despues de redimensionada
    private int widththumb = 0;
    //alto del thumbs la imagen despues de redimensionada
    private int heightthumb = 0;

    public static EntityManagerFactory getEmf() {
        return Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
    }

    public static SelectItem[] getSelectItems(List<?> entities, boolean selectOne) {
        int size = selectOne ? entities.size() + 1 : entities.size();
        SelectItem[] items = new SelectItem[size];
        int i = 0;
        if (selectOne) {
            items[0] = new SelectItem("", "---");
            i++;
        }
        for (Object x : entities) {
            items[i++] = new SelectItem(x, x.toString());
        }
        return items;
    }

    public static void addErrorMessage(Exception ex, String defaultMsg) {
        String msg = ex.getLocalizedMessage();
        if (msg != null && msg.length() > 0) {
            addErrorMessage(msg);
        } else {
            addErrorMessage(defaultMsg);
        }
    }

    public static void addErrorMessages(List<String> messages) {
        for (String message : messages) {
            addErrorMessage(message);
        }
    }

    public static void addErrorMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
    }

    public static void addSuccessMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
        FacesContext.getCurrentInstance().addMessage("successInfo", facesMsg);
    }

    public static String getRequestParameter(String key) {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(key);
    }

    public static Flash flashScope() {
        return FacesContext.getCurrentInstance().getExternalContext().getFlash();
    }

    /**
     *
     * Metodo para subir la imagen a la ruta especificada en el path
     *
     * @return
     * @throws IOException
     */
    public boolean saveFile(UploadedFile uploadedFile, String nombre, String section) throws IOException {
        String extension = "";
        boolean retorno = false;

        // Se validan las extenciones 
        if (!uploadedFile.getFileName().substring(uploadedFile.getFileName().lastIndexOf(".")).equalsIgnoreCase(".jpg")
                && !uploadedFile.getFileName().substring(uploadedFile.getFileName().lastIndexOf(".")).equalsIgnoreCase(".png")
                && !uploadedFile.getFileName().substring(uploadedFile.getFileName().lastIndexOf(".")).equalsIgnoreCase(".gif")) {
            String mensaje = "Tipo de archivo inválido. Extensiones permitidas: jpg, png, gif.";
            FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, mensaje, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, facesMsg);
            // Se valida el tamaño maximo de bytes
        } else if (uploadedFile.getSize() > maxImageBytes) {
            String mensaje = "En archivo que intenta cargar supera el tamaño permitido."
                    + " El tamaño máximo permitido para una imagen es " + maxImageBytes + " bytes";
            FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, mensaje, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, facesMsg);
        } else {
            
            //Se elimina el archivo si existe
            deleteAllFile(nombre, section);

            if (section.equals(SECTION_CLUBES)) {
                path = STORAGE_ROOT + path_images_clubes;
            } else {
                path = STORAGE_ROOT + path_images_noticias;
            }

            //Se carga el archivo
            try {
                InputStream imageInputStream = new ByteArrayInputStream(uploadedFile.getContents());

                BufferedImage im = ImageIO.read(imageInputStream);

                int originalHeight = im.getHeight();
                int originalWidth = im.getWidth();

                // Dimensiones maxima para la imagen
                int maxWidth = MAX_IMG_WIDTH;
                int maxHeight = MAX_IMG_HEIGHT;
                // Dimensiones maximas para el tumb
                int tumbw = MAX_IMG_THUMWIDTH;
                int tumbh = MAX_IMG_THUMHEIGHT;

                double scale = Math.min((double) maxWidth / im.getWidth(), (double) maxHeight / im.getHeight());
                double tumscale = Math.min((double) tumbw / im.getWidth(), (double) tumbh / im.getHeight());

                // Se crea la escala para el tumbs

                // Se buca el formato de imagen
                String originalFileName = uploadedFile.getFileName();
                extension = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
                String fileFormat;
                if (extension.equalsIgnoreCase("GIF")) {
                    fileFormat = "gif";
                } else if (extension.equalsIgnoreCase("PNG")) {
                    fileFormat = "png";
                } else {
                    fileFormat = "jpg";
                }

                if (tumscale < 1.0) {
                    originalWidth = (int) (originalWidth * tumscale);
                    originalHeight = (int) (originalHeight * tumscale);

                    //Se crea una nueva imagen a partir de la original redimensionada
                    BufferedImage resizedImage = Scalr.resize(im, 150); // Scale image

                    // Se crea el archivo del Thumbnails 
                    String prefix = path + String.valueOf("thumb_" + nombre);
                    String sufix = fileFormat;
                    File resizedImageFile = new File(prefix + "." + sufix);
                    ImageIO.write(resizedImage, fileFormat, resizedImageFile);

                    widththumb = resizedImage.getWidth();
                    heightthumb = resizedImage.getHeight();

                } else {
                    // Se crea el tumbs sin redimensionar
                    String prefix = path + String.valueOf("thumb_" + nombre);
                    String sufix = fileFormat;
                    File file = new File(prefix + "." + sufix);
                    byte[] bytes = uploadedFile.getContents();
                    try (FileOutputStream fos = new FileOutputStream(file)) {
                        fos.write(bytes);
                        fos.flush();
                    }

                    widththumb = im.getWidth();
                    heightthumb = im.getHeight();
                }

                // La escala es la proporcio de cuanto mas grande es la imagen original del maximo 800 x 600
                originalHeight = im.getHeight();
                originalWidth = im.getWidth();
                File resizedImageFile = null;

                // Si la escala es positiva pero el archivo pesa mucho se escala al 10% veces sucesivas
                scale = uploadedFile.getSize() > maxImageBytes && scale > 1.0 ? 0.9 : scale;

                if (scale < 1.0) {

                    do {
                        originalWidth = (int) (originalWidth * scale);
                        originalHeight = (int) (originalHeight * scale);

                        //Se crea una nueva imagen a partir de la original redimensionada
                        BufferedImage resizedImage = Scalr.resize(im, 150); // Scale image

                        // Se crea el archivo de la imagen redimensionada y la extension
                        String prefix = path + String.valueOf("thumb_" + nombre);
                        String sufix = fileFormat;
                        resizedImageFile = new File(prefix + "." + sufix);
                        ImageIO.write(resizedImage, fileFormat, resizedImageFile);
                        height = resizedImage.getHeight();
                        width = resizedImage.getWidth();

                    } while (resizedImageFile != null && resizedImageFile.length() > maxImageBytes);

                    // Se toma el peso final de la imagen
                    FileLength = resizedImageFile != null ? resizedImageFile.length() : 0;

                    if (FileLength > 0) {
                        retorno = true;
                    }
                }
            } catch (IOException | IllegalArgumentException | ImagingOpException ex) {
                System.out.println("Clase: " + this.getClass().getName() + ", Error al redimencionar la imagen " + ex);
                return false;
            }

            try {

                // Se buca el formato de imagen
                String originalFileName = uploadedFile.getFileName();
                extension = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
                String fileFormat;
                if (extension.equalsIgnoreCase("GIF")) {
                    fileFormat = "gif";
                } else if (extension.equalsIgnoreCase("PNG")) {
                    fileFormat = "png";
                } else {
                    fileFormat = "jpg";
                }

                String prefix = path + String.valueOf(nombre);
                String sufix = fileFormat;
                File file = new File(prefix + "." + sufix);
                byte[] bytes = uploadedFile.getContents();
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    fos.write(bytes);
                    fos.flush();
                    fos.close();
                }
                

                BufferedImage im = ImageIO.read(file);
                String contentType = uploadedFile.getContentType();
                this.height = im.getHeight();
                this.width = im.getWidth();

                FileLength = file != null ? file.length() : 0;
                retorno = FileLength > 0;

                if (retorno) {
                    String mensaje = "La imagen del tipo: " + contentType + " se ha cargado con éxito!";
                    FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, mensaje, mensaje);
                    FacesContext.getCurrentInstance().addMessage(null, facesMsg);
                }

            } catch (Exception e) {
                System.out.println("Clase: " + this.getClass().getName() + ", Error al salvar el archivo en la ruta = " + path);
                return false;
            }
        }
        return retorno;
    }

    /**
     * Eliminar Imagen del Directorio
     *
     * @param srcFile
     * @throws IOException
     */
    public void deleteAllFile(String srcFile, String section) throws IOException {
        
        if (section.equals(SECTION_CLUBES)) {
            path = STORAGE_ROOT + path_images_clubes;
        } else {
            path = STORAGE_ROOT + path_images_noticias;
        }
        
        File deletedFile = new File(path + srcFile + ".jpg");
        File deletedFileThumb = new File(path + "thumb_" + srcFile + ".jpg");
        File deletedFile1 = new File(path + srcFile + ".png");
        File deletedFileThumb1 = new File(path + "thumb_" + srcFile + ".png");
        File deletedFile2 = new File(path + srcFile + ".gif");
        File deletedFileThumb2 = new File(path + "thumb_" + srcFile + ".gif");
        deletedFile.delete();
        deletedFileThumb.delete();
        deletedFile1.delete();
        deletedFileThumb1.delete();
        deletedFile2.delete();
        deletedFileThumb2.delete();
    }

    /**
     *
     * @param srcFile
     * @throws IOException
     */
    public void deleteFile(String srcFile, String section) throws IOException {

        if (section.equals(SECTION_CLUBES)) {
            path = STORAGE_ROOT + path_images_clubes;
        } else {
            path = STORAGE_ROOT + path_images_noticias;
        }
        if (path != null) {
            File deletedFile = new File(path + srcFile);
            File deletedFileThumb = new File(path + "thumb_" + srcFile);
            deletedFile.delete();
            deletedFileThumb.delete();
        }
    }

    public long getFileLength() {
        return FileLength;
    }

    public void setFileLength(long FileLength) {
        this.FileLength = FileLength;
    }

    public String getIMAGE_RETURN() {
        return IMAGE_RETURN;
    }

    public void setIMAGE_RETURN(String IMAGE_RETURN) {
        this.IMAGE_RETURN = IMAGE_RETURN;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath_images_clubes() {
        return path_images_clubes;
    }

    public void setPath_images_clubes(String path_images_clubes) {
        this.path_images_clubes = path_images_clubes;
    }

    public String getPath_images_noticias() {
        return path_images_noticias;
    }

    public void setPath_images_noticias(String path_images_noticias) {
        this.path_images_noticias = path_images_noticias;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getHeightthumb() {
        return heightthumb;
    }

    public void setHeightthumb(int heightthumb) {
        this.heightthumb = heightthumb;
    }

    public long getMaxImageBytes() {
        return maxImageBytes;
    }

    public void setMaxImageBytes(long maxImageBytes) {
        this.maxImageBytes = maxImageBytes;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getWidththumb() {
        return widththumb;
    }

    public void setWidththumb(int widththumb) {
        this.widththumb = widththumb;
    }
}
