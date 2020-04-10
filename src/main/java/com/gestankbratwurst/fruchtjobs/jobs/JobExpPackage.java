package com.gestankbratwurst.fruchtjobs.jobs;

import lombok.AccessLevel;
import lombok.Getter;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FruchtJobs and was created at the 07.04.2020
 *
 * FruchtJobs can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class JobExpPackage {

  protected static JobExpPackage of(JobType jobType, int baseExp) {
    return new JobExpPackage(jobType, baseExp);
  }

  protected JobExpPackage(JobType jobType, int baseExp) {
    this.jobType = jobType;
    this.baseExp = baseExp;
  }

  @Getter(AccessLevel.PROTECTED)
  private final JobType jobType;
  @Getter(AccessLevel.PROTECTED)
  private final int baseExp;

}
