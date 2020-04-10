package com.gestankbratwurst.fruchtjobs.io;

import com.gestankbratwurst.fruchtcore.FruchtCore;
import com.gestankbratwurst.fruchtcore.tasks.TaskManager;
import com.gestankbratwurst.fruchtjobs.FruchtJobs;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitScheduler;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FruchtJobs and was created at the 24.03.2020
 *
 * FruchtJobs can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class IOManager {

  public IOManager(FruchtJobs plugin) {
    this.plugin = plugin;
    this.bukkitScheduler = Bukkit.getScheduler();
    this.taskManager = FruchtCore.getInstance().getTaskManager();
    this.pluginFolder = plugin.getDataFolder();
    this.playerFolder = new File(pluginFolder + File.separator + "playerdata");
    if (!pluginFolder.exists()) {
      pluginFolder.mkdirs();
    }
    if (!playerFolder.exists()) {
      playerFolder.mkdirs();
    }
  }

  private final FruchtJobs plugin;
  private final BukkitScheduler bukkitScheduler;
  private final File pluginFolder;
  private final File playerFolder;
  private final TaskManager taskManager;

  private <T> void syncConsumerCall(Consumer<T> consumer, T instance) {
    synchronized (bukkitScheduler) {
      bukkitScheduler.runTask(plugin, () -> consumer.accept(instance));
    }
  }

  public void loadDataAsync(UUID playerID, Consumer<JsonObject> dataConsumer) {
    CompletableFuture.supplyAsync(() -> new File(playerFolder, playerID.toString() + ".json"))
        .thenApply(file -> {
          Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
          JsonObject jsonObject = new JsonObject();
          try {
            jsonObject = gson.fromJson(new FileReader(file), JsonObject.class);
          } catch (FileNotFoundException ignored) {
          }
          return jsonObject;
        }).thenAccept(data -> this.syncConsumerCall(dataConsumer, data));
  }

  public void saveDataAsync(UUID playerID, JsonObject data) {
    CompletableFuture.supplyAsync(() -> new File(playerFolder, playerID.toString() + ".json"))
        .thenAccept(file -> {
          Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
          try {
            OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(file));
            osw.write(gson.toJson(data));
            osw.close();
          } catch (IOException e) {
            e.printStackTrace();
          }
        });
  }

}
