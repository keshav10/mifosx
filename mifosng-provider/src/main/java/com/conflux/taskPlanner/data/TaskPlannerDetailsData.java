package com.conflux.taskPlanner.data;

import java.util.Collection;
import java.util.List;

import org.mifosplatform.infrastructure.codes.data.CodeValueData;
import org.mifosplatform.organisation.staff.data.StaffData;

public class TaskPlannerDetailsData  {

	private final Collection<TaskPlannerData> taskPlannerData;
	private final Collection<StaffData> staffOptions;
	private final List<CodeValueData> typeOptions;
	private final List<ClientAttendenceData> clientAttendence;

	private TaskPlannerDetailsData(Collection<TaskPlannerData> taskPlannerData,
			Collection<StaffData> staffOptions,
			List<CodeValueData> typeOptions,
			List<ClientAttendenceData> clientAttendence) {
		
		this.taskPlannerData = taskPlannerData;
		this.staffOptions = staffOptions;
		this.typeOptions = typeOptions;
		this.clientAttendence = clientAttendence;
	}

	public Collection<TaskPlannerData> getTaskPlannerData() {
		return taskPlannerData;
	}

	public Collection<StaffData> getStaffOptions() {
		return staffOptions;
	}

	public List<CodeValueData> getTypeOptions() {
		return typeOptions;
	}

	public List<ClientAttendenceData> getClientAttendence() {
		return clientAttendence;
	}
	
	public static TaskPlannerDetailsData taskDetails(Collection<TaskPlannerData> taskPlannerData,			
			List<ClientAttendenceData> clientAttendence){
		    Collection<StaffData> staffOptions=null;
		    List<CodeValueData> typeOptions=null;
		return new TaskPlannerDetailsData( taskPlannerData,staffOptions, typeOptions,clientAttendence);			
			
			 
	}

}
