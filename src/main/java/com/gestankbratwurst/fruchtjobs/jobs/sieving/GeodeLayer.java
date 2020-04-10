package com.gestankbratwurst.fruchtjobs.jobs.sieving;

import com.gestankbratwurst.fruchtcore.resourcepack.skins.Model;
import com.gestankbratwurst.fruchtcore.util.common.UtilPlayer;
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
public class GeodeLayer implements SieveLayerItem {

  protected GeodeLayer(ItemStack itemStack) {
    this.maxHp = 90;
    this.itemStack = itemStack;
    this.hp = maxHp;
    this.random = ThreadLocalRandom.current();
  }

  private final ItemStack itemStack;
  private final ThreadLocalRandom random;
  private final int maxHp;
  private int hp;
  private int lastHit;

  @Override
  public ItemStack getDisplayItem() {
    ItemStack item = Model.SIEVE_GEODE.getItem();
    item.setAmount(hp);
    ItemMeta meta = item.getItemMeta();
    meta.setDisplayName("§aGeode §e[ §6" + hp + "§e / §6" + maxHp + "§e ]");
    meta.setLore(Lists.newArrayList("",
        "§7Enthält seltenere Gegenstände.",
        "§7Kann gehämmert werden.",
        "",
        "§fLinksklick: (1 Energie | 5 Schaden)",
        "§fRechtsklick: (3 Energie | 0 - 30 Schaden)",
        "",
        "§fLetzter Hit: §e" + lastHit));
    item.setItemMeta(meta);
    return item;
  }

  @Override
  public boolean onClick(InventoryClickEvent event, JobHolder holder, boolean withSieve) {
    if (!withSieve) {
      if (event.isLeftClick()) {
        lastHit = 5;
      } else if (event.isRightClick()) {
        lastHit = random.nextInt(0, 31);
      } else {
        return false;
      }
      hp -= lastHit;
      if (hp <= 0) {
        UtilPlayer.playSound((Player) event.getWhoClicked(), Sound.BLOCK_STONE_BREAK, 0.9F, 1.1F);
        Player player = (Player) event.getWhoClicked();
        player.getInventory().addItem(itemStack).values().forEach(remaining ->
            player.getWorld().dropItemNaturally(player.getLocation(), remaining));
        UtilPlayer.playSound(player, Sound.ENTITY_ITEM_PICKUP);
        return true;
      } else {
        UtilPlayer.playSound((Player) event.getWhoClicked(), Sound.BLOCK_STONE_HIT, 0.5F, 1.5F);
      }
    }
    return false;
  }
}
