/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bukkit.arthurh.RyveMusic;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.resources.npclib.NPCManager;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.entity.Player;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import com.gmail.klezst.bukkit.ryvemusic.bukkitutil.compatibility.Economy;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.packet.PacketItemName;
import org.bukkit.ChatColor;
import org.bukkit.event.block.Action;
import org.getspout.spoutapi.player.SpoutPlayer;
import org.bukkit.block.Sign;
import org.bukkit.craftbukkit.block.CraftSign;
/**
 *
 * @author arthurhalet
 */
public class RyveMusicPlayerListener implements Listener{
    private final RyveMusic plugin;
    public RyveMusicPlayerListener(RyveMusic plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event){
        if(this.plugin.isCitizenEnable && !Boolean.parseBoolean(this.plugin.setting.getProperty("onMove"))){
            this.plugin.npc.eventInteract(event.getPlayer().getWorld());
        }
        if(this.plugin.isWorldGuardEnable && !Boolean.parseBoolean(this.plugin.setting.getProperty("onMove"))){
            plugin.regionMusic.eventInteract(event.getPlayer().getWorld());
        }
        SpoutPlayer sp = (SpoutPlayer) event.getPlayer(); 
        if(!sp.isSpoutCraftEnabled()){
            return;
        }
        int random = (int)(Math.random()*800);
        if(random==92 && Boolean.parseBoolean(this.plugin.setting.getProperty("randomMusic"))){
            MusicPlayer music;
            if(!Boolean.parseBoolean(plugin.setting.getProperty("useURLs.txt"))){
                music = new MusicPlayer(plugin.setting.getProperty("ListMusic"), null);
            }else{
                music = new MusicPlayer(plugin.setting.getProperty("ListMusic"), plugin.getDataFolder());
            }
            String url = music.randomMusic();
            plugin.sm.playCustomMusic(plugin, sp, url, true);
        }
        Block block = event.getClickedBlock();
        ItemStack item = event.getItem();
        if(item != null && block != null){
            if((item.getType().equals(Material.GOLD_RECORD) || item.getType().equals(Material.GREEN_RECORD)) && block.getType().equals(Material.JUKEBOX) && event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
                MusicPlayer music;
                if(!Boolean.parseBoolean(plugin.setting.getProperty("useURLs.txt"))){
                    music = new MusicPlayer(plugin.setting.getProperty("ListMusic"), null);
                }else{
                    music = new MusicPlayer(plugin.setting.getProperty("ListMusic"), plugin.getDataFolder());
                }
                String urlBlock;
                urlBlock = plugin.cd.getProperty(""+item.getDurability());
                if(urlBlock == null){
                    urlBlock = plugin.config.getProperty(block.getLocation().getBlockX()+","+block.getLocation().getBlockY()+","+block.getLocation().getBlockZ());
                }
                if(urlBlock == null){
                    urlBlock = music.randomMusic();
                }
                plugin.sm.playGlobalCustomSoundEffect(plugin, urlBlock, true, block.getLocation());
                event.setCancelled(true);
            }
        }
        if( block != null){
            if(block.getType().equals(Material.JUKEBOX) && plugin.setJuke.containsKey(sp.getName())){
                plugin.config.addProperty(block.getLocation().getBlockX()+","+block.getLocation().getBlockY()+","+block.getLocation().getBlockZ(), plugin.setJuke.get(sp.getName()));
                plugin.sm.playGlobalCustomSoundEffect(plugin, plugin.setJuke.get(sp.getName()), true, block.getLocation());
                sp.sendMessage(ChatColor.GOLD +"[RyveMusic] "+ ChatColor.AQUA +plugin.lang.getProperty("placeJuke"));
                MusicPlayer music;
                if(!Boolean.parseBoolean(plugin.setting.getProperty("useURLs.txt"))){
                    music = new MusicPlayer(plugin.setting.getProperty("ListMusic"), null);
                }else{
                    music = new MusicPlayer(plugin.setting.getProperty("ListMusic"), plugin.getDataFolder());
                }
            }else if(block.getType().equals(Material.JUKEBOX)){
                String urlBlock = plugin.config.getProperty(block.getLocation().getBlockX()+","+block.getLocation().getBlockY()+","+block.getLocation().getBlockZ());
                if(urlBlock == null){
                    return;
                }
                MusicPlayer music;
                if(!Boolean.parseBoolean(plugin.setting.getProperty("useURLs.txt"))){
                    music = new MusicPlayer(plugin.setting.getProperty("ListMusic"), null);
                }else{
                    music = new MusicPlayer(plugin.setting.getProperty("ListMusic"), plugin.getDataFolder());
                }
                
                plugin.sm.playGlobalCustomSoundEffect(plugin, urlBlock, true, block.getLocation());
            }
        }
        if(plugin.setJuke.containsKey(sp.getName())){
            plugin.setJuke.remove(sp.getName());
        }        
        
        if(event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.LEFT_CLICK_BLOCK){
            return;
        }
        if(event.getClickedBlock().getType() != Material.WALL_SIGN && event.getClickedBlock().getType() != Material.SIGN_POST){
            return;
        }
        Sign sign = new CraftSign(event.getClickedBlock());
        if(event.getAction() == Action.LEFT_CLICK_BLOCK){
            String rm = sign.getLine(0);
            if(!rm.equalsIgnoreCase(ChatColor.GOLD+"RyveMusic")){
                return ;
            }
            MusicPlayer music;
            if(!Boolean.parseBoolean(plugin.setting.getProperty("useURLs.txt"))){
                music = new MusicPlayer(plugin.setting.getProperty("ListMusic"), null);
            }else{
                music = new MusicPlayer(plugin.setting.getProperty("ListMusic"), plugin.getDataFolder());
            }
            int musicId = Integer.parseInt(sign.getLine(1));
            String musicUrl = music.getMusic(musicId);
            if(musicUrl.equals("bad musique")){
                sp.sendMessage(ChatColor.GOLD +"[RyveMusic] "+ ChatColor.AQUA +plugin.lang.getProperty("validMusic"));
                return;
            }
            sp.sendMessage(ChatColor.GOLD +"[RyveMusic] "+ ChatColor.AQUA +plugin.lang.getProperty("sellCd")+" "+ChatColor.GREEN+ music.parseTitle(musicUrl) + ChatColor.AQUA +" !");
        }
        
        
        String rm = sign.getLine(0);
        if(!rm.equalsIgnoreCase(ChatColor.GOLD+"RyveMusic")){
            return ;
        }
        if(event.getAction() != Action.RIGHT_CLICK_BLOCK){
            return;
        }
        int price = Integer.parseInt(sign.getLine(2));
        int musicId = Integer.parseInt(sign.getLine(1));
        MusicPlayer music;
        if(!Boolean.parseBoolean(plugin.setting.getProperty("useURLs.txt"))){
            music = new MusicPlayer(plugin.setting.getProperty("ListMusic"), null);
        }else{
            music = new MusicPlayer(plugin.setting.getProperty("ListMusic"), plugin.getDataFolder());
        }
        String musicUrl = music.getMusic(musicId);
        if(musicUrl.equals("bad musique")){
            sp.sendMessage(ChatColor.GOLD +"[RyveMusic] "+ ChatColor.AQUA +plugin.lang.getProperty("validMusic"));
            return;
        }
        if(!(Economy.getBalance(sp.getName()) >= price)){
            sp.sendMessage(ChatColor.GOLD +"[RyveMusic] "+ ChatColor.AQUA +plugin.lang.getProperty("enoughMoney"));
            return;
        }
        Economy.deltaBalance(-price, sp.getName());
        sp.sendMessage(ChatColor.GOLD +"[RyveMusic] "+ ChatColor.AQUA +plugin.lang.getProperty("loseMoney") + " "+ price +" "+ plugin.setting.getProperty("moneyName") +" !");
        sp.sendMessage(ChatColor.GOLD +"[RyveMusic] "+ ChatColor.AQUA +plugin.lang.getProperty("accountState") +": "+ Economy.getBalance(sp.getName()) +" "+ plugin.setting.getProperty("moneyName")+" !");
        ItemStack cd = new ItemStack(2256);
        
        plugin.id++;
        if(plugin.id==0){
            plugin.id++;
        }
        if(plugin.id >=32767){
            plugin.id = -32767;
        }
        
        cd.setAmount(1);
        cd.setDurability(plugin.id);
        plugin.cd.removeProperty(""+plugin.id);
        plugin.cd.addProperty(""+plugin.id, musicUrl);
        // TODO: SpoutManager.getMaterialManager().setItemName(Material.GOLD_RECORD, "Music Disc: " + music.parseTitle(musicUrl));
          for (Player p : plugin.getServer().getOnlinePlayers()) {
            SpoutPlayer allsp = (SpoutPlayer)p;
            allsp.sendPacket(new PacketItemName(Material.GOLD_RECORD.getId(), plugin.id, "Music Disc: " + music.parseTitle(musicUrl)));
          }
        sp.getInventory().addItem(cd);
        
        sp.updateInventory();
        sp.sendMessage(ChatColor.GOLD +"[RyveMusic] "+ ChatColor.AQUA +plugin.lang.getProperty("receiveCd") +" "+ChatColor.GREEN+ music.parseTitle(musicUrl) + ChatColor.AQUA +" !");
    }
    
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event){
        
        if(this.plugin.isCitizenEnable && Boolean.parseBoolean(this.plugin.setting.getProperty("onMove"))){
            this.plugin.npc.eventMove(event.getPlayer());
        }
        if(this.plugin.isWorldGuardEnable && Boolean.parseBoolean(this.plugin.setting.getProperty("onMove"))){
            plugin.regionMusic.eventMove(event.getPlayer());
        }
        try{
            SpoutPlayer sp = (SpoutPlayer)event.getPlayer();
            plugin.musicManager.loopMusic(sp);
        }catch (ClassCastException e){
                            
        }
    }
    
   @EventHandler
   public void onItemHeldChange(PlayerItemHeldEvent event){
    ItemStack item = event.getPlayer().getInventory().getItem(event.getNewSlot());
    if ((item != null) && (item.getType() == Material.GOLD_RECORD) && (item.getDurability() != 0)) {
      String cd = plugin.cd.getProperty(""+ item.getDurability());
      MusicPlayer music;
      if(!Boolean.parseBoolean(plugin.setting.getProperty("useURLs.txt"))){
            music = new MusicPlayer(plugin.setting.getProperty("ListMusic"), null);
        }else{
            music = new MusicPlayer(plugin.setting.getProperty("ListMusic"), plugin.getDataFolder());
        }
      if (cd != null){
        event.getPlayer().sendMessage(ChatColor.GOLD +"[RyveMusic] "+ ChatColor.AQUA +plugin.lang.getProperty("cdContains") +": "+ChatColor.GREEN+ music.parseTitle(cd) + ChatColor.AQUA +" !");
      }
    }
  }
  
  @EventHandler
  public void onPlayerInteractEntity(PlayerInteractEntityEvent event)
  {
    if(this.plugin.isCitizenEnable){
        HumanNPC npc = NPCManager.get(event.getRightClicked());
        if (npc != null) {
            plugin.npc.selectNpc.put(event.getPlayer().getName(), npc.getUID());
        }
    }
  }
   
}
