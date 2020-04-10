package com.gestankbratwurst.fruchtjobs.jobs.sieving;

import com.gestankbratwurst.fruchtcore.resourcepack.skins.Model;
import com.gestankbratwurst.fruchtcore.util.common.UtilPlayer;
import com.gestankbratwurst.fruchtcore.util.items.ItemBuilder;
import com.gestankbratwurst.fruchtjobs.jobs.JobHolder;
import com.google.common.collect.Lists;
import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FruchtJobs and was created at the 10.04.2020
 *
 * FruchtJobs can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class ItemLayer implements SieveLayerItem {

  protected ItemLayer(ItemStack itemStack) {
    this.itemStack = itemStack;
  }

  private final ItemStack itemStack;

  @Override
  public ItemStack getDisplayItem() {
    return new ItemBuilder(itemStack.clone())
        .lore("")
        .lore("§7Klicke zum erhalten.")
        .lore("§7Kostet Energie.")
        .build();
  }

  @Override
  public boolean onClick(InventoryClickEvent event, JobHolder holder, boolean withSieve) {
    Player player = (Player) event.getWhoClicked();
    player.getInventory().addItem(itemStack).values().forEach(remaining ->
        player.getWorld().dropItemNaturally(player.getLocation(), remaining));
    UtilPlayer.playSound(player, Sound.ENTITY_ITEM_PICKUP);
    return true;
  }

}
