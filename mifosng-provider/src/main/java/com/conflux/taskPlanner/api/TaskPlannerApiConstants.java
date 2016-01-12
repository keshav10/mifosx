package com.conflux.taskPlanner.api;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.Arrays;
import java.util.HashSet;
import org.joda.time.LocalDate;
import org.mifosplatform.infrastructure.codes.data.CodeValueData;
import org.mifosplatform.infrastructure.core.data.EnumOptionData;
import org.mifosplatform.organisation.staff.data.StaffData;

public class TaskPlannerApiConstants {
	public static final String TASKPLANNER_RESOURCE_NAME = "task";
	public static final String TASKPLANNER_GROUP_TYPE ="Group Task Types";
	public static final String TASKPLANNER_CLIENT_TYPE ="Client Task Types"; 
	
	//template relate response
	public static final String staffOptionsParamName ="staffOptions";
	public static final String typeOptionsParamName  ="typeOptions" ;
	// response parameters
	public static final String idParamName = "id";
	public static final String typeParamName="type";
	public static final String statusParamName ="status";
	public static final String dateParamName ="date";
	public static final String modifiedDateParamName="modifiedDate";
	public static final String dateFormatParamName ="dateFormat";
	public static final String officeIdParamName="officeId";
	public static final String entityTypeParamName="entityType";
	public static final String entityTypeId ="entityId" ;
	//request Parameters
	public static final String staffParamName="staff";
	public static final String newStaffDetailParamName="newStaffDetail";
	public static final String removedStaffDetailParamName="removedStaffDetail";
    public static final String typeIdParamName = "typeId";
    public static final String createdDateParamName="createdDate";
    public static final String clientAttendnceParamName="clientAttendence";
    public static final String staffAttendenceParamName="staffAttendence";
    

	
	
	 public static final Set<String> TASKPLANNER_REQUEST_DATA_PARAMETERS = new HashSet<>(Arrays.asList(idParamName, typeParamName,
			    statusParamName, dateParamName, officeIdParamName, entityTypeParamName, entityTypeId, staffParamName
			    ));

	
	 public static final Set<String> TASKPLANNER_RESPONSE_DATA_PARAMETERS = new HashSet<>(Arrays.asList(idParamName, typeParamName,
			    statusParamName, dateParamName, officeIdParamName, entityTypeParamName, entityTypeId, staffOptionsParamName,
			    typeOptionsParamName));


}
