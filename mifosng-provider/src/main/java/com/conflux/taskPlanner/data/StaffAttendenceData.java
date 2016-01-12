package com.conflux.taskPlanner.data;

import org.mifosplatform.infrastructure.core.data.EnumOptionData;

public class StaffAttendenceData {
	@SuppressWarnings("unused")
	private final Long id;
	@SuppressWarnings("unused")
	private final Long taskId;
	@SuppressWarnings("unused")
	private final Long staffId;
	@SuppressWarnings("unused")
	private final EnumOptionData attendanceType;
	
	

	public StaffAttendenceData(Long id, Long taskId, Long staffId,
			EnumOptionData attendanceType) {
		
		this.id = id;
		this.taskId = taskId;
		this.staffId = staffId;
		this.attendanceType = attendanceType;
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

	public EnumOptionData getAttendanceType() {
		return attendanceType;
	}


}
