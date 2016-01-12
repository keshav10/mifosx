package com.conflux.taskPlanner.domain;

public enum TaskPlannerStatus {
	PLANED(100, "TaskPlannerStatus.planed"),
    COMPLETED(300, "TaskPlannerStatus.completed"),
	FAILED(200,"TaskPlannerStatus.failed"),
	CLOSE(400,"TaskPlannerStatus.close"),
	RESEDULE(500,"TaskPlannerStatus.resedule");
	
	//

    private final Integer value;
    private final String code;

    public static TaskPlannerStatus fromInt(final Integer statusValue) {
    	TaskPlannerStatus enumeration = TaskPlannerStatus.PLANED;
    	switch (statusValue) {
    	 case 300:
             enumeration = TaskPlannerStatus.COMPLETED;
         break;
    	 case 100:
             enumeration = TaskPlannerStatus.PLANED;
         break;
    	 case 200:
             enumeration = TaskPlannerStatus.FAILED;
         break;
    	 case 400:
             enumeration = TaskPlannerStatus.CLOSE;
         break;
    	 case 500:
             enumeration = TaskPlannerStatus.RESEDULE;
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
	        return this.value.equals(TaskPlannerStatus.COMPLETED.getValue());
	    }
	 public boolean isPlanned() {
	        return this.value.equals(TaskPlannerStatus.PLANED.getValue());
	    }
	 public boolean isFailede() {
	        return this.value.equals(TaskPlannerStatus.FAILED.getValue());
	    }
	 public boolean isClose() {
	        return this.value.equals(TaskPlannerStatus.CLOSE.getValue());
	    }
	 public boolean isResedule() {
	        return this.value.equals(TaskPlannerStatus.RESEDULE.getValue());
	    }
    
}
