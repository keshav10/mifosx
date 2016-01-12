package com.conflux.taskPlanner.handler;

import org.mifosplatform.commands.annotation.CommandType;
import org.mifosplatform.commands.handler.NewCommandSourceHandler;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.conflux.taskPlanner.service.TaskPlannerWritePlatformService;

@Service
@CommandType(entity = "TASKPLAN", action = "COMPLETE")
public class CompleteTaskPlannerHandler  implements NewCommandSourceHandler{
	
	private final TaskPlannerWritePlatformService taskPlannerWritePlatformService;
	   
	@Autowired
	public CompleteTaskPlannerHandler (final TaskPlannerWritePlatformService taskPlannerWritePlatformService){
		this.taskPlannerWritePlatformService=taskPlannerWritePlatformService;
	}
	@Transactional
    @Override
    public CommandProcessingResult processCommand(final JsonCommand command) {
        return this.taskPlannerWritePlatformService.completeTaskPlan(command.entityId(),command);

	}

}
