/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.perisistencia.gatos_api;

import java.io.IOException;
import javax.swing.JOptionPane;

/**
 *
 * @author USER
 */
public class Gatos_API {

    public static void main(String[] args) throws IOException {
        
        int opcion_menu = -1; 
        String[] botones = {"1. Ver gatos", "2. Ver Favoirtos", "3. Salir"}; 
        
        do {           
            
            String opcion = (String) JOptionPane.showInputDialog(null, "Gatitos Java", "Menu principal",
                    JOptionPane.INFORMATION_MESSAGE, null, botones, botones[0]); 
            
            
            // creamos esta validacion para saber que valor toma la seccion de opcion 
            for (int i = 0; i < botones.length; i++) {
                
                if(opcion.equals(botones[i])) {
                    
                    opcion_menu = i; 
                }    
            }
            
            switch (opcion_menu) {
                case 0: 
                    GatosService.verGatos(); 
                    continue;
                case 1:
                    GatosFav gato = new GatosFav(); 
                    GatosService.verFavoritos(gato); 
                    break;
                    
                default:
                    break;
            }
            
        } while (opcion_menu != 1);
        
        
    }
}
