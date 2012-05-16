/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bukkit.arthurh.RyveMusic;
import org.getspout.spoutapi.player.SpoutPlayer;
import java.util.*;
/**
 *
 * @author arthur
 */
public class MusicManager {
    private MusicPlayer musicPlayer = new MusicPlayer();
    private RyveMusic plugin;
    private HashMap<String, String> onAir = new HashMap<String, String>();
    private HashMap<String, Double> toLoop = new HashMap<String, Double>();
    public MusicManager(RyveMusic plugin){
        this.plugin = plugin;
    }
    public void addLoop(SpoutPlayer player, String musicUrl){
        try{
            double duration = musicPlayer.getDuration(musicUrl);
            if(duration>0){
                onAir.put(player.getName(), musicUrl);
                toLoop.put(player.getName(), System.currentTimeMillis()+duration);
            }
        }catch(Exception e){
            
        }
    }
    public void removeLoop(SpoutPlayer player){
        if(onAir.containsKey(player.getName()) && toLoop.containsKey(player.getName())){
            onAir.remove(player.getName());
            toLoop.remove(player.getName());
        }
    }
    public void loopMusic(SpoutPlayer player){
        if(onAir.containsKey(player.getName()) && toLoop.containsKey(player.getName())){
            if(System.currentTimeMillis()>toLoop.get(player.getName())){
                plugin.showListening = false;
                plugin.sm.playCustomMusic(plugin, player, onAir.get(player.getName()), true);
                double duration = musicPlayer.getDuration(onAir.get(player.getName()));
                toLoop.put(player.getName(), System.currentTimeMillis()+duration);
                
            }
        }
    }
    
}
