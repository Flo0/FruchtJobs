package com.gestankbratwurst.fruchtjobs.jobs.bossbar;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
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
public class LinkedBossBar implements Runnable {

  protected LinkedBossBar(Player player) {
    this.player = player;
    this.bossBar = Bukkit.createBossBar("", BarColor.BLUE, BarStyle.SEGMENTED_10);
    bossBar.addPlayer(player);
    bossBar.setVisible(false);
  }

  private boolean showing = false;
  private int ticksAlive = 0;
  private final Player player;
  private final BossBar bossBar;

  protected void setText(String text) {
    this.bossBar.setTitle(text);
  }

  protected void showFor(int ticks) {
    ticksAlive = ticks;
  }

  protected void setProgress(double progress) {
    bossBar.setProgress(progress);
  }

  @Override
  public void run() {
    if (ticksAlive == 0) {
      if (showing) {
        showing = false;
        bossBar.setVisible(false);
      }
    } else {
      ticksAlive--;
      if (!showing) {
        showing = true;
        bossBar.setVisible(true);
      }
    }
  }

}