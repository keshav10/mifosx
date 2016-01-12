package com.conflux.taskPlanner.data;

import org.joda.time.LocalDate;

public class TaskPlannerTimelineData {

	private final LocalDate createdDate;
	private final String creataedByUsername;
	private final String modifiedByUsername;
	private final LocalDate modifedDate;

	public TaskPlannerTimelineData(LocalDate createdDate,
			String creataedByUsername, String modifiedByUsername,
			LocalDate modifedDate) {

		this.createdDate = createdDate;
		this.creataedByUsername = creataedByUsername;
		this.modifiedByUsername = modifiedByUsername;
		this.modifedDate = modifedDate;
	}

}
