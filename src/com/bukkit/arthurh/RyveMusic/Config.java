/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bukkit.arthurh.RyveMusic;
import java.beans.Beans;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.filechooser.FileFilter;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.Set;
import java.util.Iterator;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author arthur
 */
public class Config {
    private File config;
    private File fileFolder;
    public HashMap<String, String> configMap = new HashMap<String, String>();
    private HashMap<String, String> defaultMap = new HashMap<String, String>();
    public Config(File fileFolder, String fileName, HashMap<String, String> defaultMap){
         this.config = new File(fileFolder, fileName);
         this.fileFolder = fileFolder;
         if(defaultMap != null){
             this.defaultMap = defaultMap;
         }
    }
    public void load(){
        String raw;
        String[] split = new String[2];
        boolean eof = false;
        try{
            this.config = this.config.getAbsoluteFile();
                if (!config.getParentFile().exists()) {
                        if (!config.getParentFile().mkdirs()) {
                               
                        }
                }

            if(!this.config.exists()){
                this.config.createNewFile();
                
                FileWriter fileWriter = new FileWriter(this.config);
                this.configMap = this.defaultMap;
                Set set = this.configMap.entrySet();
                Iterator it = set.iterator();
                while(it.hasNext()){
                    Map.Entry me = (Map.Entry)it.next(); 
                    fileWriter.write((String)me.getKey() +"="+ (String)me.getValue() +"\n");
                    
                }
                fileWriter.close();
            }else{
                FileReader fileReader = new FileReader(this.config);
                BufferedReader buff = new BufferedReader(fileReader);
                String value;
                while(!eof){
                    raw = buff.readLine();
                    if(raw != null){
                        split = raw.split("=");
                        if(split.length<2){
                            value = "";
                        }else{
                            value = split[1];
                        }
                        this.configMap.put(split[0], value);
                    }else{
                        eof = true;
                    }
                }
                
            }
        }catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
			// TODO Auto-generated catch block
		e.printStackTrace();
	}
        
    }
    public String getProperty(String property){
        if(this.configMap.containsKey(property)){
            return this.configMap.get(property);
        }else if (this.defaultMap.containsKey(property)) {
            try{
                String raw;
                
                boolean eof = false;
                FileReader fileReader = new FileReader(this.config);
                BufferedReader buff = new BufferedReader(fileReader);
               
                String value;
                String txt = "";
                while(!eof){
                    raw = buff.readLine();
                    if(raw != null){
                        txt += raw +"\n";
                    }else{
                        eof = true;
                    }
                }
                FileWriter fileWriter = new FileWriter(this.config);
                fileWriter.write(txt + property +"="+ this.defaultMap.get(property) +"\n");
                fileWriter.close();
            }catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
            } catch (IOException e) {
                            // TODO Auto-generated catch block
                    e.printStackTrace();
            }
            
            return this.defaultMap.get(property);
        }else{
            return null;
        }
    }
    public void reload(){
        configMap = new HashMap<String, String>();
        this.load();
    }
    public void addProperty(String property, String value){
        this.configMap.put(property, value);
    }
    public void removeProperty(String property){
        this.configMap.remove(property);
    }
    public void save(){
        try{
            FileWriter fileWriter = new FileWriter(this.config);
            Set set = this.configMap.entrySet();
            Iterator it = set.iterator();
            while(it.hasNext()){
                Map.Entry me = (Map.Entry)it.next(); 
                fileWriter.write((String)me.getKey() +"="+ (String)me.getValue() +"\n");

            }
            fileWriter.close();
        }catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
        } catch (IOException e) {
                        // TODO Auto-generated catch block
                e.printStackTrace();
        }
    }
}
