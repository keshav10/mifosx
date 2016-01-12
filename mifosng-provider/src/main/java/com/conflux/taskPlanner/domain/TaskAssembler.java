package com.conflux.taskPlanner.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.LocalDate;
import org.mifosplatform.infrastructure.codes.domain.CodeValue;
import org.mifosplatform.infrastructure.codes.domain.CodeValueRepositoryWrapper;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.serialization.FromJsonHelper;
import org.mifosplatform.organisation.office.domain.Office;
import org.mifosplatform.organisation.office.domain.OfficeRepository;
import org.mifosplatform.organisation.office.exception.OfficeNotFoundException;
import org.mifosplatform.organisation.staff.domain.Staff;
import org.mifosplatform.organisation.staff.domain.StaffRepository;
import org.mifosplatform.portfolio.client.api.ClientApiConstants;
import org.mifosplatform.portfolio.note.domain.Note;
import org.mifosplatform.useradministration.domain.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.util.json.JSONArray;
import com.amazonaws.util.json.JSONObject;
import com.conflux.taskPlanner.api.TaskPlannerApiConstants;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@Service
public class TaskAssembler {
	private final FromJsonHelper fromApiJsonHelper;
	final CodeValueRepositoryWrapper codeValueRepository;
	final OfficeRepository officeRepository;
	final StaffRepository staffRepository;

	@Autowired
	public TaskAssembler(final FromJsonHelper fromApiJsonHelper,final CodeValueRepositoryWrapper codeValueRepository,
			final OfficeRepository officeRepository,
			final StaffRepository staffRepository) {
		this.fromApiJsonHelper = fromApiJsonHelper;
		this.codeValueRepository=codeValueRepository;
		this.officeRepository=officeRepository;
		this.staffRepository=staffRepository;
	}

	
	public TaskPlanner assembleFrom (final JsonCommand command, final AppUser currentUser ,Long typeId){
		 TaskPlanner taskPlanner =null;
		 Note note =null;
		 final JsonElement element = command.parsedJson();		 
		 final Long clientId = this.fromApiJsonHelper.extractLongNamed("clientId", element);
	     final Long entityId=this.fromApiJsonHelper.extractLongNamed("entityId", element);
	     final String entityType= this.fromApiJsonHelper.extractStringNamed("entityType", element);		 
		 final Long officeId = this.fromApiJsonHelper.extractLongNamed("officeId",element);
		 final LocalDate date1 = this.fromApiJsonHelper.extractLocalDateNamed("date", element);
		 final String noteData = this.fromApiJsonHelper.extractStringNamed("note",element);
		 Date date = date1.toDate();
		 Date createdDate = new Date();
	        final Office clientOffice = this.officeRepository.findOne(officeId);
	        if (clientOffice == null) { throw new OfficeNotFoundException(officeId); }		
			CodeValue type = null;
	        if (typeId != null) {
	        	 type = this.codeValueRepository.findOneByCodeNameAndIdWithNotFoundDetection(TaskPlannerApiConstants.TASKPLANNER_GROUP_TYPE, typeId);
	        }
	        TaskPlannerStatus status = TaskPlannerStatus.PLANED;
		    List<Note>notes = new ArrayList<Note>();

	   	    taskPlanner =  TaskPlanner.createNew(type, status, clientOffice, entityId,entityType,date,currentUser,createdDate) ;
	               
	                if(noteData!=null){
	                	note =Note.taskNote(taskPlanner,noteData);
	                	notes.add(note);
	                }
	                
	              //  TaskPlanner taskplannerForReturn = new TaskPlanner(type, status, date, clientOffice, entityType, entityId, currentUser, new Date(), taskStaffMappings,notes);
	                       
	                return taskPlanner;
	}
	
	public TaskPlanner reseduleTask (final TaskPlanner taskPlannerForUpdate, final String [] staffDetails,final String [] oldStaffDetails, List<TaskStaffMapping> taskStaffMapping,final Date date){
		Boolean ischange= false;
		 Date createdDate = new Date();
		TaskPlanner taskPlanner =null;
		 Note note =null;
		 	    List<TaskStaffMapping> taskStaffMappings = taskStaffMapping;
	    List<Note>notes = new ArrayList<Note>();
        TaskPlannerStatus status = TaskPlannerStatus.PLANED;

	    taskPlanner =  TaskPlanner.createNew(taskPlannerForUpdate.getType(), status, taskPlannerForUpdate.getOffice(), taskPlannerForUpdate.getEntityId(),taskPlannerForUpdate.getEntitytype(),taskPlannerForUpdate.getDate(),taskPlannerForUpdate.getCreatedby(),createdDate) ;
	                if (staffDetails != null && staffDetails.length>0){	                	
	                	for (String staffId : staffDetails) {
	                		if(staffId.length()>0 ){
	                		final Long staffid =Long.parseLong(staffId);
	                		 for(TaskStaffMapping taskStaffId:taskStaffMapping){
	                			 if(taskStaffId.getStaff().getId()!=staffid){
	                				 ischange =true; 
	                			 }
	                		 }
	                		
	                		 if(ischange){
	                		final Staff staff = this.staffRepository.findOne(staffid);
	                		final TaskStaffMapping newtaskStaffMapping = TaskStaffMapping.createNewMapping(taskPlanner,staff);
	                		taskStaffMappings.add(newtaskStaffMapping);
	                		 ischange =false;
	                		 }
	                	}
	                	}	
	                	for(String staffId : oldStaffDetails){
		                		final Long staffid =Long.parseLong(staffId);
		                		 for(TaskStaffMapping taskStaffId:taskStaffMappings){
		                			 if(taskStaffId.getStaff().getId()==staffid){
				                		 taskStaffMappings.remove(taskStaffId);	 
		                			 }
		                		 }
		                		
	                	}
	                 }
	                /*if(taskPlannerForUpdate.getNote()!=null){
	                	note =Note.taskNote(taskPlanner,taskPlannerForUpdate.getNote().get(0).getNote());
	                	notes.add(note);
	                }*/
	                
	                TaskPlanner taskplannerForReturn = new TaskPlanner(taskPlannerForUpdate.getType(), status, date, taskPlannerForUpdate.getOffice(), taskPlannerForUpdate.getEntitytype(), taskPlannerForUpdate.getEntityId(), taskPlannerForUpdate.getCreatedby(), taskPlannerForUpdate.getmodifieddate().toDate(),taskStaffMappings,notes);
	                       
	                return taskplannerForReturn;
	}
	
	
	public List<ClientAttendence> clientsAttendence (final TaskPlanner taskPlannerForcomplete, JsonArray clientAttendences,JsonArray staffAttendence){
		List<ClientAttendence>clientsAttendences = new ArrayList<>();
		for(int i=0;i<clientAttendences.size();i++){
        	JsonElement jsonElement =clientAttendences.get(i);
        	JsonObject clientAttendence =jsonElement.getAsJsonObject();
        	final Long clientId =clientAttendence.get("clientId").getAsLong();
        	final Integer attendence =clientAttendence.get("attendenceType").getAsInt();
    		final ClientAttendence clientAttendenceforUpdat = ClientAttendence.updateClientAttendence(taskPlannerForcomplete,clientId,attendence);
    		clientsAttendences.add(clientAttendenceforUpdat);
        }
		return clientsAttendences;
	}	
	public List<StaffAttendence> staffAttendence (final TaskPlanner taskPlannerForcomplete, JsonArray clientAttendences,JsonArray staffAttendence){
		List<StaffAttendence>staffsAttendences= new ArrayList<>();
		for(int i=0;i<staffAttendence.size();i++){
        	JsonElement jsonElement =staffAttendence.get(i);
        	JsonObject staffAttendences =jsonElement.getAsJsonObject();
        	final Long staffId =staffAttendences.get("staffId").getAsLong();
        	final Staff staff =this.staffRepository.findOne(staffId);
        	final Integer attendence =staffAttendences.get("attendenceType").getAsInt();
        	final StaffAttendence staffAttendenceForUpdate = StaffAttendence.updateStaffAttendence(taskPlannerForcomplete, staff, attendence);
        	staffsAttendences.add(staffAttendenceForUpdate);
		}
	return staffsAttendences;
	}
}
