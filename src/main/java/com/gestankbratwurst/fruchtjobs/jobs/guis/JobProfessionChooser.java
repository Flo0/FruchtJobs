package com.gestankbratwurst.fruchtjobs.jobs.guis;

import com.gestankbratwurst.fruchtcore.util.common.UtilPlayer;
import com.gestankbratwurst.fruchtjobs.jobs.JobHolder;
import com.gestankbratwurst.fruchtjobs.jobs.JobType;
import java.util.function.Consumer;
import net.crytec.inventoryapi.SmartInventory;
import net.crytec.inventoryapi.api.ClickableItem;
import net.crytec.inventoryapi.api.InventoryContent;
import net.crytec.inventoryapi.api.InventoryProvider;
import net.crytec.inventoryapi.api.SlotPos;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FruchtJobs and was created at the 27.03.2020
 *
 * FruchtJobs can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class JobProfessionChooser implements InventoryProvider {

  static void choose(Player player, JobHolder holder, JobType mainJob, Consumer<JobType> professionConsumer) {
    SmartInventory.builder()
        .title("Spezialisierung wählen [" + mainJob.getDisplayName() + "]")
        .size(3)
        .provider(new JobProfessionChooser(holder, mainJob, professionConsumer))
        .build()
        .open(player);
  }

  private JobProfessionChooser(JobHolder holder, JobType mainJob, Consumer<JobType> professionConsumer) {
    this.mainJob = mainJob;
    this.professionConsumer = professionConsumer;
    this.jobHolder = holder;
    this.parentJob = mainJob.getParentJob();
  }

  private final JobHolder jobHolder;
  private final JobType mainJob;
  private final JobType parentJob;
  private final Consumer<JobType> professionConsumer;

  @Override
  public void init(Player player, InventoryContent content) {
    JobType[] professions = mainJob.getProfessions();
    int slot = 5 - professions.length;
    int subIndex = professions.length > 0 ? 2 : 1;
    for (JobType profession : professions) {
      content.set(SlotPos.of(1, slot), ClickableItem.of(profession.getMainIcon(jobHolder, true), e -> {
        professionConsumer.accept(profession);
        UtilPlayer.playSound(player, Sound.UI_BUTTON_CLICK);
      }));
      slot += 2;
    }
    if (parentJob != mainJob) {
      professions = parentJob.getProfessions();
      slot = 5 - professions.length;
      for (JobType profession : professions) {
        content.set(SlotPos.of(subIndex, slot), ClickableItem.of(profession.getMainIcon(jobHolder, true), e -> {
          professionConsumer.accept(profession);
          UtilPlayer.playSound(player, Sound.UI_BUTTON_CLICK);
        }));
        slot += 2;
      }
    }
  }

}
