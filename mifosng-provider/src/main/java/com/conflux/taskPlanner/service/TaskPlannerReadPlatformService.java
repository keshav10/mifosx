package com.conflux.taskPlanner.service;


import java.util.List;

import com.conflux.taskPlanner.data.TaskPlannerData;
import com.conflux.taskPlanner.data.TaskPlannerDetailsData;

public interface TaskPlannerReadPlatformService {
	
	TaskPlannerData retrieveTemplate(Long groupId);
	
	TaskPlannerData retriveClientTaskPlannerTemplate(Long clientId);
	
	List<TaskPlannerData> retriveGroupTaskByGroupIdAndTaskType(Long groupId,Long taskType);
	

}
