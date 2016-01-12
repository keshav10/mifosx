package com.conflux.taskPlanner.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.LocalDate;
import org.mifosplatform.infrastructure.codes.domain.CodeValue;
import org.mifosplatform.infrastructure.codes.domain.CodeValueRepository;
import org.mifosplatform.infrastructure.codes.domain.CodeValueRepositoryWrapper;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResultBuilder;
import org.mifosplatform.infrastructure.core.serialization.FromJsonHelper;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.mifosplatform.organisation.office.domain.Office;
import org.mifosplatform.organisation.office.domain.OfficeRepository;
import org.mifosplatform.organisation.office.exception.OfficeNotFoundException;
import org.mifosplatform.organisation.staff.domain.Staff;
import org.mifosplatform.organisation.staff.domain.StaffRepository;
import org.mifosplatform.portfolio.client.api.ClientApiConstants;
import org.mifosplatform.portfolio.client.domain.Client;
import org.mifosplatform.portfolio.note.domain.Note;
import org.mifosplatform.useradministration.domain.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.amazonaws.util.json.JSONObject;
import com.conflux.taskPlanner.api.TaskPlannerApiConstants;
import com.conflux.taskPlanner.data.TaskStaffMappingData;
import com.conflux.taskPlanner.domain.ClientAttendence;
import com.conflux.taskPlanner.domain.StaffAttendence;
import com.conflux.taskPlanner.domain.TaskAssembler;
import com.conflux.taskPlanner.domain.TaskPlanner;
import com.conflux.taskPlanner.domain.TaskPlannerRepository;
import com.conflux.taskPlanner.domain.TaskPlannerStatus;
import com.conflux.taskPlanner.domain.TaskStaffMapping;
import com.conflux.taskPlanner.domain.TaskStaffMappingRepository;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
@Service
public class TaskPlannerWritePlatformServiceImp implements TaskPlannerWritePlatformService {
	
	private final PlatformSecurityContext context;
	private final TaskStaffMappingRepository taskStaffMappingRepository;
	private final TaskPlannerRepository taskPlannerRepository;
	private final CodeValueRepositoryWrapper codeValueRepository;
	private final OfficeRepository officeRepository;
	private final TaskAssembler taskAssembler;
	private final StaffRepository staffRepository;
	private final FromJsonHelper fromApiJsonHelper;

    
	@Autowired
	public TaskPlannerWritePlatformServiceImp(final PlatformSecurityContext context,
			final TaskStaffMappingRepository taskStaffMappingRepository,final TaskPlannerRepository taskPlannerRepository,
			final CodeValueRepositoryWrapper codeValueRepository,
			final OfficeRepository officeRepository,
			final TaskAssembler taskAssembler,
			final StaffRepository staffRepository,
			final FromJsonHelper fromApiJsonHelper){

		    this.context=context;
		    this.taskStaffMappingRepository=taskStaffMappingRepository;
		    this.taskPlannerRepository=taskPlannerRepository;
		    this.codeValueRepository=codeValueRepository;
		    this.officeRepository=officeRepository;
		    this.taskAssembler=taskAssembler;
		    this.staffRepository=staffRepository;
		    this.fromApiJsonHelper=fromApiJsonHelper;
	}
	@Transactional
    @Override
    public CommandProcessingResult createTaskPlan(final JsonCommand command){
        final AppUser currentUser = this.context.getAuthenticatedUserIfPresent();
        final Long typeId = command.longValueOfParameterNamed(TaskPlannerApiConstants.typeParamName);
        final String staff= command.stringValueOfParameterNamed(TaskPlannerApiConstants.staffParamName); 
	    final String[] staffs = command.arrayValueOfParameterNamed("staff");
	    List<TaskStaffMapping> taskStaffMappings = new ArrayList<TaskStaffMapping>();
        final TaskPlanner newTaskPlanner = this.taskAssembler.assembleFrom(command, currentUser,typeId);

	                if (staffs != null){               	
	                	for (String staffId : staffs) {
	                		final Long staffid =Long.parseLong(staffId);
	                		final Staff staffDetails = this.staffRepository.findOne(staffid);
	                		final TaskStaffMapping taskStaffMapping = TaskStaffMapping.createNewMapping(newTaskPlanner,staffDetails);
	                		taskStaffMappings.add(taskStaffMapping);
	                	}
	                	newTaskPlanner.setTaskStaffMapping(taskStaffMappings);
	                 }
        
        
        this.taskPlannerRepository.save(newTaskPlanner);
        return new CommandProcessingResultBuilder() //
        .withCommandId(command.commandId()) // 
        .withEntityId(command.entityId())
        .withCommandId(command.commandId())
        .build();
        

}
	@Transactional
    @Override	
    public  CommandProcessingResult updateTaskPlan(final Long taskId,final JsonCommand command )	{
		Boolean isChange =false; 
	 final JsonElement element = command.parsedJson();
	 final Map<String, Object> changes =new HashMap<>();
	 final AppUser currentUser = this.context.getAuthenticatedUserIfPresent();	
	 TaskPlanner taskPlannerForUpdate = this.taskPlannerRepository.findOne(taskId);
	 ArrayList<TaskStaffMapping>taskStaffMappingData = this.taskStaffMappingRepository.findbyTaskId(taskPlannerForUpdate); 
	 List<TaskStaffMapping> taskStaffMappings = new ArrayList<TaskStaffMapping>();
	 final JsonObject object = new JsonParser().parse(command.json()).getAsJsonObject();
	 final JsonElement jsonElement=object.get("staff");
	 final JsonObject staffDetails = jsonElement.getAsJsonObject();	 
	 final String newstaffDetail=staffDetails.get("newstaffDetails").toString();
	 final String oldstaffDetails =staffDetails.get("oldstaffDetails").toString(); 
	 final String[] newStaffDetail=newstaffDetail.replace("[", "").replace("]", "").trim().split(",");
	 final String[] oldStaffDetail=oldstaffDetails.replace("[", "").replace("]", "").trim().split(","); 
	 taskPlannerForUpdate.updateModifiedBy(currentUser); 
	 taskPlannerForUpdate.udateModifiedDate(new Date());
     TaskPlannerStatus status = TaskPlannerStatus.RESEDULE;
	 taskPlannerForUpdate.updateStatus(status.getValue());
	
     //.taskPlannerRepository.saveAndFlush(taskPlannerForUpdate);

	 if (command.isChangeInLocalDateParameterNamed(TaskPlannerApiConstants.dateParamName, taskPlannerForUpdate.getmodifieddate())) {
	        final String valueAsInput = command.stringValueOfParameterNamed(TaskPlannerApiConstants.dateParamName);	        
	        final LocalDate newValue = command.localDateValueOfParameterNamed(TaskPlannerApiConstants.dateParamName);
	        final TaskPlanner newTaskPlanner  = this.taskAssembler.reseduleTask(taskPlannerForUpdate, newStaffDetail,oldStaffDetail,taskStaffMappingData,newValue.toDate());
	        changes.put(TaskPlannerApiConstants.dateParamName, newValue.toDate());
	        this.taskPlannerRepository.save(newTaskPlanner);
	    } 
	 changes.put(TaskPlannerApiConstants.statusParamName, status) ;  	
     this.taskPlannerRepository.saveAndFlush(taskPlannerForUpdate);
	return new CommandProcessingResultBuilder() //
    .withCommandId(command.commandId())
    .withGroupId(command.entityId())// 
    .with(changes)
    .build(); 
	

	
}
	@Override
	public CommandProcessingResult completeTaskPlan(Long taskId,JsonCommand command) {
		 final JsonElement element = command.parsedJson();
		 Note note=null;
		 List<Note>notes = new ArrayList<Note>();
		 final Map<String, Object> changes =new HashMap<>();
    	 TaskPlanner taskPlannerForComplete = this.taskPlannerRepository.findOne(taskId);
    	  LocalDate date1 = this.fromApiJsonHelper.extractLocalDateNamed("date", element);
		 final String noteData = this.fromApiJsonHelper.extractStringNamed("note",element);
		 if(noteData!=null){
         	note =Note.taskNote(taskPlannerForComplete,noteData);
         	notes.add(note);
         }
		final AppUser currentUser = this.context.getAuthenticatedUserIfPresent();	
		JsonArray clientAttendences =command.arrayOfParameterNamed(TaskPlannerApiConstants.clientAttendnceParamName);
		JsonArray staffAttendence  = command.arrayOfParameterNamed(TaskPlannerApiConstants.staffAttendenceParamName);
		List<ClientAttendence>clientAttendence=this.taskAssembler.clientsAttendence(taskPlannerForComplete, clientAttendences, staffAttendence);
		List<StaffAttendence>StaffAttendence=this.taskAssembler.staffAttendence(taskPlannerForComplete, staffAttendence, staffAttendence);
		taskPlannerForComplete.updateclientAttendence(clientAttendence);
		taskPlannerForComplete.updatestaffAttendence(StaffAttendence);
		TaskPlannerStatus status = TaskPlannerStatus.COMPLETED;
		taskPlannerForComplete.updateStatus(status.getValue());
		taskPlannerForComplete.updateModifiedBy(currentUser);
		taskPlannerForComplete.udateModifiedDate(new Date());		 
		taskPlannerForComplete.updatePlanedDate(date1.toDate());
		taskPlannerForComplete.updateNote(notes);
		changes.put(TaskPlannerApiConstants.statusParamName, status);
	     this.taskPlannerRepository.saveAndFlush(taskPlannerForComplete);
	     return new CommandProcessingResultBuilder() //
	     .withCommandId(command.commandId())
	     .withGroupId(command.entityId())//
	     .with(changes)
	     .build();
	}
	
	@Override
	public CommandProcessingResult cancleTaskPlan(Long taskId,JsonCommand command) {
		 final JsonElement element = command.parsedJson();
		 Note note=null;
		 List<Note>notes = new ArrayList<Note>();
		 final Map<String, Object> changes =new HashMap<>();
    	 TaskPlanner taskPlannerForComplete = this.taskPlannerRepository.findOne(taskId);
    	  LocalDate date1 = this.fromApiJsonHelper.extractLocalDateNamed("date", element);
		 final String noteData = this.fromApiJsonHelper.extractStringNamed("note",element);
		 if(noteData!=null){
         	note =Note.taskNote(taskPlannerForComplete,noteData);
         	notes.add(note);
         }
		final AppUser currentUser = this.context.getAuthenticatedUserIfPresent();	
		JsonArray clientAttendences =command.arrayOfParameterNamed(TaskPlannerApiConstants.clientAttendnceParamName);
		JsonArray staffAttendence  = command.arrayOfParameterNamed(TaskPlannerApiConstants.staffAttendenceParamName);
		List<ClientAttendence>clientAttendence=this.taskAssembler.clientsAttendence(taskPlannerForComplete, clientAttendences, staffAttendence);
		List<StaffAttendence>StaffAttendence=this.taskAssembler.staffAttendence(taskPlannerForComplete, staffAttendence, staffAttendence);
		taskPlannerForComplete.updateclientAttendence(clientAttendence);
		taskPlannerForComplete.updatestaffAttendence(StaffAttendence);
		TaskPlannerStatus status = TaskPlannerStatus.CLOSE;
		taskPlannerForComplete.updateStatus(status.getValue());
		taskPlannerForComplete.updateModifiedBy(currentUser);
		taskPlannerForComplete.udateModifiedDate(new Date());		 
		taskPlannerForComplete.updatePlanedDate(date1.toDate());
		taskPlannerForComplete.updateNote(notes);
		changes.put(TaskPlannerApiConstants.statusParamName, status);
	     this.taskPlannerRepository.saveAndFlush(taskPlannerForComplete);
	     return new CommandProcessingResultBuilder() //
	     .withCommandId(command.commandId())
	     .withGroupId(command.entityId())//
	     .with(changes)
	     .build();
	}
	
}
