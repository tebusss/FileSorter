/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.filesorter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ville Terasahde
 */
public class ListenerApp {
    private final Scanner scanner;
    private final Listen listen;
    
    public ListenerApp(Scanner s, Listen l){
        this.scanner = s;
        this.listen = l;
    }
    
    public void runShit() throws IOException{
        System.out.println("Folder Listener");
        System.out.println("");
        openCommandLine();
        listen.configuratePaths();
        System.out.println("All set..");
        System.out.println("");
        printCommands();
    }

    private void openCommandLine(){

    }
    private void printCommands(){
        while(true){
        System.out.println("(1) Fire up");
        System.out.println("(2) Set Source Folder");
        System.out.println("(3) Set Destination Folder");
        System.out.println("(4) List all destination folders and extensions");
        System.out.println("(E) Exit");
        
        String command = scanner.nextLine();  
            switch(command){
                case "1" -> listen.start();
                case "2" -> setSourceFolder();
                case "3" -> setDestinationFolder();
                case "4" -> listen.printDestinations();
                case "E","e" -> {
                    System.out.println("Exiting..");
                    System.exit(0);
                    return;
                }
                default -> {
                    System.out.println("Command " + command + " not found");
                    System.out.println("");
                }
            }
        }
    }
    private void setSourceFolder(){
        while(true){
            System.out.println("(B) Back");
            System.out.println("Source folder path: ");
            String path = scanner.nextLine();
            if(path.equals("b") || path.equals("B")){
                return;
            } else{
                try {
                    listen.setSource(path);
                } catch (IOException ex) {
                    Logger.getLogger(ListenerApp.class.getName()).log(Level.SEVERE, null, ex);
                }
                return;
            }
            }
    }
    private void setDestinationFolder(){
        while(true){
            System.out.println("What file extension should be stored?");
            String extension = scanner.nextLine();
            if(extension.startsWith(".")){
                extension = extension.substring(1); // let's drop the DOT
            }
            String path = askPath();     
            if(path.equals("b") || path.equals("B")){
                return;
            } else{ 
                try {
                    listen.setDestination(path, extension);
                } catch (FileNotFoundException ex) {
                    
                } catch (IOException ex) {
                    Logger.getLogger(ListenerApp.class.getName()).log(Level.SEVERE, null, ex);
                }
                return;
            }
            }
    }
    private String askPath(){
        System.out.println("(B) Back");
            System.out.println("Destination folder path: ");
            String path = scanner.nextLine();
            return path;
    }   
}
