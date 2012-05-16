/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bukkit.arthurh.RyveMusic;
import java.util.*;
import org.bukkit.entity.Player;
import org.getspout.spoutapi.player.SpoutPlayer;
import org.bukkit.World;
/**
 *
 * @author arthur
 */
public class RegionMusic {
    private RyveMusic plugin;
    private WorldGuard worldGuard;
    private HashMap<String, String> playerPlay = new HashMap<String, String>();
    private HashMap<String, String> inChildren = new HashMap<String, String>();
    public RegionMusic(RyveMusic plugin){
        this.plugin = plugin;
    }
    public void eventInteract(World world){
        List<Player> listPlayer = world.getPlayers();
        Player player;
        
        for(int i =0; i < listPlayer.size(); i++){
            player = listPlayer.get(i);
            this.event(player);
        }
    }
    public void eventMove(Player player){
        this.event(player);
    }
    private void event(Player player){
        SpoutPlayer sp;
        boolean playMusic;
        worldGuard = new WorldGuard(plugin, player.getWorld());
            Set set = plugin.config.configMap.entrySet();
            Iterator it = set.iterator();
            while(it.hasNext()){
                playMusic = true;
                Map.Entry me = (Map.Entry)it.next(); 
                String regionName = (String)me.getKey();
                String url = (String)me.getValue();
                if(worldGuard.existRegion(regionName)){
                    if(worldGuard.inRegion(player, regionName)){
                        try{
                            sp = (SpoutPlayer)player;
                            if(!sp.isSpoutCraftEnabled()){
                                return;
                            }
                            if(this.playerPlay.containsKey(sp.getName()) ){
                                if(this.playerPlay.get(sp.getName()).equals(regionName)){
                                    playMusic = false;
                                }
                            }
                            if(this.inChildren.containsKey(sp.getName())){
                                if(this.inChildren.get(sp.getName()).equals(regionName)){
                                    playMusic = false;
                                }
                            }
                            if(playMusic){
                                if(worldGuard.hasParent(regionName)){
                                    this.playerPlay.put(sp.getName(), worldGuard.getParent(regionName));
                                }else{
                                    this.playerPlay.put(sp.getName(), regionName);
                                }
                                plugin.sm.stopMusic(sp);
                                if(Boolean.parseBoolean(plugin.setting.getProperty(("loopInRegion")))){
                                    plugin.musicManager.addLoop(sp, url);
                                }
                                plugin.sm.playCustomMusic(plugin, sp, url, true);
                                
                            }
                            if(worldGuard.hasParent(regionName)){
                                this.inChildren.put(sp.getName(), regionName);
                                return;
                            }
                        }catch (ClassCastException e){
                            
                        }
                    }else if(this.inChildren.containsKey(player.getName())){
                        if(this.inChildren.get(player.getName()).equals(regionName)){
                            try {
                                sp = (SpoutPlayer)player;
                                if(Boolean.parseBoolean(plugin.setting.getProperty(("loopInRegion")))){
                                    plugin.musicManager.removeLoop(sp);
                                }
                                plugin.sm.stopMusic(sp, false, 2000);
                                this.playerPlay.remove(player.getName());
                                this.inChildren.remove(sp.getName());
                                
                            }catch (ClassCastException e){

                            }
                        }
                    }else if(this.playerPlay.containsKey(player.getName())) {
                        if(this.playerPlay.get(player.getName()).equals(regionName)){
                            try {
                                sp = (SpoutPlayer)player;
                                if(Boolean.parseBoolean(plugin.setting.getProperty(("loopInRegion")))){
                                    plugin.musicManager.removeLoop(sp);
                                }
                                plugin.sm.stopMusic(sp, false, 2000);
                                this.playerPlay.remove(player.getName());
                                
                            }catch (ClassCastException e){
                            
                            }
                        }
                    }
                }

            }
    }
}
