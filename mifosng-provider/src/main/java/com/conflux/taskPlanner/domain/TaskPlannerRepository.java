package com.conflux.taskPlanner.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TaskPlannerRepository  extends JpaRepository<TaskPlanner, Long>, JpaSpecificationExecutor<TaskPlanner>{

}
