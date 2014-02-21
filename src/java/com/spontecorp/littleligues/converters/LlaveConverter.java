package com.spontecorp.littleligues.converters;

import com.spontecorp.littleligues.jpacontroller.extentions.LlaveJpaControllerExt;
import com.spontecorp.littleligues.model.torneo.Llave;
import com.spontecorp.littleligues.utils.LittleLiguesUtils;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author Andrés
 */
@FacesConverter("com.spontecorp.LlaveConverter")
public class LlaveConverter implements Converter{
    private LlaveJpaControllerExt jpaController = null;

    private LlaveJpaControllerExt getJpaController() {
        if (jpaController == null) {
            jpaController = new LlaveJpaControllerExt(LittleLiguesUtils.getEmf());
        }
        return jpaController;
    }

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
        if (value == null || value.length() == 0) {
            return null;
        }
        return getJpaController().findLlave(value);
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof Llave) {
            Llave o = (Llave) object;
            return o.toString();
        } else {
            throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Llave.class.getName());
        }
    }
}
