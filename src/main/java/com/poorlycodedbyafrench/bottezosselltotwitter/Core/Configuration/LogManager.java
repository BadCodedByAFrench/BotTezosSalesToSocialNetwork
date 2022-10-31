/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.poorlycodedbyafrench.bottezosselltotwitter.Core.Configuration;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author david
 */
public class LogManager {
    
    private static LogManager logManager;
    
    private Logger fileLogger;
    
    public static LogManager getLogManager(){
        if (logManager == null){
            logManager = new LogManager();
        }
        
        return logManager;
    }
    
    private LogManager(){
        fileLogger = Logger.getLogger("Logger");
        FileHandler fh;

        try {  
            fh = new FileHandler("./BotTezosLog.log");
            SimpleFormatter formatter = new SimpleFormatter();  
            fh.setFormatter(formatter); 
            fileLogger.setUseParentHandlers(false);
            fileLogger.addHandler(fh);
        } catch (IOException ex) {
            Logger.getLogger(LogManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(LogManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void writeLog(String className, Exception ex){
        
        StringWriter sw = new StringWriter();
        ex.printStackTrace(new PrintWriter(sw));

        fileLogger.log(Level.SEVERE, className + "\n" + ex.getMessage() + "\n" + sw.toString());
    }
    
     public void writeLog(String className, String ex){
        

        fileLogger.log(Level.SEVERE, className + "\n" + ex + "\n");
    }
     
}
