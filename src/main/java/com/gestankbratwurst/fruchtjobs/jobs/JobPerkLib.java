package com.gestankbratwurst.fruchtjobs.jobs;

import com.gestankbratwurst.fruchtjobs.FruchtJobs;
import com.google.common.base.Stopwatch;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FruchtJobs and was created at the 28.03.2020
 *
 * FruchtJobs can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class JobPerkLib {

  private JobPerkLib() {

  }

  private static final Map<JobType, Int2ObjectMap<JobPerkType[]>> PERK_MAP = new Object2ObjectOpenHashMap<>();

  protected static void init(FruchtJobs plugin) {
    Stopwatch sw = Stopwatch.createStarted();
    Map<JobType, Int2ObjectMap<List<JobPerkType>>> preMap = new Object2ObjectOpenHashMap<>();
    for (JobType jobType : JobType.values()) {
      preMap.put(jobType, new Int2ObjectOpenHashMap<>());
      PERK_MAP.put(jobType, new Int2ObjectOpenHashMap<>());
    }
    for (JobPerkType perkType : JobPerkType.values()) {
      Int2ObjectMap<List<JobPerkType>> levelPerks = preMap.get(perkType.getJobType());
      if (levelPerks.containsKey(perkType.getLevel())) {
        levelPerks.get(perkType.getLevel()).add(perkType);
      } else {
        levelPerks.put(perkType.getLevel(), new ArrayList<>(Collections.singleton(perkType)));
      }
    }
    for (JobType jobType : JobType.values()) {
      Int2ObjectMap<List<JobPerkType>> listMap = preMap.get(jobType);
      for (Int2ObjectMap.Entry<List<JobPerkType>> entry : listMap.int2ObjectEntrySet()) {
        // NPE
        PERK_MAP.get(jobType).put(entry.getIntKey(), entry.getValue().toArray(new JobPerkType[0]));
      }
    }
    sw.stop();
    double millis = sw.elapsed(TimeUnit.MICROSECONDS) / 1000D;
    millis = ((int) (millis * 10)) / 10D;
    plugin.getLogger().info("Initialized JobPerkLib [" + millis + "ms]");
  }

  protected static List<JobPerkType> getAllJobPerks(JobType type) {
    List<JobPerkType> perkTypeList = new ArrayList<>();
    for (JobPerkType[] perkArray : PERK_MAP.get(type).values()) {
      perkTypeList.addAll(Arrays.asList(perkArray));
    }
    return perkTypeList;
  }

  public static Int2ObjectMap<JobPerkType[]> getJobPerkMap(JobType jobType) {
    return PERK_MAP.get(jobType);
  }

}
