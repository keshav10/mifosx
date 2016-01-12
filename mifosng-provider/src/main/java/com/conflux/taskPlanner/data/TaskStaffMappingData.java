package com.conflux.taskPlanner.data;

public class TaskStaffMappingData {

	private final Long id;
	private final Long taskId;
	private final Long staffId;

	private TaskStaffMappingData(Long id, Long taskId, Long staffId) {
		super();
		this.id = id;
		this.taskId = taskId;
		this.staffId = staffId;
	}

	public Long getId() {
		return id;
	}

	public Long getTaskId() {
		return taskId;
	}

	public Long getStaffId() {
		return staffId;
	}

}
