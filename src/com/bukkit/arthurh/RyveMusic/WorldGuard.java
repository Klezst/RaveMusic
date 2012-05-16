/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bukkit.arthurh.RyveMusic;
import org.bukkit.entity.Player;
import org.bukkit.World;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.protection.flags.InvalidFlagFormat;
import org.bukkit.command.CommandSender;
import com.sk89q.worldguard.protection.ApplicableRegionSet;


/**
 *
 * @author arthur
 */
public class WorldGuard {
    private final RyveMusic plugin;
    private RegionManager regionManager;
    private World world;
    public WorldGuard(RyveMusic plugin, World world ){
        this.plugin = plugin;
        regionManager = plugin.wgp.getRegionManager(world);
        this.world = world;
    }
    public boolean existRegion(String name){ 
        return regionManager.hasRegion(name);
    }
    
    public void addOwner(String name, String owner){       
        if(plugin.getServer().getPlayer(owner) != null){
            LocalPlayer lp = plugin.wgp.wrapPlayer(plugin.getServer().getPlayer(owner));
            if(lp != null){
                if(existRegion(name)){
                    regionManager.getRegion(name).getOwners().addPlayer(owner);
                }
            }
        }
    }
    public void removeOwner(String name, String owner){
        if(plugin.getServer().getPlayer(owner) != null){
            LocalPlayer lp = plugin.wgp.wrapPlayer(plugin.getServer().getPlayer(owner));
            if(existRegion(name)){
                regionManager.getRegion(name).getOwners().removePlayer(owner);
            }
        }
        
    }
    public boolean isOwner(Player player, String region){
        if(regionManager.getRegion(region) == null){
            return false;
        }
        if(player ==null){
            return false;
        }
        LocalPlayer lp = plugin.wgp.wrapPlayer(player);
        return regionManager.getRegion(region).isOwner(lp);
    }
    public String getParent(String region){
        if(regionManager.getRegion(region) == null){
            return null;
        }
        return regionManager.getRegion(region).getParent().getId();
    }
    public boolean hasParent(String region){
        
        if(regionManager.getRegion(region) == null){
            return false;
        }
        return regionManager.getRegion(region).getParent() != null;
    }
    public boolean inRegion(Player player, String region){
        LocalPlayer lp = plugin.wgp.wrapPlayer(player);

        if(regionManager.getRegion(region) == null){
            return true;
        }
        if(regionManager.getRegion(region).contains(lp.getPosition())){
            return true;
        }
        return false;
    }

    public Flag<?> getFlag(String flagName){
        Flag<?> foundFlag = null;

        
        // Now time to find the flag!
        for (Flag<?> flag : DefaultFlag.getFlags()) {
            // Try to detect the flag
            if (flag.getName().replace("-", "").equalsIgnoreCase(flagName.replace("-", ""))) {
                foundFlag = flag;
                break;
            }
        }
        return foundFlag;
    }
    public static <V> void setFlag(ProtectedRegion region,
            Flag<V> flag, WorldGuardPlugin plugin, CommandSender sender, String value)
                throws InvalidFlagFormat {
        region.setFlag(flag, flag.parseInput(plugin, sender, value));
    }
    
    public boolean canBuild(Player player){
        return plugin.wgp.canBuild(player, player.getLocation().getBlock().getRelative(0, -1, 0));
    }
    
}
