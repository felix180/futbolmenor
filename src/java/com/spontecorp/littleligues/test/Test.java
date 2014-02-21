/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.littleligues.test;

import com.spontecorp.littleligues.jpacontroller.CronistaJpaController;
import com.spontecorp.littleligues.model.Cronista;
import com.spontecorp.littleligues.utils.LittleLiguesUtils;
import java.net.URLDecoder;
import java.net.URLEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Test {

    private static Logger logger = LoggerFactory.getLogger(Test.class);
//    public static void main(String... args) throws Exception {
//        String input = "日本語";
//        System.out.println("Original input string from client: " + input);
//
//        String encoded = URLEncoder.encode(input, "UTF-8");
//        System.out.println("URL-encoded by client with UTF-8: " + encoded);
//
//        String incorrectDecoded = URLDecoder.decode(encoded, "ISO-8859-1");
//        System.out.println("Then URL-decoded by server with ISO-8859-1: " + incorrectDecoded);
//
//        String correctDecoded = URLDecoder.decode(encoded, "UTF-8");
//        System.out.println("Server should URL-decode with UTF-8: " + correctDecoded);
//    }
    
    public static void main(String[] args) {
        Cronista cronista = new Cronista();
        CronistaJpaController controller = new CronistaJpaController(LittleLiguesUtils.getEmf());
        
        try {
            cronista.setApellido("Testing");
            cronista.setNombre("MiTest");
            cronista.setColumna("La columnata");
            cronista.setDescripcion("Tremenda descripción");
            
            controller.create(cronista);
            logger.info("termnó bien");
        } catch (Exception e) {
            logger.error("Error al ejecura", e);
        }
                
        
        
    }
}
