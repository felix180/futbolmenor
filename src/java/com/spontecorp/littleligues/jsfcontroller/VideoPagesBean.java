package com.spontecorp.littleligues.jsfcontroller;

import com.spontecorp.littleligues.jpacontroller.VideoJpaController;
import com.spontecorp.littleligues.jsfcontroller.util.PaginationHelper;
import com.spontecorp.littleligues.model.Video;
import com.spontecorp.littleligues.utils.LittleLiguesUtils;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

/**
 *
 * @author jgcastillo
 */
@ManagedBean(name = "videoPagesBean")
@RequestScoped
public class VideoPagesBean {

    private VideoJpaController jpaController = null;
    private DataModel<Video> items;
    private Video entity;
    private PaginationHelper pagination;
    private int cantidadVideos = 3;

    /**
     * Creates a new instance of VideoPagesBean
     */
    public VideoPagesBean() {
    }

    private VideoJpaController getJpaController() {
        if (jpaController == null) {
            jpaController = new VideoJpaController(LittleLiguesUtils.getEmf());
        }
        return jpaController;
    }

    public Video getEntity() {
        if (entity == null) {
            entity = new Video();
        }
        return entity;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(cantidadVideos) {

                @Override
                public int getItemsCount() {
                    return getJpaController().getVideoCount();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getJpaController().findVideoEntities(getPageSize(), getPageFirstItem()));
                }
            };
        }
        return pagination;
    }

    private void recreateModel() {
        items = null;
    }

    public String next() {
        getPagination().nextPage();
        recreateModel();
        return "listaVideos";
    }

    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "listaVideos";
    }

    public DataModel<Video> getItems() {
        if (items == null) {
        //items = new ListDataModel<>(getJpaController().findVideoEntities());
            items = getPagination().createPageDataModel();
        }
        return items;
    }
}
