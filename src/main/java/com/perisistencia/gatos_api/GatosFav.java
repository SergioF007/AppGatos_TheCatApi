/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.perisistencia.gatos_api;

/**
 *
 * @author USER
 */

// Creo esta clase para poder trabajar con el tipo de formato de resputa que se tiene
// al hacer la GET favoritos.
public class GatosFav {
    
    String id;
    String image_id; 
    String apikey = "live_XIN3L3cOcvKXlUfBbCs0rnIAlAPat08GZ5f1T1rlQcY3st90JyHc2h12QdiqQZQC"; 
    Imagex image; 

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage_id() {
        return image_id;
    }

    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }

    public String getApikey() {
        return apikey;
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
    }

    public Imagex getImage() {
        return image;
    }

    public void setImage(Imagex image) {
        this.image = image;
    }
    
    
}
