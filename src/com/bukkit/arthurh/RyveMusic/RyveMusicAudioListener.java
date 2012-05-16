/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bukkit.arthurh.RyveMusic;

import org.getspout.spoutapi.event.sound.BackgroundMusicEvent;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
/**
 *
 * @author arthur
 */
public class RyveMusicAudioListener implements Listener{
    private RyveMusic plugin;
    public RyveMusicAudioListener(RyveMusic plugin){
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onBackgroundMusicChange(BackgroundMusicEvent event){
        if(plugin.showListening && Boolean.parseBoolean(plugin.setting.getProperty("playMessage"))){
            MusicPlayer music;
            if(!Boolean.parseBoolean(plugin.setting.getProperty("useURLs.txt"))){
                music = new MusicPlayer(plugin.setting.getProperty("ListMusic"), null);
            }else{
                music = new MusicPlayer(plugin.setting.getProperty("ListMusic"), plugin.getDataFolder());
            }
            if(event.getVolumePercent()>0){
                event.getTargetPlayer().sendMessage(ChatColor.GOLD +"[RyveMusic] "+ ChatColor.AQUA +plugin.lang.getProperty("listening") +" "+ ChatColor.GREEN +music.parseTitle(event.getMusicUrl())+ChatColor.AQUA+".");
            }
        }
        plugin.showListening = true;
    }
}
