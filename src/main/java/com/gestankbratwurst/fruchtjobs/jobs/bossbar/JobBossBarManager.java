package com.gestankbratwurst.fruchtjobs.jobs.bossbar;

import com.gestankbratwurst.fruchtcore.FruchtCore;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.entity.Player;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FruchtJobs and was created at the 26.03.2020
 *
 * FruchtJobs can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class JobBossBarManager {

  public JobBossBarManager() {
    barMappings = new Object2ObjectOpenHashMap<>();
    FruchtCore.getInstance().getTaskManager().runRepeatedBukkit(this::checkBars, 1L, 1L);
  }

  private final Map<UUID, LinkedBossBar> barMappings;

  public void login(Player player) {
    barMappings.put(player.getUniqueId(), new LinkedBossBar(player));
  }

  public void logout(Player player) {
    barMappings.remove(player.getUniqueId());
  }

  public void update(Player player, double progress, String text, int ticks) {
    update(player.getUniqueId(), progress, text, ticks);
  }

  public void update(UUID playerID, double progress, String text, int ticks) {
    LinkedBossBar bar = barMappings.get(playerID);
    bar.setProgress(progress);
    bar.setText(text);
    bar.showFor(ticks);
  }

  private void checkBars() {
    for (LinkedBossBar bar : barMappings.values()) {
      bar.run();
    }
  }

}
