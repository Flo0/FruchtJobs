package com.gestankbratwurst.fruchtjobs.jobs.sieving;

import com.gestankbratwurst.fruchtcore.util.common.UtilPlayer;
import com.gestankbratwurst.fruchtjobs.FruchtJobs;
import com.gestankbratwurst.fruchtjobs.jobs.JobHolder;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FruchtJobs and was created at the 10.04.2020
 *
 * FruchtJobs can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class SieveManager {

  public SieveManager(FruchtJobs plugin) {
    this.sieveInventories = new Object2ObjectOpenHashMap<>();
    Bukkit.getPluginManager().registerEvents(new SieveListener(this), plugin);
  }

  public void openFor(JobHolder holder, Block block) {
    SieveInventoryType type = SieveInventoryType.valueOf(block.getType().toString());
    SieveInventory sieveInventory = new SieveInventory(type, holder, block);
    sieveInventories.put(sieveInventory.getBukkitInventory(), sieveInventory);
    Bukkit.getPlayer(holder.getOwnerID()).openInventory(sieveInventory.getBukkitInventory());
  }

  private final Object2ObjectOpenHashMap<Inventory, SieveInventory> sieveInventories;

  protected void handleEvent(InventoryClickEvent event) {
    Inventory inventory = event.getInventory();
    if (!sieveInventories.containsKey(inventory)) {
      return;
    }
    event.setCancelled(true);
    if (event.getView().getTopInventory() != inventory) {
      return;
    }
    sieveInventories.get(inventory).handleClick(event);
  }

  protected void handleEvent(InventoryCloseEvent event) {
    SieveInventory inv = sieveInventories.remove(event.getInventory());
    if (inv != null && inv.isSieved()) {
      UtilPlayer.playSound((Player) event.getPlayer(), Sound.BLOCK_SAND_BREAK, 0.8F, 0.8F);
      inv.getSievedBlock().setType(Material.AIR);
    }
  }

}