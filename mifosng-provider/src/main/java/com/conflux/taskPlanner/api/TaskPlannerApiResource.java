package com.conflux.taskPlanner.api;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.mifosplatform.commands.domain.CommandWrapper;
import org.mifosplatform.commands.service.CommandWrapperBuilder;
import org.mifosplatform.commands.service.PortfolioCommandSourceWritePlatformService;
import org.mifosplatform.infrastructure.core.api.ApiRequestParameterHelper;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.mifosplatform.infrastructure.core.serialization.ApiRequestJsonSerializationSettings;
import org.mifosplatform.infrastructure.core.serialization.ToApiJsonSerializer;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.conflux.taskPlanner.data.TaskPlannerData;
import com.conflux.taskPlanner.data.TaskPlannerDetailsData;
import com.conflux.taskPlanner.domain.TaskAssembler;
import com.conflux.taskPlanner.service.TaskPlannerReadPlatformService;
import com.conflux.taskPlanner.service.TaskPlannerReadPlatformServiceImp;

@Path("/task")
@Component
@Scope("singleton")
public class TaskPlannerApiResource {
	 private final PlatformSecurityContext context;
	 private final TaskPlannerReadPlatformService taskPlannerReadPlatformService;
	 private final ToApiJsonSerializer<TaskPlannerData> toApiJsonSerializer;
	 private final ToApiJsonSerializer<List<TaskPlannerData>> totaskPlannerDetailsDataApiJsonSerializer;

	 private final TaskPlannerReadPlatformServiceImp taskPlannerReadPlatformServiceImp;
	 private final ApiRequestParameterHelper apiRequestParameterHelper;
	 private final PortfolioCommandSourceWritePlatformService commandsSourceWritePlatformService;
	 private final TaskAssembler taskAssembler;


	 @Autowired
	 public TaskPlannerApiResource(final PlatformSecurityContext context,final TaskPlannerReadPlatformService taskPlannerReadPlatformService,
			 final ToApiJsonSerializer<TaskPlannerData> toApiJsonSerializer,
			 
			 final TaskPlannerReadPlatformServiceImp taskPlannerReadPlatformServiceImp,
			 final ApiRequestParameterHelper apiRequestParameterHelper,
			 final PortfolioCommandSourceWritePlatformService commandsSourceWritePlatformService,
			 final TaskAssembler taskAssembler,
			 final ToApiJsonSerializer<List<TaskPlannerData>> totaskPlannerDetailsDataApiJsonSerializer){
		 this.context=context;
		 this.taskPlannerReadPlatformService=taskPlannerReadPlatformService;
		 this.toApiJsonSerializer=toApiJsonSerializer;
		 this.taskPlannerReadPlatformServiceImp=taskPlannerReadPlatformServiceImp;
		 this.apiRequestParameterHelper=apiRequestParameterHelper;
		 this.commandsSourceWritePlatformService=commandsSourceWritePlatformService;
		 this.taskAssembler=taskAssembler;
		 this.totaskPlannerDetailsDataApiJsonSerializer=totaskPlannerDetailsDataApiJsonSerializer;
	 }
	 
	 
	 @GET
	 @Path("group/{groupId}/template/")
	 @Consumes({ MediaType.APPLICATION_JSON })
	 @Produces({ MediaType.APPLICATION_JSON })
	 public String retriveTemplate(@Context final UriInfo uriInfo,@PathParam("groupId") final Long groupId){
		    TaskPlannerData taskPlannerData =null;
	        this.context.authenticatedUser();
	        taskPlannerData = this.taskPlannerReadPlatformServiceImp.retrieveTemplate(groupId);
	        final ApiRequestJsonSerializationSettings settings = this.apiRequestParameterHelper.process(uriInfo.getQueryParameters());
	        return this.toApiJsonSerializer.serialize(settings, taskPlannerData, TaskPlannerApiConstants.TASKPLANNER_RESPONSE_DATA_PARAMETERS);
	
	 }
	 
	 @GET
	 @Path("client/{clientId}/template")
	 @Consumes({ MediaType.APPLICATION_JSON })
	 @Produces({ MediaType.APPLICATION_JSON })
	 public String retriveClientTaskPlannerTemplate(@Context final UriInfo uriInfo,@PathParam("clientId") final Long clientId){
		    TaskPlannerData taskPlannerData =null;
		    taskPlannerData = this.taskPlannerReadPlatformServiceImp.retriveClientTaskPlannerTemplate(clientId);
		    final ApiRequestJsonSerializationSettings settings = this.apiRequestParameterHelper.process(uriInfo.getQueryParameters());
	        return this.toApiJsonSerializer.serialize(settings, taskPlannerData, TaskPlannerApiConstants.TASKPLANNER_RESPONSE_DATA_PARAMETERS);
		 } 
	 
	 @GET
	 @Path("group/{groupId}")
	 @Consumes({ MediaType.APPLICATION_JSON })
	 @Produces({ MediaType.APPLICATION_JSON })
	 public String retriveGroupTaskByGroupIdAndTaskType(@Context final UriInfo uriInfo,@PathParam("groupId") final Long groupId,@QueryParam("taskType") final Long taskType){
		 //TaskPlannerDetailsData taskPlannerDetailsData =null;
		  List<TaskPlannerData> taskPlannerDetailsData = this.taskPlannerReadPlatformServiceImp.retriveGroupTaskByGroupIdAndTaskType(groupId, taskType);
		    final ApiRequestJsonSerializationSettings settings = this.apiRequestParameterHelper.process(uriInfo.getQueryParameters());
	        final Set<String> TASK_PLANNER_DATA_PARAMETERS = new HashSet<>(Arrays.asList("taskPlanner", "clientAttendence"));
             
		    return this.totaskPlannerDetailsDataApiJsonSerializer.serialize(settings,taskPlannerDetailsData,TASK_PLANNER_DATA_PARAMETERS);
		 } 
	 
	 @POST
	 @Consumes({ MediaType.APPLICATION_JSON })
	 @Produces({ MediaType.APPLICATION_JSON })
	 public String createTask(final String jsonRequestBody) {      
	        final CommandWrapper commandRequest = new CommandWrapperBuilder().createTaskPlan().withJson(jsonRequestBody).build();
	        final CommandProcessingResult result = this.commandsSourceWritePlatformService.logCommandSource(commandRequest);
	        return this.toApiJsonSerializer.serialize(result);
	    }
	 
	 
	 @PUT
	 @Path("/{taskId}")
	 @Consumes({ MediaType.APPLICATION_JSON })
	 @Produces({ MediaType.APPLICATION_JSON })
	 public String updateTask(final String jsonRequestBody,@PathParam("taskId") final Long taskId,@QueryParam("command") final String command) {  
		//  commandRequest  =null;
          CommandProcessingResult result =null;
		 if(command.equalsIgnoreCase("resedule")){
	       final CommandWrapper  commandRequest = new CommandWrapperBuilder().reseduleTaskPlan(taskId).withJson(jsonRequestBody).build();
	       result = this.commandsSourceWritePlatformService.logCommandSource(commandRequest);

		 }
		 if(command.equalsIgnoreCase("complete")){
	         final CommandWrapper commandRequest = new CommandWrapperBuilder().completeTaskPlan(taskId).withJson(jsonRequestBody).build();
	         result = this.commandsSourceWritePlatformService.logCommandSource(commandRequest);

		 }
		 if(command.equalsIgnoreCase("cancel")){
	         final CommandWrapper commandRequest = new CommandWrapperBuilder().cancleTaskPlan(taskId).withJson(jsonRequestBody).build();
	         result = this.commandsSourceWritePlatformService.logCommandSource(commandRequest);

		 }
	        return this.toApiJsonSerializer.serialize(result);
	    }
	 
	 
	 
}
