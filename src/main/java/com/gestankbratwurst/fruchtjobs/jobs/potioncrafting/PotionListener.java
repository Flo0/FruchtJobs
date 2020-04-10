package com.gestankbratwurst.fruchtjobs.jobs.potioncrafting;

import com.gestankbratwurst.fruchtcore.util.Msg;
import com.gestankbratwurst.fruchtcore.util.common.NameSpaceFactory;
import com.gestankbratwurst.fruchtcore.util.common.UtilPlayer;
import com.gestankbratwurst.fruchtjobs.jobs.JobManager;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Campfire;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.persistence.PersistentDataType;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FruchtJobs and was created at the 07.04.2020
 *
 * FruchtJobs can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
@AllArgsConstructor
public class PotionListener implements Listener {

  private final JobManager jobManager;
  private final PotionRecipeManager potionRecipeManager;
  private final PotionManager potionManager;

  @EventHandler
  public void onPlace(BlockPlaceEvent event) {
    Material mat = event.getBlockPlaced().getType();
    Block placed = event.getBlockPlaced();
    Campfire campfire;
    if (mat == Material.CAULDRON) {
      Block down = placed.getRelative(BlockFace.DOWN);
      if (down.getType() == Material.CAMPFIRE) {
        campfire = (Campfire) down.getState();
      } else {
        return;
      }
    } else if (mat == Material.CAMPFIRE) {
      Block up = placed.getRelative(BlockFace.UP);
      if (up.getType() == Material.CAULDRON) {
        campfire = (Campfire) placed.getState();
      } else {
        return;
      }
    } else {
      return;
    }
    Player player = event.getPlayer();
    campfire.getPersistentDataContainer()
        .set(NameSpaceFactory.provide("POTION_FIRE"), PersistentDataType.STRING, player.getUniqueId().toString());
    String kessel = Msg.elem("Trank-Kessel");
    Msg.send(player, "Alchemie", "Du hast einen " + kessel + " gebaut.");
    UtilPlayer.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 0.5F, 1.2F);
    potionManager.createCauldron(placed.getLocation(), null);
  }

  @EventHandler
  public void onInteract(PlayerInteractEvent event) {
    potionManager.handleInteract(event);
  }

  @EventHandler
  public void onBreak(BlockBreakEvent event) {
    Block block = event.getBlock();
    Material mat = block.getType();
    if (mat == Material.CAULDRON) {
      Block down = block.getRelative(BlockFace.DOWN);
      if (down.getType() == Material.CAMPFIRE) {
        down.breakNaturally();
        potionManager.removeCauldron(block.getLocation());
      }
    } else if (mat == Material.CAMPFIRE) {
      Block up = block.getRelative(BlockFace.UP);
      if (up.getType() == Material.CAULDRON) {
        potionManager.removeCauldron(up.getLocation());
      }
    }
  }

  @EventHandler
  public void onDrop(PlayerDropItemEvent event) {
    event.getItemDrop()
        .getPersistentDataContainer()
        .set(NameSpaceFactory.provide("OWNER"), PersistentDataType.STRING, event.getPlayer().getUniqueId().toString());
  }

  @EventHandler
  public void onLoad(ChunkLoadEvent event) {
    potionManager.loadCauldrons(event.getChunk());
  }

  @EventHandler
  public void onUnload(ChunkUnloadEvent event) {
    potionManager.unloadCauldrons(event.getChunk());
  }

}
