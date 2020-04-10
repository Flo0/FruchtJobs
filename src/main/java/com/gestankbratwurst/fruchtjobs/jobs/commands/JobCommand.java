package com.gestankbratwurst.fruchtjobs.jobs.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;
import com.gestankbratwurst.fruchtcore.util.Msg;
import com.gestankbratwurst.fruchtjobs.jobs.JobManager;
import com.gestankbratwurst.fruchtjobs.jobs.JobType;
import org.bukkit.entity.Player;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FruchtJobs and was created at the 25.03.2020
 *
 * FruchtJobs can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
@CommandAlias("job|jobs|berufe")
@CommandPermission("jobs.commands.job")
public class JobCommand extends BaseCommand {

  public JobCommand(JobManager jobManager) {
    this.jobManager = jobManager;
  }

  private final JobManager jobManager;

  @Default
  public void onDefault(Player player) {
    jobManager.openJobGUI(player);
  }

  @Subcommand("optionen")
  public void onOptions(Player player) {
    jobManager.openJobOptionsGUI(player);
  }

  @Subcommand("admin addexp")
  @CommandPermission("jobs.commands.admin")
  @CommandCompletion("@jobtypes")
  @Syntax("<Player> <JobType> <Exp>")
  public void onAddExp(Player sender, JobType jobType, long amount) {
    jobManager.getOnlineHolder(sender).addExp(jobType, amount);
    String expElem = Msg.elem("" + amount + " Exp");
    String jobElem = Msg.elem(jobType.getDisplayName());
    String playerElem = Msg.elem(sender.getName());
    Msg.send(sender, "Jobs", playerElem + " hat " + expElem + " für den Job " + jobElem + " erhalten.");
  }

}
