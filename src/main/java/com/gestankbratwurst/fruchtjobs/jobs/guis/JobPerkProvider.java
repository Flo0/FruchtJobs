package com.gestankbratwurst.fruchtjobs.jobs.guis;

import com.gestankbratwurst.fruchtcore.util.Msg;
import com.gestankbratwurst.fruchtcore.util.common.UtilPlayer;
import com.gestankbratwurst.fruchtcore.util.guis.ConfirmationGUI;
import com.gestankbratwurst.fruchtcore.util.items.ItemBuilder;
import com.gestankbratwurst.fruchtjobs.jobs.JobHolder;
import com.gestankbratwurst.fruchtjobs.jobs.JobManager;
import com.gestankbratwurst.fruchtjobs.jobs.JobPerkLib;
import com.gestankbratwurst.fruchtjobs.jobs.JobPerkType;
import com.gestankbratwurst.fruchtjobs.jobs.JobType;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.crytec.inventoryapi.api.ClickableItem;
import net.crytec.inventoryapi.api.InventoryContent;
import net.crytec.inventoryapi.api.InventoryProvider;
import net.crytec.inventoryapi.api.SlotPos;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FruchtJobs and was created at the 29.03.2020
 *
 * FruchtJobs can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class JobPerkProvider implements InventoryProvider {

  public JobPerkProvider(JobManager jobManager, JobHolder jobHolder, JobType jobType, int page) {
    this.jobManager = jobManager;
    this.jobHolder = jobHolder;
    this.jobType = jobType;
    this.page = page;
    slotIndex = 0;
    firstHit = (page - 1) * 8;
    hitIndex = 0;
  }

  private final JobManager jobManager;
  private final JobHolder jobHolder;
  private final JobType jobType;
  private final int page;
  private int slotIndex;
  private final int firstHit;
  private int hitIndex;


  @Override
  public void init(Player player, InventoryContent content) {
    Int2ObjectMap<JobPerkType[]> perkMap = JobPerkLib.getJobPerkMap(jobType);
    for (int lvl = 0; lvl <= jobType.getMaxLevel(); lvl++) {
      JobPerkType[] lvlPerks = perkMap.get(lvl);
      if (lvlPerks != null) {
        hitIndex++;
        if (hitIndex < firstHit) {
          continue;
        } else if (hitIndex > firstHit + 8) {
          ClickableItem nextIcon = ClickableItem.of(new ItemBuilder(Material.ARROW).name("§6Seite [§e" + (page + 1) + "§6]").build(), e -> {
            UtilPlayer.playSound(player, Sound.UI_BUTTON_CLICK);
            jobManager.openJobPerkGUI(player, jobType, this.page + 1);
          });
          content.set(SlotPos.of(4, 8), nextIcon);
          break;
        }
        if (lvlPerks.length == 1) {
          content.set(SlotPos.of(2, slotIndex), getIcon(player, content, lvlPerks[0], lvlPerks));
        } else if (lvlPerks.length == 2) {
          content.set(SlotPos.of(1, slotIndex), getIcon(player, content, lvlPerks[0], lvlPerks));
          content.set(SlotPos.of(3, slotIndex), getIcon(player, content, lvlPerks[1], lvlPerks));
        } else if (lvlPerks.length == 3) {
          content.set(SlotPos.of(1, slotIndex), getIcon(player, content, lvlPerks[0], lvlPerks));
          content.set(SlotPos.of(2, slotIndex), getIcon(player, content, lvlPerks[1], lvlPerks));
          content.set(SlotPos.of(3, slotIndex), getIcon(player, content, lvlPerks[2], lvlPerks));
        } else if (lvlPerks.length == 4) {
          content.set(SlotPos.of(0, slotIndex), getIcon(player, content, lvlPerks[0], lvlPerks));
          content.set(SlotPos.of(1, slotIndex), getIcon(player, content, lvlPerks[1], lvlPerks));
          content.set(SlotPos.of(3, slotIndex), getIcon(player, content, lvlPerks[2], lvlPerks));
          content.set(SlotPos.of(4, slotIndex), getIcon(player, content, lvlPerks[3], lvlPerks));
        }
        slotIndex++;
      }
    }
    if (page != 1) {
      ClickableItem prevIcon = ClickableItem.of(new ItemBuilder(Material.ARROW).name("§6Seite [§e" + (page - 1) + "§6]").build(), e -> {
        UtilPlayer.playSound(player, Sound.UI_BUTTON_CLICK);
        jobManager.openJobPerkGUI(player, jobType, this.page - 1);
      });
      content.set(SlotPos.of(4, 0), prevIcon);
    }
    ClickableItem backIcon = ClickableItem.of(new ItemBuilder(Material.CRAFTING_TABLE).name("§6Zu den Jobs").build(), e -> {
      UtilPlayer.playSound(player, Sound.UI_BUTTON_CLICK);
      jobManager.openJobGUI(player);
    });
    content.set(SlotPos.of(4, 8), backIcon);
  }

  private ClickableItem getIcon(Player player, InventoryContent content, JobPerkType perkType, JobPerkType[] lvlPerks) {
    return ClickableItem.of(perkType.getPerkChooseIcon(jobHolder), e -> {
      boolean hasLvlPerk = false;
      for (JobPerkType perk : lvlPerks) {
        if (jobHolder.hasPerk(perk)) {
          hasLvlPerk = true;
        }
      }

      if (hasLvlPerk) {
        Msg.error(player, "Jobs", "Du hast schon einen Perk für dieses Level.");
      } else if (jobHolder.getLvl(perkType.getJobType()) >= perkType.getLevel()) {
        ConfirmationGUI.open(player, perkType.getDisplayName(), succ -> {
          jobHolder.addPerkType(perkType);
          String perkName = perkType.getDisplayName();
          Msg.send(player, "Jobs", "Du hast den Perk " + perkName + " erhalten.");
          UtilPlayer.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP);
          jobManager.openJobPerkGUI(player, jobType, page);
        }, fail -> jobManager.openJobPerkGUI(player, jobType, page));
      } else {
        Msg.error(player, "Jobs", "Du hast noch nicht das benötigte level.");
      }

      UtilPlayer.playSound(player, Sound.UI_BUTTON_CLICK);
    });
  }
}