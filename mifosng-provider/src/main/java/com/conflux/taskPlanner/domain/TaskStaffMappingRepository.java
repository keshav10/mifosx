package com.conflux.taskPlanner.domain;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.conflux.taskPlanner.data.TaskStaffMappingData;

public interface TaskStaffMappingRepository extends JpaRepository<TaskStaffMapping, Long>, JpaSpecificationExecutor<TaskStaffMapping>{
    @Query("from TaskStaffMapping taskStaffMapping  where taskStaffMapping.taskPlanner = :taskPlannerForUpdate")
	ArrayList<TaskStaffMapping> findbyTaskId( @Param("taskPlannerForUpdate")  TaskPlanner taskPlannerForUpdate);
}
