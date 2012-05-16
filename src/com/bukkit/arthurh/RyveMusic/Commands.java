/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bukkit.arthurh.RyveMusic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.World;
import org.getspout.spoutapi.Spout;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.player.SpoutPlayer;
import org.bukkit.ChatColor;
/**
 *
 * @author arthurhalet
 */
public class Commands implements CommandExecutor{
    private final RyveMusic plugin;
    private MusicPlayer music;
    
    public Commands(RyveMusic plugin) {
            this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command,String commandLabel, String[] args) {
            String commandName = command.getName().toLowerCase();
            Player player;
            if (sender instanceof Player) {
                player = (Player)sender;
            }else{
                return true;
            }


            if(!Boolean.parseBoolean(plugin.setting.getProperty("useURLs.txt"))){
                music = new MusicPlayer(plugin.setting.getProperty("ListMusic"), null);
            }else{
                music = new MusicPlayer(plugin.setting.getProperty("ListMusic"), plugin.getDataFolder());
            }
            if (commandName.equalsIgnoreCase("rm")) {
                    return performRm(args, player);
            }

            return true;
    }
    
    public boolean performRm(String[] args, Player sender){
        String command;
        if (args.length < 1) {
            sender.sendMessage("/rm <global:list:play:stop:setJuke:setRegion:delRegion:setNpc:delNpc:reload> [next argument]!");
            return true;
        }else{
            command = args[0];
        }
        if(command.equalsIgnoreCase("global")){
            if(!plugin.canPlayGlobal(sender)){
                sender.sendMessage(ChatColor.GOLD +"[RyveMusic] "+ ChatColor.AQUA +plugin.lang.getProperty("permission"));
                return true;
            }
            if (args.length < 2) {
                sender.sendMessage(ChatColor.GOLD +"[RyveMusic] "+ ChatColor.AQUA +"/rm global <music number>!");
                return true;
            }
            String musicUrl = music.getMusic(Integer.parseInt(args[1]));
            if(musicUrl.equals("bad musique")){
                sender.sendMessage(ChatColor.GOLD +"[RyveMusic] "+ ChatColor.AQUA +plugin.lang.getProperty("validMusic"));
                return true;
            }
            plugin.sm.playGlobalCustomMusic(plugin, musicUrl, true);
            plugin.getServer().broadcastMessage(ChatColor.GOLD +"[RyveMusic] "+ ChatColor.AQUA +plugin.lang.getProperty("listenWorld")+" "+ ChatColor.GREEN +music.parseTitle(musicUrl)+ChatColor.AQUA+".");
            return true;
            
        }else if(command.equalsIgnoreCase("list")){
            if(!plugin.canList(sender)){
                sender.sendMessage(ChatColor.GOLD +"[RyveMusic] "+ ChatColor.AQUA +plugin.lang.getProperty("permission"));
                return true;
            }
            sender.sendMessage(ChatColor.GOLD +"[RyveMusic] "+ ChatColor.AQUA + plugin.lang.getProperty("musicList"));
            music.allMusic(sender);

        }else if(command.equalsIgnoreCase("stop")){
            if(!plugin.canStop(sender)){
                sender.sendMessage(ChatColor.GOLD +"[RyveMusic] "+ ChatColor.AQUA +plugin.lang.getProperty("permission"));
                return true;
            }
            SpoutPlayer sp = (SpoutPlayer)sender;
            plugin.sm.stopMusic(sp, true);
            plugin.musicManager.removeLoop(sp);
            sender.sendMessage(ChatColor.GOLD +"[RyveMusic] "+ ChatColor.AQUA + plugin.lang.getProperty("stopMusic"));

        }else if(command.equalsIgnoreCase("play")){
            if(!plugin.canPlay(sender)){
                sender.sendMessage(ChatColor.GOLD +"[RyveMusic] "+ ChatColor.AQUA +plugin.lang.getProperty("permission"));
                return true;
            }
            if (args.length < 2) {
                sender.sendMessage(ChatColor.GOLD +"[RyveMusic] "+ ChatColor.AQUA +"/rm play <music number> [player]!");
                return true;
            }
            String musicUrl = music.getMusic(Integer.parseInt(args[1]));
            if(musicUrl.equals("bad musique")){
                sender.sendMessage(ChatColor.GOLD +"[RyveMusic] "+ ChatColor.AQUA + plugin.lang.getProperty("validMusic"));
                return true;
            }
            Player player;
            if(args.length > 2){
                player = plugin.getServer().getPlayer(args[2]);
                if(player == null){
                    sender.sendMessage(ChatColor.GOLD +"[RyveMusic] "+ ChatColor.AQUA +plugin.lang.getProperty("existPlayer"));
                    return true;
                }
            }else{
                player = sender;
            }
            SpoutPlayer sp = (SpoutPlayer)player;

            
            plugin.sm.playCustomMusic(plugin, sp, musicUrl, true);
            sender.sendMessage(ChatColor.GOLD +"[RyveMusic] "+ ChatColor.AQUA + plugin.lang.getProperty("musicTxt") +" "+ ChatColor.GREEN +music.parseTitle(musicUrl)+ChatColor.AQUA+" "+ plugin.lang.getProperty("startMusic") +" "+ sp.getName() +" !");

        }else if(command.equalsIgnoreCase("playLoop")){
            if(!plugin.canPlay(sender)){
                sender.sendMessage(ChatColor.GOLD +"[RyveMusic] "+ ChatColor.AQUA +plugin.lang.getProperty("permission"));
                return true;
            }
            if (args.length < 2) {
                sender.sendMessage(ChatColor.GOLD +"[RyveMusic] "+ ChatColor.AQUA +"/rm playLoop <music number> [player]!");
                return true;
            }
            String musicUrl = music.getMusic(Integer.parseInt(args[1]));
            if(musicUrl.equals("bad musique")){
                sender.sendMessage(ChatColor.GOLD +"[RyveMusic] "+ ChatColor.AQUA + plugin.lang.getProperty("validMusic"));
                return true;
            }
            Player player;
            if(args.length > 2){
                player = plugin.getServer().getPlayer(args[2]);
                if(player == null){
                    sender.sendMessage(ChatColor.GOLD +"[RyveMusic] "+ ChatColor.AQUA +plugin.lang.getProperty("existPlayer"));
                    return true;
                }
            }else{
                player = sender;
            }
            SpoutPlayer sp = (SpoutPlayer)player;

            plugin.musicManager.addLoop(sp, musicUrl);
            plugin.sm.playCustomMusic(plugin, sp, musicUrl, true);
            sender.sendMessage(ChatColor.GOLD +"[RyveMusic] "+ ChatColor.AQUA + plugin.lang.getProperty("musicTxt") +" "+ ChatColor.GREEN +music.parseTitle(musicUrl)+ChatColor.AQUA+" "+ plugin.lang.getProperty("startMusic") +" "+ sp.getName() +" !");

        }else if(command.equalsIgnoreCase("setJuke")){
            if(!plugin.canSetJuke(sender)){
                sender.sendMessage(ChatColor.GOLD +"[RyveMusic] "+ ChatColor.AQUA +plugin.lang.getProperty("permission"));
                return true;
            }
            if (args.length < 2) {
                sender.sendMessage(ChatColor.GOLD +"[RyveMusic] "+ ChatColor.AQUA +"/rm setJuke <music number>!");
                return true;
            }
            String musicUrl = music.getMusic(Integer.parseInt(args[1]));
            if(musicUrl.equals("bad musique")){
                sender.sendMessage(ChatColor.GOLD +"[RyveMusic] "+ ChatColor.AQUA +plugin.lang.getProperty("validMusic"));
                return true;
            }
            plugin.setJuke.put(sender.getName(), musicUrl);
            sender.sendMessage(ChatColor.GOLD +"[RyveMusic] "+ ChatColor.AQUA +plugin.lang.getProperty("hitJukebox"));
            return true;
            
        }else if(command.equalsIgnoreCase("setRegion")){
            if(!plugin.isWorldGuardEnable){
                sender.sendMessage(ChatColor.GOLD +"[RyveMusic] "+ ChatColor.AQUA +"WorldGuard "+ plugin.lang.getProperty("activePlugin"));
                return true;
            }
            if(!plugin.canSetRegion(sender)){
                sender.sendMessage(ChatColor.GOLD +"[RyveMusic] "+ ChatColor.AQUA +plugin.lang.getProperty("permission"));
                return true;
            }
            if (args.length < 3) {
                sender.sendMessage(ChatColor.GOLD +"[RyveMusic] "+ ChatColor.AQUA +"/rm setRegion <music number> <region name>!");
                return true;
            }
            String region = args[2];
            String musicUrl = music.getMusic(Integer.parseInt(args[1]));
            if(musicUrl.equals("bad musique")){
                sender.sendMessage(ChatColor.GOLD +"[RyveMusic] "+ ChatColor.AQUA +plugin.lang.getProperty("validMusic"));
                return true;
            }
            WorldGuard worldGuard = new WorldGuard(plugin, sender.getWorld());
            if(!worldGuard.existRegion(region)){
                sender.sendMessage(ChatColor.GOLD +"[RyveMusic] "+ ChatColor.AQUA +plugin.lang.getProperty("regionExist"));
                return true;
            }
            plugin.config.addProperty(region, musicUrl);
            sender.sendMessage(ChatColor.GOLD +"[RyveMusic]"+ ChatColor.AQUA +" Music "+ ChatColor.GREEN + music.parseTitle(musicUrl) + ChatColor.AQUA +" " +plugin.lang.getProperty("putRegion")+" "+ ChatColor.DARK_RED + region + ChatColor.AQUA +".");
            return true;
            
        }else if(command.equalsIgnoreCase("delRegion")){
            if(!plugin.isWorldGuardEnable){
                sender.sendMessage(ChatColor.GOLD +"[RyveMusic] "+ ChatColor.AQUA +"WorldGuard "+ plugin.lang.getProperty("activePlugin"));
                return true;
            }
            if(!plugin.canDelRegion(sender)){
                sender.sendMessage(ChatColor.GOLD +"[RyveMusic] "+ ChatColor.AQUA +plugin.lang.getProperty("permission"));
                return true;
            }
            if (args.length < 2) {
                sender.sendMessage(ChatColor.GOLD +"[RyveMusic] "+ ChatColor.AQUA +"/rm delRegion <region name>!");
                return true;
            }
            String region = args[1];
            WorldGuard worldGuard = new WorldGuard(plugin, sender.getWorld());
            if(!worldGuard.existRegion(region)){
                sender.sendMessage(ChatColor.GOLD +"[RyveMusic] "+ ChatColor.AQUA +plugin.lang.getProperty("regionExist"));
                return true;
            }
            plugin.config.removeProperty(region);
            sender.sendMessage(ChatColor.GOLD +"[RyveMusic] "+ ChatColor.AQUA +plugin.lang.getProperty("removeMusic") +" "+ ChatColor.DARK_RED + region + ChatColor.AQUA +".");
            return true;
            
        }else if(command.equalsIgnoreCase("setNpc")){
            if(!plugin.isCitizenEnable){
                sender.sendMessage(ChatColor.GOLD +"[RyveMusic] "+ ChatColor.AQUA +"Citizen "+ plugin.lang.getProperty("activePlugin"));
                return true;
            }
            if(!plugin.canSetNpc(sender)){
                sender.sendMessage(ChatColor.GOLD +"[RyveMusic] "+ ChatColor.AQUA +plugin.lang.getProperty("permission"));
                return true;
            }
            if(!plugin.npc.selectNpc.containsKey(sender.getName())){
                sender.sendMessage(ChatColor.GOLD +"[RyveMusic] "+ ChatColor.AQUA +plugin.lang.getProperty("selectNpc"));
                return true;
            }
            if (args.length < 2) {
                sender.sendMessage(ChatColor.GOLD +"[RyveMusic] "+ ChatColor.AQUA +"/rm setNpc <music number>!");
                return true;
            }
            String musicUrl = music.getMusic(Integer.parseInt(args[1]));
            if(musicUrl.equals("bad musique")){
                sender.sendMessage(ChatColor.GOLD +"[RyveMusic] "+ ChatColor.AQUA +plugin.lang.getProperty("validMusic"));
                return true;
            }
            plugin.npcs.addProperty(""+plugin.npc.selectNpc.get(sender.getName()), musicUrl);
            sender.sendMessage(ChatColor.GOLD +"[RyveMusic]"+ ChatColor.AQUA +plugin.lang.getProperty("npcPlay")+" "+ ChatColor.GREEN + music.parseTitle(musicUrl) + ChatColor.AQUA +".");
            return true;
        }else if(command.equalsIgnoreCase("delNpc")){
            if(!plugin.isCitizenEnable){
                sender.sendMessage(ChatColor.GOLD +"[RyveMusic] "+ ChatColor.AQUA +"Citizen "+ plugin.lang.getProperty("activePlugin"));
                return true;
            }
            if(!plugin.canSetNpc(sender)){
                sender.sendMessage(ChatColor.GOLD +"[RyveMusic] "+ ChatColor.AQUA +plugin.lang.getProperty("permission"));
                return true;
            }
            if(!plugin.npc.selectNpc.containsKey(sender.getName())){
                sender.sendMessage(ChatColor.GOLD +"[RyveMusic] "+ ChatColor.AQUA +plugin.lang.getProperty("selectNpc"));
                return true;
            }
            plugin.npcs.removeProperty(""+plugin.npc.selectNpc.get(sender.getName()));
            sender.sendMessage(ChatColor.GOLD +"[RyveMusic]"+ ChatColor.AQUA +plugin.lang.getProperty("npcDel"));
            return true;
        }else if(command.equalsIgnoreCase("reload")){
            plugin.setting.reload();
            sender.sendMessage(ChatColor.GOLD +"[RyveMusic]"+ ChatColor.AQUA +" reloaded");
            return true;
        }else{
            sender.sendMessage("/rm <global:list:play:stop:setJuke:setRegion:delRegion:setNpc:delNpc:reload> [next argument]!");
            return true;
        }
        return true;
    }
}
