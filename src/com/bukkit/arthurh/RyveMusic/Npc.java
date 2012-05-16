/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bukkit.arthurh.RyveMusic;
import java.util.*;

import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.resources.npclib.NPCManager;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.getspout.spoutapi.player.SpoutPlayer;
/**
 *
 * @author arthur
 */
public class Npc {
    public HashMap<String, Integer> selectNpc = new HashMap<String, Integer>();
    public HashMap<String, String> musicNpc;
    private HashMap<String, String> playerPlay = new HashMap<String, String>();
    public RyveMusic plugin;
    public Npc(RyveMusic plugin){
        this.musicNpc = plugin.npcs.configMap;
        this.plugin = plugin;
    }
    public void eventInteract(World world){
            Set set = musicNpc.entrySet();
            Iterator it = set.iterator();
            SpoutPlayer sp;
            while(it.hasNext()){
                Map.Entry me = (Map.Entry)it.next(); 
                String npcId = (String)me.getKey();
                String url = (String)me.getValue();
                HumanNPC npc = NPCManager.get(Integer.parseInt(npcId));
                if(npc != null){
                    Location npcLocation = npc.getLocation();
                    for(Player player : world.getPlayers()){
                        if (npcLocation.distance(player.getLocation())<=3){
                            try {
                                sp = (SpoutPlayer)player;
                                if(this.playerPlay.containsKey(sp.getName())){
                                    if(this.playerPlay.get(sp.getName()).equals(npcId)){
                                        return;
                                    }
                                }
                                this.playerPlay.put(sp.getName(), npcId);
                                plugin.showListening = false;
                                if(Boolean.parseBoolean(plugin.setting.getProperty(("loopNpc")))){
                                    plugin.musicManager.addLoop(sp, url);
                                }
                                plugin.sm.playCustomMusic(plugin, sp, url, true);
                            }catch (ClassCastException e){

                            }
                        }else if(this.playerPlay.containsKey(player.getName())) {
                            if(this.playerPlay.get(player.getName()).equals(npcId)){
                                try {
                                    sp = (SpoutPlayer)player;
                                    plugin.sm.stopMusic(sp, false, 2000);
                                    this.playerPlay.remove(player.getName());
                                    if(Boolean.parseBoolean(plugin.setting.getProperty(("loopNpc")))){
                                        plugin.musicManager.removeLoop(sp);
                                    }
                                }catch (ClassCastException e){

                                }
                            }
                        }
                    }
                }
            }
    }
    public void eventMove(Player player){
        Set set = musicNpc.entrySet();
        Iterator it = set.iterator();
        SpoutPlayer sp;
        while(it.hasNext()){
            Map.Entry me = (Map.Entry)it.next(); 
            String npcId = (String)me.getKey();
            String url = (String)me.getValue();
            HumanNPC npc = NPCManager.get(Integer.parseInt(npcId));
            if(npc != null){
                Location npcLocation = npc.getLocation();
                    if (npcLocation.distance(player.getLocation())<=3){
                        try {
                            sp = (SpoutPlayer)player;
                            if(this.playerPlay.containsKey(sp.getName())){
                                if(this.playerPlay.get(sp.getName()).equals(npcId)){
                                    return;
                                }
                            }
                            this.playerPlay.put(sp.getName(), npcId);
                            plugin.showListening = false;
                            plugin.sm.playCustomMusic(plugin, sp, url, true);
                            if(Boolean.parseBoolean(plugin.setting.getProperty(("loopNpc")))){
                                plugin.musicManager.addLoop(sp, url);
                            }
                        }catch (ClassCastException e){

                        }
                    }else if(this.playerPlay.containsKey(player.getName())) {
                        if(this.playerPlay.get(player.getName()).equals(npcId)){
                            try {
                                sp = (SpoutPlayer)player;
                                plugin.sm.stopMusic(sp, false, 2000);
                                this.playerPlay.remove(player.getName());
                                if(Boolean.parseBoolean(plugin.setting.getProperty(("loopNpc")))){
                                    plugin.musicManager.removeLoop(sp);
                                }
                            }catch (ClassCastException e){

                            }
                        }
                    }
                }
        }
    }
}
