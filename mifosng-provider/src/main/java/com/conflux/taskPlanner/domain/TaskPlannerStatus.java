package com.conflux.taskPlanner.domain;

public enum TaskPlannerStatus {
	INVALID(0, "TaskPlannerStatus.invalid"),
    ACTIVE(300, "TaskPlannerStatus.active"); //

    private final Integer value;
    private final String code;

    public static TaskPlannerStatus fromInt(final Integer statusValue) {
    	TaskPlannerStatus enumeration = TaskPlannerStatus.INVALID;
    	switch (statusValue) {
    	 case 300:
             enumeration = TaskPlannerStatus.ACTIVE;
         break;
    	}
    	return enumeration;
    }

	private TaskPlannerStatus(Integer value, String code) {
		this.value = value;
		this.code = code;
	}

	public Integer getValue() {
		return value;
	}

	public String getCode() {
		return code;
	}
	 public boolean isActive() {
	        return this.value.equals(TaskPlannerStatus.ACTIVE.getValue());
	    }
    
}
