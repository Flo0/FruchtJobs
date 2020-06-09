package com.gestankbratwurst.fruchtjobs.jobs.guis;

import com.gestankbratwurst.fruchtcore.FruchtCore;
import com.gestankbratwurst.fruchtcore.recipes.RecipeGUI;
import com.gestankbratwurst.fruchtcore.resourcepack.skins.Model;
import com.gestankbratwurst.fruchtcore.resourcepack.sounds.CustomSound;
import com.gestankbratwurst.fruchtcore.util.Msg;
import com.gestankbratwurst.fruchtcore.util.common.UtilPlayer;
import com.gestankbratwurst.fruchtcore.util.items.ItemBuilder;
import com.gestankbratwurst.fruchtjobs.jobs.JobHolder;
import com.gestankbratwurst.fruchtjobs.jobs.JobManager;
import com.gestankbratwurst.fruchtjobs.jobs.JobType;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import net.crytec.inventoryapi.api.ClickableItem;
import net.crytec.inventoryapi.api.InventoryContent;
import net.crytec.inventoryapi.api.InventoryProvider;
import net.crytec.inventoryapi.api.SlotPos;
import net.minecraft.server.v1_15_R1.SoundCategory;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
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
public class JobMainProvider implements InventoryProvider {

  public static boolean PLAY_INFO_SOUND = true;

  private static final ItemStack[] STAGES = new ItemStack[]{
      new ItemBuilder(Model.RECIPE_ICON_ONE.getItem()).name("§eRezepte").build(),
      new ItemBuilder(Model.RECIPE_ICON_TWO.getItem()).name("§eRezepte").build(),
      new ItemBuilder(Model.RECIPE_ICON_THREE.getItem()).name("§eRezepte").build()
  };

  private static final Object2IntMap<Inventory> STAGED_INVENTORIES = new Object2IntOpenHashMap<>();

  private static void updateStages() {
    for (Entry<Inventory> entry : STAGED_INVENTORIES.object2IntEntrySet()) {
      int stage = entry.getIntValue();
      if (++stage == STAGES.length) {
        stage = 0;
      }
      STAGED_INVENTORIES.put(entry.getKey(), stage);
      entry.getKey().setItem(36, STAGES[stage]);
    }
  }

  static {
    FruchtCore.getInstance().getTaskManager().runRepeatedBukkit(JobMainProvider::updateStages, 6L, 6L);
  }

  public JobMainProvider(JobManager jobManager, JobHolder jobHolder) {
    this.jobHolder = jobHolder;
    this.jobManager = jobManager;
  }

  private final JobHolder jobHolder;
  private final JobManager jobManager;

  @Override
  public void onClose(Player player, InventoryContent content) {
    STAGED_INVENTORIES.removeInt(content.getInventory());
  }

  @Override
  public void init(Player player, InventoryContent content) {
    STAGED_INVENTORIES.put(content.getInventory(), 0);
    content.set(0, ClickableItem.empty(new ItemBuilder(Model.JOB_MAIN_GUI.getItem()).name("").build()));
    if (PLAY_INFO_SOUND) {
      CustomSound.RECIPE_INFO.play(player, SoundCategory.MASTER, 1F, 1F);
    }
    List<JobType> jobTypes = Arrays.stream(JobType.values()).filter(JobType::isMainJob)
        .filter(j -> j != JobType.ALCHEMIST).collect(Collectors.toList());
    for (int x = 0; x < jobTypes.size(); x++) {
      JobType tempType = jobTypes.get(x);
      JobType profession = jobHolder.getProfession(tempType);
      boolean isProfession = false;
      if (profession != null) {
        isProfession = true;
        tempType = profession;
      }
      final JobType type = tempType;
      content.set(SlotPos.of(1, x + 2), ClickableItem.of(type.getMainIcon(jobHolder, false), e -> {
        UtilPlayer.playSound(player, Sound.UI_BUTTON_CLICK);
        jobManager.openJobPerkGUI(player, type, 1);
      }));
      content.set(SlotPos.of(2, x + 2), ClickableItem.of(type.getMainButtonIcon(jobHolder), e -> {
        UtilPlayer.playSound(player, Sound.UI_BUTTON_CLICK);
        if (jobHolder.isActive(type)) {
          Msg.send(player, "Jobs", "Job wurde deaktiviert.");
          jobHolder.unApplyForJob(type);
          reopen(player, content);
        } else if (jobHolder.getJobSlotsLeft() == 0) {
          Msg.error(player, "Jobs", "Maximale Anzahl an Jobs erreicht.");
          reopen(player, content);
        } else {
          jobHolder.applyForJob(type);
          Msg.send(player, "Jobs", "Job wurde aktiviert.");
          reopen(player, content);
        }
      }));
      if (isProfession || jobHolder.isMaxLevel(type)) {
        content.set(SlotPos.of(3, x + 2), ClickableItem.of(new ItemBuilder(Material.NETHER_STAR)
            .name("§6Spezialisierung wählen")
            .lore("")
            .lore("§4[§cAchtung§4]")
            .lore("§7Wenn du von einer Spezialisierung auf")
            .lore("§7eine andere wechselst, werden alle exp")
            .lore("§7und level von deiner bisherigen Speziali-")
            .lore("§7sierung gelöscht.")
            .build(), e -> {
          UtilPlayer.playSound(player, Sound.UI_BUTTON_CLICK);
          JobProfessionChooser.choose(player, jobHolder, type, prof -> {
            jobHolder.setProfession(prof);
            jobHolder.applyForJob(prof);
            reopen(player, content);
          });
        }));
      }
    }

    ClickableItem optionsButton = ClickableItem.of(new ItemBuilder(Material.STRUCTURE_VOID).name("§eOptionen").build(), e -> {
      jobManager.openJobOptionsGUI(player);
      UtilPlayer.playSound(player, Sound.UI_BUTTON_CLICK);
    });
    content.set(SlotPos.of(4, 8), optionsButton);

    ClickableItem recipesButton = ClickableItem.of(new ItemBuilder(Model.RECIPE_ICON_ONE.getItem()).name("§eRezepte").build(), e -> {
      RecipeGUI.open(player, ev -> {
        UtilPlayer.playSound(player, Sound.UI_BUTTON_CLICK);
        this.reopen(player, content);
      });
      UtilPlayer.playSound(player, Sound.UI_BUTTON_CLICK);
    });
    content.set(SlotPos.of(4, 0), recipesButton);
  }

}