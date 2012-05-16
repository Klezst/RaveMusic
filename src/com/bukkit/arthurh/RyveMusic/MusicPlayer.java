/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bukkit.arthurh.RyveMusic;
import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;
import java.net.URLDecoder;
import java.net.URLEncoder;
/**
 *
 * @author arthurhalet
 */
public class MusicPlayer {
    public ArrayList<String> listMusic;
    
    public MusicPlayer(String musicUrl, File fileFolder){
        listMusic = new ArrayList<String>();
        boolean eof = false;
        String raw;
        BufferedReader in;
        try {
            if(fileFolder == null){
                URL url = new URL(musicUrl);
                in = new BufferedReader(new InputStreamReader(url.openStream()));
            }else{
                File listMusicFile = new File(fileFolder, "URLs.txt");
                    listMusicFile = listMusicFile.getAbsoluteFile();
                    if (!listMusicFile.getParentFile().exists()) {
                            if (!listMusicFile.getParentFile().mkdirs()) {

                            }
                    }

                if(!listMusicFile.exists()){
                    listMusicFile.createNewFile();
                }
                FileReader fileReader = new FileReader(listMusicFile);
                in = new BufferedReader(fileReader);
            }
            while(!eof){
                raw = in.readLine();
                if(raw != null){
                    this.loadUrl(raw);
                }else{
                    eof = true;
                }
            }
        } catch(Exception e){
            return;
        }
    }
    public MusicPlayer(){}
    private void loadUrl(String strUrl){
        if(strUrl == null){
            return;
        }
        if(strUrl.equals("")){
            return;
        }
        try {

            listMusic.add(strUrl);
            
        }catch(Exception e){
            return;
        }

    }
    public double getDuration(String urlMusic){
        String raw;
        double getDuration = 0;
        BufferedReader in;
        try {
            URL url = new URL("http://mine-elite.fr/plugin/duration.php?url="+ URLEncoder.encode(urlMusic, "UTF8"));
            in = new BufferedReader(new InputStreamReader(url.openStream()));
            getDuration = Double.parseDouble(in.readLine());
        }catch(Exception e){
            
        }
        return getDuration;
    }
    public double getDuration(int index){
        return getDuration(getMusic(index));
    }
    public String randomMusic(){
        int random = (int)(Math.random()*listMusic.size());
        return listMusic.get(random);
    }
    public String parseTitle(String strUrl){
        try {
                FileName fileName = new FileName(strUrl, '/', '.');
                
                return URLDecoder.decode(fileName.filename(), "UTF8");
            }catch(Exception e){
                return null;
            }
    }
    public String getMusic(int index){
        if(index<=0 || index>this.listMusic.size()){
            return "bad musique";
        }
        return this.listMusic.get(index-1);
    }
    public void allMusic(Player player){
        if(player == null){
            return;
        }

        URL url;
        for(int i=0; i<this.listMusic.size(); i++){
            try {
                url = new URL(this.listMusic.get(i));
                int iPlus = i+1;
                
                String fileName = this.parseTitle(url.getFile());
                player.sendMessage(ChatColor.RED +""+ iPlus + ChatColor.WHITE +": "+ ChatColor.GREEN + fileName);
            }catch(Exception e){
                
            }
            
        }
    }
    
}

