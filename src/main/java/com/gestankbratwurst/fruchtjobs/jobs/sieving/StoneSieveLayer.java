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
public class StoneSieveLayer implements SieveLayerItem {

  protected StoneSieveLayer() {
    this.maxHp = 40;
    this.hp = maxHp;
    this.random = ThreadLocalRandom.current();
  }

  private final ThreadLocalRandom random;
  private final int maxHp;
  private int hp;
  private int lastHit;

  @Override
  public ItemStack getDisplayItem() {
    ItemStack item = Model.SIEVE_STONE.getItem();
    item.setAmount(hp);
    ItemMeta meta = item.getItemMeta();
    meta.setDisplayName("§fStein Schicht §e[ §6" + hp + "§e / §6" + maxHp + "§e ]");
    meta.setLore(Lists.newArrayList("",
        "§7Kann gehämmert werden.",
        "",
        "§fLinksklick: (1 Energie | 10 Schaden)",
        "§fRechtsklick: (3 Energie | 0 - 60 Schaden)",
        "",
        "§fLetzter Hit: §e" + lastHit));
    item.setItemMeta(meta);
    return item;
  }

  @Override
  public boolean onClick(InventoryClickEvent event, JobHolder holder, boolean withSieve) {
    if (!withSieve) {
      if (event.isLeftClick()) {
        lastHit = 10;
      } else if (event.isRightClick()) {
        lastHit = random.nextInt(0, 61);
        UtilPlayer.playSound((Player) event.getWhoClicked(), Sound.ENTITY_ZOMBIE_ATTACK_WOODEN_DOOR, 0.8F, 1.6F);
      } else {
        return false;
      }
      hp -= lastHit;
      if (hp <= 0) {
        UtilPlayer.playSound((Player) event.getWhoClicked(), Sound.BLOCK_STONE_BREAK, 0.9F, 1.1F);
        return true;
      } else {
        UtilPlayer.playSound((Player) event.getWhoClicked(), Sound.BLOCK_STONE_HIT, 0.5F, 1.5F);
      }
    }
    return false;
  }
}
