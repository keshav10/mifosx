package com.conflux.taskPlanner.service;

import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;

public interface TaskPlannerWritePlatformService {
	CommandProcessingResult createTaskPlan(JsonCommand command);
	CommandProcessingResult updateTaskPlan(final Long taskId,JsonCommand command);
	CommandProcessingResult cancleTaskPlan(final Long taskId,JsonCommand command);
	CommandProcessingResult completeTaskPlan(final Long taskId,JsonCommand command);

	

}
