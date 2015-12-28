package com.conflux.taskPlanner.service;

import com.conflux.taskPlanner.data.TaskPlannerData;

public interface TaskPlannerReadPlatformService {
	
	TaskPlannerData retrieveTemplate(Long groupId);
	
	TaskPlannerData retriveClientTaskPlannerTemplate(Long clientId);
	

}
