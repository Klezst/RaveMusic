/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bukkit.arthurh.RyveMusic;
import net.citizensnpcs.Citizens;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.entity.Player;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.material.MaterialData;
import org.getspout.spoutapi.player.SpoutPlayer;
import org.getspout.spoutapi.sound.SoundManager;
import org.getspout.spoutapi.packet.PacketItemName;
import org.bukkit.plugin.Plugin;
import org.bukkit.Bukkit;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.gmail.klezst.bukkit.ryvemusic.bukkitutil.compatibility.Permission;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
/**
 *
 * @author arthurhalet
 */
public class RyveMusic extends JavaPlugin {
    public boolean spoutEnable;
    public SoundManager sm = null;
    public Config config;
    public Config cd;
    public Config idTxt;
    public Config setting;
    public Config lang;
    public Config npcs;
    public boolean showListening = true;
    public short id;
    public Citizens citizen;
    public WorldGuardPlugin wgp;
    public HashMap<String, String> setJuke = new HashMap<String, String>();
    public RegionMusic regionMusic = new RegionMusic(this);
    public boolean isWorldGuardEnable = false;
    public boolean isIconomyEnable = false;
    public boolean isCitizenEnable = false;
    public Npc npc;
    private final RyveMusicPlayerListener playerListener = new RyveMusicPlayerListener(this);
    private final RyveMusicAudioListener audioListener = new RyveMusicAudioListener(this);
    private final RyveMusicBlockListener blockListener = new RyveMusicBlockListener(this);
    public MusicManager musicManager = new MusicManager(this);
    
    public void onDisable() {
        idTxt.removeProperty("id");
        idTxt.addProperty("id", ""+this.id);
        config.save();
        cd.save();
        setting.save();
        idTxt.save();
        lang.save();
        npcs.save();
    }

    public void onEnable() {
        try{
            File listMusicFile = new File(this.getDataFolder(), "URLs.txt");
                listMusicFile = listMusicFile.getAbsoluteFile();
                if (!listMusicFile.getParentFile().exists()) {
                        if (!listMusicFile.getParentFile().mkdirs()) {

                        }
                }

            if(!listMusicFile.exists()){
                listMusicFile.createNewFile();
            }
        }catch(Exception e){
            
        }
        HashMap<String, String> defaultSetting = new HashMap<String, String>();
            defaultSetting.put("ListMusic", "http://mine-elite.fr/musique/listmusic.php");
            defaultSetting.put("useURLs.txt", "false");
            defaultSetting.put("moneyName", "dollar");
            defaultSetting.put("onMove", "true");
            defaultSetting.put("randomMusic", "true");
            defaultSetting.put("loopInRegion", "true");
            defaultSetting.put("loopNpc", "true");
            defaultSetting.put("playMessage", "true");
        npcs = new Config(this.getDataFolder(), "NPCs.txt", null);
        npcs.load();
        
        setting = new Config(this.getDataFolder(), "setting.txt", defaultSetting);
        setting.load();
        
        HashMap<String, String> defaultLang = new HashMap<String, String>();
            defaultLang.put("permission", "You don't have the permission to use this command");
            defaultLang.put("validMusic", "This is not a valid music number !");
            defaultLang.put("musicList", "List of music:");
            defaultLang.put("listenWorld", "The world listen");
            defaultLang.put("stopMusic", "music is stopping !");
            defaultLang.put("existPlayer", "This player doesn't exist !");
            defaultLang.put("haveSpout", "This player doesn't have spout!");
            defaultLang.put("hitJukebox", "Hit a jukebox to put music !");
            defaultLang.put("activePlugin", "is not active !");
            defaultLang.put("regionExist", "This region doesn't exist !");
            defaultLang.put("removeMusic", "Music remove in the region");
            defaultLang.put("listening", "You are listening");
            defaultLang.put("removeJuke", "Jukebox remove !");
            defaultLang.put("secondRaw", "Second raw is not a number !");
            defaultLang.put("thirdRaw", "Third raw is not a number !");
            defaultLang.put("shopCreate", "Sign shop successfully create !");
            defaultLang.put("placeJuke", "Music successfully place !");
            defaultLang.put("sellCd", "We sell the cd");
            defaultLang.put("enoughMoney", "You don't have enough money!");
            defaultLang.put("loseMoney", "You lose");
            defaultLang.put("accountState", "Account state");
            defaultLang.put("receiveCd", "You received a cd wich contains");
            defaultLang.put("cdContains", "This cd contains");
            defaultLang.put("startMusic", "started for");
            defaultLang.put("musicTxt", "music");
            defaultLang.put("selectNpc", "You must select an Npc !");
            defaultLang.put("putRegion", "put into the region");
            defaultLang.put("npcPlay", "This NPC play:");
            defaultLang.put("npcDel", "Music remove from this NPC !");
        lang = new Config(this.getDataFolder(), "lang.txt", defaultLang);
        lang.load();
        
        config = new Config(this.getDataFolder(), "content.txt", null);
        config.load();
        
        cd = new Config(this.getDataFolder(), "cds.txt", null);
        cd.load();
        
        idTxt = new Config(this.getDataFolder(), "id.txt", null);
        idTxt.load();
        
        if(idTxt.getProperty("id") == null){
            idTxt.addProperty("id", "-32767");
        }
        id = Short.parseShort(idTxt.getProperty("id"));
        
        PluginManager pm = getServer().getPluginManager();
        Plugin spout = pm.getPlugin("Spout");
        
        Set set = this.cd.configMap.entrySet();
        Iterator it = set.iterator();
        MusicPlayer music;
        if(!Boolean.parseBoolean(setting.getProperty("useURLs.txt"))){
            music = new MusicPlayer(setting.getProperty("ListMusic"), null);
        }else{
            music = new MusicPlayer(setting.getProperty("ListMusic"), this.getDataFolder());
        }
        
        npc = new Npc(this);
        while(it.hasNext()){
            Map.Entry me = (Map.Entry)it.next(); 
            short idItem = Short.parseShort((String)me.getKey());
            String musicUrl = (String)me.getValue();
            SpoutManager.getMaterialManager().setItemName(MaterialData.goldMusicDisc, "Music Disc: " + music.parseTitle(musicUrl));
              for (Player p : this.getServer().getOnlinePlayers()) {
                SpoutPlayer allsp = (SpoutPlayer)p;
                allsp.sendPacket(new PacketItemName(MaterialData.goldMusicDisc.getRawId(), idItem, "Music Disc: " + music.parseTitle(musicUrl)));
              }
        }
        if(spout != null){
            this.spoutEnable = true;
            sm = SpoutManager.getSoundManager();
            
        }else{
            this.spoutEnable = false;
        }
        Plugin iconomy = getServer().getPluginManager().getPlugin("iConomy");
        // WorldGuard may not be loaded
        
        Plugin permissionsPlugin = pm.getPlugin("Permissions");
        Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");
        // WorldGuard may not be loaded
        if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
            System.out.println("WorldGuard non actif!");
        }else{
            this.isWorldGuardEnable = true;
            wgp = (WorldGuardPlugin) plugin;
        }
        
        plugin = getServer().getPluginManager().getPlugin("Citizens");
        // WorldGuard may not be loaded
        if (plugin == null || !(plugin instanceof Citizens)) {
            System.out.println("Citizens non actif!");
        }else{
            this.isCitizenEnable = true;
            this.citizen = (Citizens) plugin;
        }
        
        if(this.spoutEnable){
            pm.registerEvents(audioListener, this);
        }
        pm.registerEvents(playerListener, this);
        pm.registerEvents(blockListener, this);
        
        getCommand("rm").setExecutor(new Commands(this));
        // EXAMPLE: Custom code, here we just output some info so we can check all is well
        PluginDescriptionFile pdfFile = this.getDescription();
        System.out.println( pdfFile.getName() + " version " + pdfFile.getVersion() + " est active!" );
        
        
    }
    
    public boolean canPlayGlobal(Player player){
        if(player == null){
            return true;
        }
        return Permission.has(player,"RyveMusic.playGlobal");
        
    }
    
    public boolean canPlay(Player player){
        if(player == null){
            return true;
        }
        return Permission.has(player,"RyveMusic.play");
        
    }
    
    public boolean canList(Player player){
        if(player == null){
            return true;
        }
        return Permission.has(player,"RyveMusic.list");
        
    }
    
    public boolean canStop(Player player){
        if(player == null){
            return true;
        }
        return Permission.has(player,"RyveMusic.stop");
        
    }
    
    public boolean canStopGlobal(Player player){
        if(player == null){
            return true;
        }
        return Permission.has(player,"RyveMusic.stopGlobal");
        
    }
    
    public boolean canSetJuke(Player player){
        if(player == null){
            return true;
        }
        return Permission.has(player,"RyveMusic.setJuke");
        
    }
    
    public boolean canSetRegion(Player player){
        if(player == null){
            return true;
        }
        return Permission.has(player,"RyveMusic.setRegion");
        
    }
    
    public boolean canDelRegion(Player player){
        if(player == null){
            return true;
        }
        return Permission.has(player,"RyveMusic.delRegion");
        
    }
    
    public boolean canHaveShop(Player player){
        if(player == null){
            return true;
        }
        return Permission.has(player,"RyveMusic.haveShop");
        
    }
    
    public boolean canSetNpc(Player player){
        if(player == null){
            return true;
        }
        return Permission.has(player,"RyveMusic.setNpc");
        
    }
    
    public boolean canDelNpc(Player player){
        if(player == null){
            return true;
        }
        return Permission.has(player,"RyveMusic.delNpc");
        
    }
}
