/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.filesorter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ville Terasahde
 */
public class Listen extends Thread{
    private final Properties prop;
    private final String propFileName;
    private final FileReader reader;
    private File from;
    
    public Listen() throws FileNotFoundException, IOException{
            this.prop = new Properties();
            this.propFileName = "C:\\netbeansprojects\\filesorter\\src\\main\\java\\com\\mycompany\\filesorter\\config.properties";
            this.reader = new FileReader(this.propFileName);
    }
    @Override
    public void run(){
        if(from == null){
            System.out.println("Source folder not set");
        } else{
            
            //find the files in source folder
                System.out.println("");
                System.out.println("Running...");
                System.out.println("");
                Timer timer = new Timer();
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() { 
                    File[] filesInDirectory = from.listFiles();
                    for(var item : filesInDirectory){
                        String extension = getFileExtension(item.toString());
                        if(prop.keySet().contains(extension)){
                            try {
                                Files.move(Paths.get(item.getPath()), Paths.get(new File(prop.getProperty(extension)).getPath() + "\\" + item.getName()), StandardCopyOption.ATOMIC_MOVE);
                            } catch (IOException ex) {
                                Logger.getLogger(Listen.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                         else try {
                             Files.move(Paths.get(item.getPath()), Paths.get(new File(prop.getProperty("default_destination_path")).getPath() + "\\" + item.getName()), StandardCopyOption.ATOMIC_MOVE);
                        } catch (IOException ex) {
                            Logger.getLogger(Listen.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    }
                   
                };
                timer.schedule(task, 0, 5000);
        }
    }
    public void configuratePaths() throws IOException {
        try{
            prop.load(reader);
            from = new File(prop.getProperty(("source_path")));
            reader.close();
        } catch(FileNotFoundException e){
            System.out.println("property file not found");
        }
    }  

     public void setSource(String path) throws FileNotFoundException, IOException{
         if (!new File(path).exists()) {
                    System.out.println("Folder not found. Please try again"); 
                    throw new FileNotFoundException();
                } else{
                    from = new File(path);
                    prop.setProperty("source_path", path);
                    FileWriter writer = new FileWriter(propFileName);
                    prop.store(writer, "Source path updated");
                    System.out.println("Source folder set: " + path);
                 }   
     }
     public void setDestination(String path, String extension) throws FileNotFoundException, IOException{
         prop.load(reader);
         var keyset = prop.keySet();
         reader.close();
         if(!keyset.contains(extension)){       
            if (new File(path).exists()) {
                       System.out.println("Folder not found. Please try again");
                       throw new FileNotFoundException();
                   } else{
                       prop.setProperty(extension, path);
                       var writer = new FileWriter(propFileName);
                       prop.store(writer, "new destination folder");
                       writer.close();
                       System.out.println("Destination folder set: " + path);
                   } 
         }else System.out.println("Path for " + extension + " already exists: " + prop.getProperty(extension));
     }
     public void printDestinations(){
         System.out.println("");
         prop.keySet().forEach(item -> {
             System.out.println(item + ": " + prop.get(item));
        });
         System.out.println("");
     }
     private String getFileExtension(String filename){
         int i = filename.lastIndexOf(".");
         if(i > 0){
             String extension = filename.substring(i + 1);
             System.out.println(extension);
             return extension;
         }
         else System.out.println("No extension");
         return "";
     }
}
