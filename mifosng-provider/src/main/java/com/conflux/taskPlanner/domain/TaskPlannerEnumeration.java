package com.conflux.taskPlanner.domain;



import org.mifosplatform.infrastructure.core.data.EnumOptionData;

public class TaskPlannerEnumeration {
	
	 public static EnumOptionData status(final Integer statusId) {
	        return status(TaskPlannerStatus.fromInt(statusId));
	    }

	    public static EnumOptionData status(final TaskPlannerStatus status) {
	        EnumOptionData optionData = new EnumOptionData(TaskPlannerStatus.PLANED.getValue().longValue(), TaskPlannerStatus.PLANED.getCode(),
	                "Planed");
	        switch (status) {
	            case PLANED:
	                optionData = new EnumOptionData(TaskPlannerStatus.PLANED.getValue().longValue(), TaskPlannerStatus.PLANED.getCode(), "Planned");
	            break;
	            case COMPLETED:
	                optionData = new EnumOptionData(TaskPlannerStatus.COMPLETED.getValue().longValue(), TaskPlannerStatus.COMPLETED.getCode(), "Completed");
	            break;
	            case CLOSE:
	                optionData = new EnumOptionData(TaskPlannerStatus.CLOSE.getValue().longValue(), TaskPlannerStatus.CLOSE.getCode(), "Failed");
	            break;
	            
	                  }

	        return optionData;
	    }

		

	   
	   


}
