/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bukkit.arthurh.RyveMusic;

import net.citizensnpcs.api.event.NPCTargetEvent;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.resources.npclib.NPCManager;
import net.citizensnpcs.resources.npclib.creatures.CreatureTask;

import org.bukkit.entity.Player;
import org.getspout.spoutapi.player.SpoutPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;
/**
 *
 * @author arthur
 */
public class RyveMusicEntityListener implements Listener {
    private RyveMusic plugin;
    public RyveMusicEntityListener(RyveMusic plugin){
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onEntityTarget(EntityTargetEvent event)
    {
        System.out.println("Tets");
        if(plugin.isCitizenEnable){
            if (!(event instanceof NPCTargetEvent)) {
                return;
            }
            if (CreatureTask.getCreature(event.getEntity()) != null) {
              CreatureTask.getCreature(event.getEntity()).onRightClick(
                (Player)event.getTarget());
            }
            if (NPCManager.isNPC(event.getTarget())) {
        	HumanNPC npc = NPCManager.get(event.getTarget());
        	if (npc.types().size() > 0) {
        	    npc.callTargetEvent(event);
        	    event.setCancelled(true);
        	}
            }
            NPCTargetEvent e = (NPCTargetEvent)event;
            HumanNPC npc = NPCManager.get(e.getEntity());
            if ((npc != null) && ((event.getTarget() instanceof Player))) {
                if(plugin.npcs.getProperty(""+npc.getUID()) != null){
                    SpoutPlayer sp = (SpoutPlayer)event.getTarget();
                    plugin.showListening = false;
                    plugin.sm.playCustomMusic(plugin, sp, plugin.npcs.getProperty(""+npc.getUID()), true);

                }
            }
        }
    }
}
