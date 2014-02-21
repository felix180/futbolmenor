package com.spontecorp.littleligues.converters;

import com.spontecorp.littleligues.jpacontroller.LigaJpaController;
import com.spontecorp.littleligues.jpacontroller.extentions.LigaJpaControllerExt;
import com.spontecorp.littleligues.model.liga.Liga;
import com.spontecorp.littleligues.utils.LittleLiguesUtils;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author jgcastillo
 */
@FacesConverter("com.spontecorp.LigaConverter")
public class LigaConverter implements Converter {

    private LigaJpaControllerExt jpaController = null;
    
    private LigaJpaControllerExt getJpaController(){
        if(jpaController == null){
            jpaController = new LigaJpaControllerExt(LittleLiguesUtils.getEmf());
        }
        return jpaController;
    }
    
    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
        if (value == null || value.length() == 0) {
            return null;
        }
        
        return getJpaController().findLiga(value);
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof Liga) {
            Liga o = (Liga) object;
            return o.toString();
        } else {
            throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Liga.class.getName());
        }
    }

}
