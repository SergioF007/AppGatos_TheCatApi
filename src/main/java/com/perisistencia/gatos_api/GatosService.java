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
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

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
        
        
        int opcion_menu = -1;
        
        try {
            URL url = new URL(gatos.getUrl());
            //Creo una conexión HTTP con el servidor que aloja la imagen del perro
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            // Agrega una propiedad de solicitud al objeto http
            http.addRequestProperty("User-Agent", "");
            BufferedImage bufferedImage = ImageIO.read(http.getInputStream());
            ImageIcon fondoGato = new ImageIcon(bufferedImage); 
            
            if (fondoGato.getIconWidth() > 400 || fondoGato.getIconHeight() > 400) {
                
                // redimencionar
                Image fondo = fondoGato.getImage();
                Image modificada = fondo.getScaledInstance(400, 400, java.awt.Image.SCALE_SMOOTH); 
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
    
public static void verFavoritos(GatosFav gatosFav) throws IOException {
        
    OkHttpClient client = new OkHttpClient();

    Request request = new Request.Builder()
        .url("https://api.thecatapi.com/v1/favourites")
        .get()
        .addHeader("x-api-key", gatosFav.getApikey())
        .build();
    Response response = client.newCall(request).execute();
    
    String elJson = response.body().string(); 
    
    Gson gson = new Gson();
    
    GatosFav[] gatosArray = gson.fromJson(elJson, GatosFav[].class); 
    
    // ArrayList<ImageIcon> listFav = new ArrayList<>(); 
    
    // crear un panel para mostrar las imágenes
    JPanel panel = new JPanel(new GridLayout(0, 4));
    panel.setPreferredSize(new Dimension(1000, 800));
            
    for (GatosFav gatoFav : gatosArray) {
        Image image = null; 

        try {
            URL url = new URL(gatoFav.image.getUrl());
            //Creo una conexión HTTP con el servidor que aloja la imagen del perro
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            // Agrega una propiedad de solicitud al objeto http
            http.addRequestProperty("User-Agent", "");
            BufferedImage bufferedImage = ImageIO.read(http.getInputStream());
            ImageIcon fondoGato = new ImageIcon(bufferedImage); 

            if (fondoGato.getIconWidth() > 400 || fondoGato.getIconHeight() > 400) {

                // redimensionar
                Image fondo = fondoGato.getImage();
                Image modificada = fondo.getScaledInstance(200, 200, java.awt.Image.SCALE_SMOOTH); 
                fondoGato = new ImageIcon(modificada);
            }
            
            // listFav.add(fondoGato); 
            
            //  Crea un nuevo JPanel para cada imagen
            JPanel panelImg = new JPanel(new BorderLayout()); 
            // Agrego el Id de la imagen al panel  y Agrego la Imagen
            JLabel imgId = new JLabel("ID: " + gatoFav.getId(), JLabel.CENTER); 
            JLabel img = new JLabel(fondoGato, JLabel.CENTER);
            
            // Los agrego al panel
            panelImg.add(imgId, BorderLayout.NORTH); 
            panelImg.add(img, BorderLayout.CENTER); 
            
            panelImg.setPreferredSize(new Dimension(100, 50));

            
            panel.add(panelImg);


        } catch (IOException e) {
            System.out.println(e);
        }
    }
    

    // agregar un JLabel para cada imagen almacenada en la lista
 /* 
    for (ImageIcon imagen : listFav) {
        JLabel label = new JLabel(imagen);
        panel.add(label);
    } */

    // agregar el panel a un JScrollPane para que sea posible desplazarse por las imágenes si hay muchas
    // JScrollPane scrollPane = new JScrollPane(panel);
    // scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    // scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    
    //JScrollPane scrollPane = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    
    // crear un panel a la derecha con el Select text label y el botón OK
    JPanel panelDerecho = new JPanel(new GridLayout(3, 1));
    JComboBox<String> opciones = new JComboBox<>(new String[]{"Eliminar Favorito", "Volver"});
    JButton botonOK = new JButton("OK");
    panelDerecho.add(new JLabel("Seleccionar opción:"));
    panelDerecho.add(opciones);
    panelDerecho.add(botonOK);
    
    JPanel panelTotal = new  JPanel(); 
    panelTotal.add(panel, BorderLayout.CENTER);
    panelTotal.add(panelDerecho, BorderLayout.EAST); 
    
    JScrollPane scrollPane = new JScrollPane(panelTotal, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

    // mostrar el JScrollPane en un JOptionPane.showInputDialog
    //JOptionPane.showInputDialog(null, scrollPane, "Gatos favoritos", JOptionPane.PLAIN_MESSAGE);
    
    // crear un JFrame para mostrar el JScrollPane en una ventana
    JFrame frame = new JFrame("Gatos favoritos");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    //frame.add(panel, BorderLayout.CENTER);
    //frame.add(panelDerecho, BorderLayout.EAST);
    frame.add(scrollPane); 
    frame.pack();
    frame.setVisible(true);
    

}
    
    public static void borrarFavorito(GatosFav gatoFav) {

        
        
    }
    
}
