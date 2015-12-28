package com.conflux.taskPlanner.data;

import java.util.Collection;
import java.util.List;

import org.joda.time.LocalDate;
import org.mifosplatform.infrastructure.codes.data.CodeValueData;
import org.mifosplatform.infrastructure.core.data.EnumOptionData;
import org.mifosplatform.organisation.staff.data.StaffData;

public class TaskPlannerData {
	private final Long id;
	private final Long type;
	private final EnumOptionData status;
	private final LocalDate date;
	private final Long officeId;
	private final String entityType;
	private final Long entityId;
	
	//template
	private final Collection<StaffData> staffOptions;
	private final List<CodeValueData> typeOptions;

	
	
	
	public TaskPlannerData(Long id, Long type, EnumOptionData status,
			LocalDate date, Long officeId, String entityType, Long entityId,
			Collection<StaffData> staffOptions, List<CodeValueData> typeOptions) {
		super();
		this.id = id;
		this.type = type;
		this.status = status;
		this.date = date;
		this.officeId = officeId;
		this.entityType = entityType;
		this.entityId = entityId;
		this.staffOptions = staffOptions;
		this.typeOptions = typeOptions;
	}




	public static TaskPlannerData template(final Collection<StaffData> staffOptions,final List<CodeValueData> typeOptions){
		final Long id=null;
		final Long type=null;
		final EnumOptionData status =null;
		final LocalDate date =null;
		final Long officeId = null;
		final String entityType = null;
		final Long entityId = null;
		return new TaskPlannerData(id,type,status,date,officeId,entityType,entityId,staffOptions,typeOptions);
		
	}
}
