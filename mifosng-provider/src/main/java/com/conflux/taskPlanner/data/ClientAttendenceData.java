package com.conflux.taskPlanner.data;

import org.mifosplatform.infrastructure.core.data.EnumOptionData;

public class ClientAttendenceData {
	@SuppressWarnings("unused")
	private final Long id;
	@SuppressWarnings("unused")
	private final Long taskId;
	@SuppressWarnings("unused")
	private final Long clientId;
	@SuppressWarnings("unused")
	private final EnumOptionData attendanceType;

	public ClientAttendenceData(Long id, Long taskId, Long clientId,
			EnumOptionData attendanceType) {
		
		this.id = id;
		this.taskId = taskId;
		this.clientId = clientId;
		this.attendanceType = attendanceType;
	}

	public Long getId() {
		return id;
	}

	public Long getTaskId() {
		return taskId;
	}

	public Long getClientId() {
		return clientId;
	}

	public EnumOptionData getAttendanceType() {
		return attendanceType;
	}
	

}
