/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.perisistencia.gatos_api;

import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author USER
 */
public class GatosService {
    
    public static void verGatos() throws IOException {
        
        // Traemos los datos de la Api dados por postman
        
        OkHttpClient client = new OkHttpClient();
        // MediaType mediaType = MediaType.parse("text/plain");
        // RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
          .url("https://api.thecatapi.com/v1/images/search")
          .get()
          .build();
        Response response = client.newCall(request).execute();
        
        String elJson = response.body().string(); 
        
        // devemos cortar los primeros corchetes de iteracion del archivo
        elJson = elJson.substring(1, elJson.length()); // corte el primer corteche
        elJson = elJson.substring(0, elJson.length()-1); // corto el ultimo corchete
        
        // creo el objeto de la Gson
        Gson gson = new Gson(); 
        
        // Apartir del gson, creo el objeto gato
        Gatos gatos = gson.fromJson(elJson, Gatos.class); 
        
        // Redimencinar Imagen 
        
        Image image = null; 
        
        int opcion_menu = -1;
        
        try {
            URL url = new URL(gatos.getUrl());
            //Creo una conexión HTTP con el servidor que aloja la imagen del perro
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            // Agrega una propiedad de solicitud al objeto http
            http.addRequestProperty("User-Agent", "");
            BufferedImage bufferedImage = ImageIO.read(http.getInputStream());
            ImageIcon fondoGato = new ImageIcon(bufferedImage); 
            
            if (fondoGato.getIconWidth() > 800 || fondoGato.getIconHeight() > 500) {
                
                // redimencionar
                Image fondo = fondoGato.getImage();
                Image modificada = fondo.getScaledInstance(800, 500, java.awt.Image.SCALE_SMOOTH); 
                fondoGato = new ImageIcon(modificada);
            }
            
            String menu = "Opciones: \n "
                    + "1. Ver otra imagen  \n "
                    + "2. Favorito \n"
                    + "3. Volver \n";
            
            String[] botones = {"Ver otra imagen", "Favorito", "Volver"};

            String id_gato = gatos.getId();
            String opcion = (String) JOptionPane.showInputDialog(null, menu, id_gato, JOptionPane.INFORMATION_MESSAGE, fondoGato,botones, botones[0]);
               
            for (int i = 0; i < botones.length; i++) {
                
                if (opcion.equals(botones[i])) {
                    
                    opcion_menu = i; 
                    
                }
                
            }
            
            switch (opcion_menu) {
                case 0:
                    verGatos();
                    
                    break; 
                    
                case 1:
                    favoritoGato(gatos); 
                    break;
                   
                case 2:
                    
                   break;
                   
                default:
                    break;
            }
            
        } catch (IOException e) {
            
            System.out.println(e);
        }
    }
    
    public static void favoritoGato(Gatos gatos) {

        try {
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\r\n\t\"image_id\":\""+ gatos.getId() +"\",\r\n\t\"sub_id\":\"your-user-1234\"\r\n}");
            Request request = new Request.Builder()
              .url("https://api.thecatapi.com/v1/favourites")
              .method("POST", body)
              .addHeader("Content-Type", "application/json")
              .addHeader("x-api-key", gatos.getApikey())
              .build();
            Response response = client.newCall(request).execute();
                    
        } catch (IOException e) {
            
            System.out.println(e);
        }
    }
    
    public static void verFavorito(String apiKey) throws IOException {
        
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
          .url("https://api.thecatapi.com/v1/favourites")
          .method("GET", body)
          .addHeader("x-api-key", apiKey)
          .build();
        Response response = client.newCall(request).execute();
        
        String elJson = response.body().string(); 
        
        Gson gson = new Gson();
        
        GatosFav[] gatosArray = gson.fromJson(elJson, GatosFav[].class); 
        
        // Vamos a seleccionar una de los favoritos aleatoriamente
        
        if (gatosArray.length > 0 ) {
            
            int min = 1;
            int max = gatosArray.length;
            int aleatorio = (int) (Math.random() * ((max-min)-1)) + min; 
            int indice = aleatorio-1; 
            
            GatosFav gatoFav = gatosArray[indice]; 
            
            
        }
        
        
    }
    
}
