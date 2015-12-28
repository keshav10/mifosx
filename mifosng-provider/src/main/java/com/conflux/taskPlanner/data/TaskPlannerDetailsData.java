package com.conflux.taskPlanner.data;

import java.util.Collection;
import java.util.List;

import org.mifosplatform.infrastructure.codes.data.CodeValueData;
import org.mifosplatform.organisation.staff.data.StaffData;

public class TaskPlannerDetailsData {

	private final Collection<TaskPlannerData> taskPlannerData;
	private final Collection<StaffData> staffOptions;
	private final List<CodeValueData> typeOptions;

	public TaskPlannerDetailsData(Collection<TaskPlannerData> taskPlannerData,
			Collection<StaffData> staffOptions, List<CodeValueData> typeOptions) {
		super();
		this.taskPlannerData = taskPlannerData;
		this.staffOptions = staffOptions;
		this.typeOptions = typeOptions;
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

}
