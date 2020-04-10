package com.gestankbratwurst.fruchtjobs;

import co.aikar.commands.PaperCommandManager;
import com.gestankbratwurst.fruchtcore.FruchtCore;
import com.gestankbratwurst.fruchtcore.recipes.RecipeModule;
import com.gestankbratwurst.fruchtjobs.jobs.JobManager;
import com.gestankbratwurst.fruchtjobs.jobs.JobPeripheral;
import com.gestankbratwurst.fruchtjobs.jobs.JobType;
import com.gestankbratwurst.fruchtjobs.jobs.bossbar.JobBossBarManager;
import com.gestankbratwurst.fruchtjobs.jobs.commands.JobCommand;
import com.gestankbratwurst.fruchtjobs.jobs.potioncrafting.PotionManager;
import com.gestankbratwurst.fruchtjobs.jobs.sieving.SieveManager;
import java.util.Arrays;
import java.util.stream.Collectors;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public final class FruchtJobs extends JavaPlugin {

  @Getter
  private JobManager jobManager;
  private PaperCommandManager paperCommandManager;
  @Getter
  private JobBossBarManager jobBossBarManager;
  @Getter
  private SieveManager sieveManager;
  private PotionManager potionManager;

  @Override
  public void onEnable() {
    jobBossBarManager = new JobBossBarManager();
    paperCommandManager = FruchtCore.getInstance().getCommandManager();
    sieveManager = new SieveManager(this);
    jobManager = new JobManager(this);
    potionManager = new PotionManager(this);
    paperCommandManager.registerCommand(new JobCommand(this.jobManager));
    paperCommandManager.getCommandCompletions().registerStaticCompletion("jobtypes", Arrays
        .stream(JobType.values())
        .map(Enum::toString)
        .collect(Collectors.toList()));
    JobPeripheral.init(FruchtCore.getInstance(), this);
  }

  @Override
  public void onDisable() {
    // Plugin shutdown logic
  }
}
