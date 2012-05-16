/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bukkit.arthurh.RyveMusic;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.entity.Player;
import org.bukkit.block.Block;
import org.bukkit.Material;
import org.bukkit.ChatColor;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.player.SpoutPlayer;
import java.util.*;
import org.bukkit.Location;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.material.MaterialData;
/**
 *
 * @author arthur
 */
public class RyveMusicBlockListener implements Listener{
    private RyveMusic plugin;
    private ArrayList<Location> onJukebox = new ArrayList<Location>();
    public RyveMusicBlockListener(RyveMusic plugin){
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        Block block = event.getBlock();
        SpoutPlayer sp = (SpoutPlayer) event.getPlayer(); 
        if(block.getType().equals(Material.JUKEBOX)){
            String location = block.getLocation().getBlockX()+","+block.getLocation().getBlockY()+","+block.getLocation().getBlockZ();
            String urlBlock = plugin.config.getProperty(location);
            if(urlBlock != null){
                plugin.config.removeProperty(location);
                event.getPlayer().sendMessage(ChatColor.GOLD +"[RyveMusic] "+ ChatColor.AQUA +plugin.lang.getProperty("removeJuke"));
                plugin.sm.stopMusic(sp);
            }
        }
    }
    
    @EventHandler
    public void onSignChange(SignChangeEvent event){
        Player player = event.getPlayer();
        String rm = event.getLine(0);
        
        if(!rm.equalsIgnoreCase("RyveMusic")){
            return ;
        }
        
        if(!plugin.isIconomyEnable){
            player.sendMessage(ChatColor.GOLD +"[RyveMusic] "+ ChatColor.AQUA +"iConomy "+ plugin.lang.getProperty("activePlugin"));
            event.setCancelled(true);
            return;
        }
        if(!plugin.canHaveShop(player)){
            player.sendMessage(ChatColor.GOLD +"[RyveMusic] "+ ChatColor.AQUA +plugin.lang.getProperty("permission"));
            event.setCancelled(true);
            return;
        }
        int musicId;
        int price;
         try {
            musicId = Integer.parseInt(event.getLine(1));
        } catch (NumberFormatException ex) {
            player.sendMessage(ChatColor.GOLD +"[RyveMusic] "+ ChatColor.AQUA +plugin.lang.getProperty("secondRaw"));
            return;
        }
        try {
         price = Integer.parseInt(event.getLine(2));
        }catch (NumberFormatException ex) {
            player.sendMessage(ChatColor.GOLD +"[RyveMusic] "+ ChatColor.AQUA +plugin.lang.getProperty("thirdRaw"));
            return;
        }
        if(!plugin.canHaveShop(player)){
            player.sendMessage(ChatColor.GOLD +"[RyveMusic] "+ ChatColor.AQUA +plugin.lang.getProperty("permission"));
            event.setCancelled(true);
            return ;
        }
        MusicPlayer music;
        if(!Boolean.parseBoolean(plugin.setting.getProperty("useURLs.txt"))){
                music = new MusicPlayer(plugin.setting.getProperty("ListMusic"), null);
            }else{
                music = new MusicPlayer(plugin.setting.getProperty("ListMusic"), plugin.getDataFolder());
            }
        String musicUrl = music.getMusic(musicId);
        if(musicUrl.equals("bad musique")){
            player.sendMessage(ChatColor.GOLD +"[RyveMusic] "+ ChatColor.AQUA +plugin.lang.getProperty("validMusic"));
            return;
        }
        event.setLine(0, ChatColor.GOLD+rm);
        player.sendMessage(ChatColor.GOLD +"[RyveMusic] "+ ChatColor.AQUA +plugin.lang.getProperty("shopCreate"));
        
    }
    
    @EventHandler
    public void onBlockRedstoneChange(BlockRedstoneEvent event){
	Set set = plugin.config.configMap.entrySet();
        Iterator it = set.iterator();
        Block block;
        while(it.hasNext()){
            Map.Entry me = (Map.Entry)it.next(); 
            String location = (String)me.getKey();
            String[] coordonate = location.split(",");
            if(coordonate.length==3){
                String url = (String)me.getValue();
                block = event.getBlock().getWorld().getBlockAt(Integer.parseInt(coordonate[0]), Integer.parseInt(coordonate[1]), Integer.parseInt(coordonate[2]));
                if (((block.isBlockPowered()) || (block.isBlockIndirectlyPowered())) && !onJukebox.contains(block.getLocation())){
                    MusicPlayer music;
                    if(!Boolean.parseBoolean(plugin.setting.getProperty("useURLs.txt"))){
                        music = new MusicPlayer(plugin.setting.getProperty("ListMusic"), null);
                    }else{
                        music = new MusicPlayer(plugin.setting.getProperty("ListMusic"), plugin.getDataFolder());
                    }
                    onJukebox.add(block.getLocation());
                    plugin.sm.playGlobalCustomSoundEffect(plugin, url, true, block.getLocation());
                }else if (((!block.isBlockPowered()) && !(block.isBlockIndirectlyPowered())) && onJukebox.contains(block.getLocation())){
                    onJukebox.remove(block.getLocation());
                }
            }
        }
    }
}
