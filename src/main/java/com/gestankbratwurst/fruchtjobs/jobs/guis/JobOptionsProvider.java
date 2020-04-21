package com.gestankbratwurst.fruchtjobs.jobs.guis;

import com.gestankbratwurst.fruchtcore.resourcepack.skins.Model;
import com.gestankbratwurst.fruchtcore.util.common.UtilPlayer;
import com.gestankbratwurst.fruchtcore.util.items.ItemBuilder;
import com.gestankbratwurst.fruchtjobs.jobs.JobHolder;
import com.gestankbratwurst.fruchtjobs.jobs.JobManager;
import lombok.AllArgsConstructor;
import net.crytec.inventoryapi.api.ClickableItem;
import net.crytec.inventoryapi.api.InventoryContent;
import net.crytec.inventoryapi.api.InventoryProvider;
import net.crytec.inventoryapi.api.SlotPos;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FruchtJobs and was created at the 25.03.2020
 *
 * FruchtJobs can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
@AllArgsConstructor
public class JobOptionsProvider implements InventoryProvider {

  private final JobManager jobManager;

  @Override
  public void init(Player player, InventoryContent content) {

    JobHolder jobHolder = jobManager.getOnlineHolder(player);

    ClickableItem optionsButton = ClickableItem.of(new ItemBuilder(Material.CRAFTING_TABLE).name("§eJobs").build(), e -> {
      jobManager.openJobGUI(player);
      UtilPlayer.playSound(player, Sound.UI_BUTTON_CLICK);
    });
    content.set(SlotPos.of(2, 0), optionsButton);

    boolean actionBarEnabled = jobHolder.isActionBarDisplaying();
    boolean bossBarEnabled = jobHolder.isBossBarDisplaying();
    boolean hologramsEnabled = jobHolder.isHologramDisplaying();

    ItemStack actionBarIcon = new ItemBuilder(actionBarEnabled ? Model.GREEN_CHECK.getItem() : Model.RED_X.getItem())
        .name("§eBenutze Action Bar: " + (actionBarEnabled ? "§a✔" : "§c✖"))
        .lore("", "§fBenutze die Action Bar zum Anzeigen", "§fvon Erfahrungspunkten.")
        .build();
    ItemStack bossBarIcon = new ItemBuilder(bossBarEnabled ? Model.GREEN_CHECK.getItem() : Model.RED_X.getItem())
        .name("§eBenutze Boss Bar: " + (bossBarEnabled ? "§a✔" : "§c✖"))
        .lore("", "§fBenutze die Boss Bar zum Anzeigen", "§fvon Erfahrungspunkten.")
        .build();
    ItemStack hologramIcon = new ItemBuilder(hologramsEnabled ? Model.GREEN_CHECK.getItem() : Model.RED_X.getItem())
        .lore("", "§fBenutze Hologramme zum Anzeigen", "§fvon Erfahrungspunkten.")
        .name("§eBenutze Hologramme: " + (hologramsEnabled ? "§a✔" : "§c✖")).build();

    ClickableItem actionBarButton = ClickableItem.of(actionBarIcon, e -> {
      jobHolder.setActionBarDisplaying(!jobHolder.isActionBarDisplaying());
      UtilPlayer.playSound(player, Sound.UI_BUTTON_CLICK);
      this.reopen(player, content);
    });
    ClickableItem bossBarButton = ClickableItem.of(bossBarIcon, e -> {
      jobHolder.setBossBarDisplaying(!jobHolder.isBossBarDisplaying());
      UtilPlayer.playSound(player, Sound.UI_BUTTON_CLICK);
      this.reopen(player, content);
    });
    ClickableItem hologramButton = ClickableItem.of(hologramIcon, e -> {
      jobHolder.setHologramDisplaying(!jobHolder.isHologramDisplaying());
      UtilPlayer.playSound(player, Sound.UI_BUTTON_CLICK);
      this.reopen(player, content);
    });

    content.set(SlotPos.of(1, 2), actionBarButton);
    content.set(SlotPos.of(1, 4), bossBarButton);
    content.set(SlotPos.of(1, 6), hologramButton);

  }

}
