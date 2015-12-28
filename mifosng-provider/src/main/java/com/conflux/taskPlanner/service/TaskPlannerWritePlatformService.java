package com.conflux.taskPlanner.service;

import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;

public interface TaskPlannerWritePlatformService {
	CommandProcessingResult createTaskPlan(JsonCommand command);

}
