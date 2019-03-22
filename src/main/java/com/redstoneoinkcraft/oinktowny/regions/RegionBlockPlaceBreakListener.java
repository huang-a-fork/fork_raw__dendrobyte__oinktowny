package com.redstoneoinkcraft.oinktowny.regions;

import com.redstoneoinkcraft.oinktowny.Main;
import com.redstoneoinkcraft.oinktowny.clans.ClanManager;
import com.redstoneoinkcraft.oinktowny.clans.ClanObj;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.UUID;

/**
 * OinkTowny created/started by Mark Bacon (Mobkinz78/Dendrobyte)
 * Please do not use or edit without permission!
 * If you have any questions, reach out to me on Twitter: @Mobkinz78
 * §
 */
public class RegionBlockPlaceBreakListener implements Listener {

    Main mainInstance = Main.getInstance();
    String prefix = mainInstance.getPrefix();

    RegionsManager rm = RegionsManager.getInstance();
    ClanManager cm = ClanManager.getInstance();

    @EventHandler
    public void onBreakInRegion(BlockBreakEvent event){
        Player player = event.getPlayer();
        if(!canPlayerEdit(event.getBlock().getChunk(), player)){
            event.setCancelled(true);
            player.sendMessage(prefix + "You are not in the proper clan to edit this claim.");
        }
    }

    @EventHandler
    public void onPlaceInRegion(BlockPlaceEvent event){
        Player player = event.getPlayer();
        if(!canPlayerEdit(event.getBlock().getChunk(), event.getPlayer())){
            event.setCancelled(true);
            player.sendMessage(prefix + "You are not in the proper clan to edit this claim.");
        }
    }

    private boolean canPlayerEdit(Chunk eventChunk, Player editor){
        if(!rm.chunkIsClaimed(eventChunk)) return true;

        // If the player is not in a clan, or the chunk owner has no clan, cancel the event
        ClanObj eventPlayerClan = cm.getPlayerClanID(editor.getUniqueId());
        UUID chunkOwnerID = rm.getClaimedChunks().get(eventChunk);
        ClanObj chunkOwnerClan = cm.getPlayerClanID(chunkOwnerID);
        if(eventPlayerClan == null || chunkOwnerClan == null){
            return false;
        }

        // If both players do have a clan, see if they're equal and continue
        // Since the claim isn't necessarily the leader's claim, we check if the clans are equal
        if(!eventPlayerClan.equals(chunkOwnerClan)){ // TODO: Verify functionality with this equality, otherwise write custom equals or compareTo method
            return false;
        }
        return true;
    }


}
