package com.gestankbratwurst.fruchtjobs.jobs.sieving;

import com.gestankbratwurst.fruchtcore.resourcepack.skins.Model;
import com.gestankbratwurst.fruchtcore.util.common.UtilItem;
import com.gestankbratwurst.fruchtcore.util.common.UtilPlayer;
import com.gestankbratwurst.fruchtcore.util.items.ItemBuilder;
import com.gestankbratwurst.fruchtjobs.jobs.JobHolder;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.util.function.Consumer;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
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
public class SieveInventory {

  private static final ItemStack SIEVE_ITEM = new ItemBuilder(Model.SIEVE_SIEVE.getItem())
      .name("§eSieben [§cNicht ausgewählt§e]")
      .lore("")
      .lore("§7Kann für die oberste")
      .lore("§7Schicht aus Sand oder")
      .lore("§7Kies verwender werden.")
      .build();

  private static final ItemStack SIEVE_ITEM_SELECTED = new ItemBuilder(Model.SIEVE_SIEVE_SELECTED.getItem())
      .name("§eSieben [§aAusgewählt§e]")
      .lore("")
      .lore("§7Kann für die oberste")
      .lore("§7Schicht aus Sand oder")
      .lore("§7Kies verwender werden.")
      .build();

  private static final ItemStack HAMMER_ITEM = new ItemBuilder(Model.SIEVE_HAMMER.getItem())
      .name("§eHammern [§cNicht ausgewählt§e]")
      .lore("")
      .lore("§7Kann für das Zerschlagen")
      .lore("§7von Steinen und Geoden ver-")
      .lore("§7wendet werden.")
      .build();

  private static final ItemStack HAMMER_ITEM_SELECTED = new ItemBuilder(Model.SIEVE_HAMMER_SELECTED.getItem())
      .name("§eHammern [§aAusgewählt§e]")
      .lore("")
      .lore("§7Kann für das Zerschlagen")
      .lore("§7von Steinen und Geoden ver-")
      .lore("§7wendet werden.")
      .build();

  private static final ItemStack ENERGY_ITEM = new ItemBuilder(Model.SIEVE_ENERGY_FULL.getItem())
      .name("§eEnergie §6[ §e100 §6/ §e100 §6]")
      .build();

  private static final int[] SIEVE_SLOT_IDS = new int[]{
      10, 11, 12, 13, 14, 15,
      19, 20, 21, 22, 23, 24,
      28, 29, 30, 31, 32, 33,
      37, 38, 39, 40, 41, 42};

  protected SieveInventory(SieveInventoryType type, JobHolder holder, Block sievedBlock) {
    this.bukkitInventory = Bukkit.createInventory(null, 54, "Schürfen");
    this.sieveSelected = true;
    this.holder = holder;
    this.type = type;
    this.sievedBlock = sievedBlock;
    this.sieveSlots = new Int2ObjectOpenHashMap<>();
    this.miscConsumer = new Int2ObjectOpenHashMap<>();
    this.energyItem = ENERGY_ITEM.clone();
    UtilPlayer.playSound(Bukkit.getPlayer(holder.getOwnerID()), Sound.ENTITY_GENERIC_SPLASH, 0.8F, 1.2F);
    energyItem.setAmount(100);
    fill();
  }

  @Getter(AccessLevel.PROTECTED)
  private final Inventory bukkitInventory;
  private final JobHolder holder;
  private final SieveInventoryType type;
  private final Int2ObjectMap<SieveInventorySlot> sieveSlots;
  private final Int2ObjectMap<Consumer<InventoryClickEvent>> miscConsumer;
  @Getter(AccessLevel.PROTECTED)
  private final Block sievedBlock;
  private boolean sieveSelected;
  private final ItemStack energyItem;
  @Getter(AccessLevel.PROTECTED)
  private boolean sieved = false;

  protected void handleClick(InventoryClickEvent event) {
    int slotID = event.getSlot();
    SieveInventorySlot sieveSlot = sieveSlots.get(slotID);
    if (sieveSlot != null) {
      sieveSlot.onClick(event, holder, sieveSelected);
      bukkitInventory.setItem(slotID, sieveSlot.getDisplay());
      int energyMinus = 0;
      sieved = true;
      if (event.isLeftClick()) {
        energyMinus = 1;
      } else if (event.isRightClick()) {
        energyMinus = 3;
      }
      if (energyItem.getAmount() - energyMinus <= 0) {
        Player player = (Player) event.getWhoClicked();
        player.closeInventory();
        sievedBlock.getWorld().playSound(sievedBlock.getLocation(), Sound.ENTITY_PLAYER_ATTACK_CRIT, 0.8F, 0.8F);
        return;
      }
      energyItem.subtract(energyMinus);
      ItemMeta energyMeta = energyItem.getItemMeta();
      energyMeta.setDisplayName("§eEnergie §6[ §e" + energyItem.getAmount() + " §6/ §e100 §6]");
      energyItem.setItemMeta(energyMeta);
      bukkitInventory.setItem(8, energyItem);
    } else if (miscConsumer.containsKey(slotID)) {
      miscConsumer.get(slotID).accept(event);
    }
  }

  private void fill() {
    for (int i : SIEVE_SLOT_IDS) {
      SieveInventorySlot sieveSlot = new SieveInventorySlot(type);
      sieveSlots.put(i, sieveSlot);
      bukkitInventory.setItem(i, sieveSlot.getDisplay());
    }
    bukkitInventory.setItem(26, SIEVE_ITEM_SELECTED);
    bukkitInventory.setItem(35, HAMMER_ITEM);
    bukkitInventory.setItem(8, energyItem);
    miscConsumer.put(26, event -> {
      if (!sieveSelected) {
        sieveSelected = true;
        bukkitInventory.setItem(26, SIEVE_ITEM_SELECTED);
        bukkitInventory.setItem(35, HAMMER_ITEM);
        UtilPlayer.playSound((Player) event.getWhoClicked(), Sound.UI_BUTTON_CLICK);
      }
    });
    miscConsumer.put(35, event -> {
      if (sieveSelected) {
        sieveSelected = false;
        bukkitInventory.setItem(26, SIEVE_ITEM);
        bukkitInventory.setItem(35, HAMMER_ITEM_SELECTED);
        UtilPlayer.playSound((Player) event.getWhoClicked(), Sound.UI_BUTTON_CLICK);
      }
    });
  }

}
