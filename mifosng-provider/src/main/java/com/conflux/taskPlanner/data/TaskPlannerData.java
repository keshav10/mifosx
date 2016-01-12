package com.conflux.taskPlanner.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.joda.time.LocalDate;
import org.mifosplatform.infrastructure.codes.data.CodeValueData;
import org.mifosplatform.infrastructure.codes.domain.CodeValue;
import org.mifosplatform.infrastructure.core.data.EnumOptionData;
import org.mifosplatform.organisation.staff.data.StaffData;
import org.mifosplatform.portfolio.interestratechart.data.InterestIncentiveData;
import org.mifosplatform.portfolio.note.data.NoteData;

public class TaskPlannerData {
	private final Long id;
	private final CodeValueData type;
	private final EnumOptionData status;
	private final LocalDate date;
	private final Long officeId;
	private final String entityType;
	private final Long entityId;
	private final TaskPlannerTimelineData taskPlannerTimelineData;
	private final String notes;

	private  List<ClientAttendenceData> clientAttendences;	
	private  List<StaffAttendenceData> staffAttendenceData;

	// template
	private final Collection<StaffData> staffOptions;
	private final List<CodeValueData> typeOptions;
	

	private TaskPlannerData(Long id, CodeValueData type, EnumOptionData status,
			LocalDate date, Long officeId, String entityType, Long entityId,
			TaskPlannerTimelineData taskPlannerTimelineData,			
			Collection<StaffData> staffOptions, List<CodeValueData> typeOptions,
			List<ClientAttendenceData> clientAttendences,String notes,
			List<StaffAttendenceData> staffAttendenceData) {
		
		this.id = id;
		this.type = type;
		this.status = status;
		this.date = date;
		this.officeId = officeId;
		this.entityType = entityType;
		this.entityId = entityId;
		this.taskPlannerTimelineData = taskPlannerTimelineData;
		this.staffOptions = staffOptions;
		this.typeOptions = typeOptions;
		this.clientAttendences = clientAttendences;
		this.notes =notes;
		this.staffAttendenceData = staffAttendenceData;
	}
	
	
	public static TaskPlannerData template(
			final Collection<StaffData> staffOptions,
			final List<CodeValueData> typeOptions) {
		final Long id = null;
		final CodeValueData type = null;
		final EnumOptionData status = null;
		final LocalDate date = null;
		final Long officeId = null;
		final String entityType = null;
		final Long entityId = null;
		final List<ClientAttendenceData> clientAttendences =null;
		final String notes =null;
		final List<StaffAttendenceData> staffAttendenceData =null;
		final TaskPlannerTimelineData taskPlannerTimelineData =null;
				return new TaskPlannerData(id, type, status, date, officeId,
				entityType, entityId,taskPlannerTimelineData, staffOptions, typeOptions,clientAttendences,notes,staffAttendenceData);

	}
	public void addClientAttendence(final ClientAttendenceData clientAttendenceData) {
        if (this.clientAttendences == null) {
            this.clientAttendences = new ArrayList<ClientAttendenceData>();
        }

        this.clientAttendences.add(clientAttendenceData);
    }
	public void addStaffAttendence(final StaffAttendenceData staffAttendenceData) {
        if (this.staffAttendenceData == null) {
            this.staffAttendenceData = new ArrayList<StaffAttendenceData>();
        }

        this.staffAttendenceData.add(staffAttendenceData);
    }

	public static TaskPlannerData taskData(Long id, CodeValueData type, EnumOptionData status,
			LocalDate date, Long officeId, String entityType, Long entityId,
			TaskPlannerTimelineData taskPlannerTimelineData	,String notes		
			){
		final Collection<StaffData> staffOptions=null;
		final List<CodeValueData> typeOptions =null;
		final List<StaffAttendenceData> staffAttendenceData =null;
		final List<ClientAttendenceData> clientAttendences =null;
		return new TaskPlannerData(id, type, status, date, officeId,
				entityType, entityId,taskPlannerTimelineData, staffOptions, typeOptions,clientAttendences,notes,staffAttendenceData);
	
	
	}
}
