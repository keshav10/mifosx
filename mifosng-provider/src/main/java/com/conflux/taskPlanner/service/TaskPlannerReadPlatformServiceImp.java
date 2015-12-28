package com.conflux.taskPlanner.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.joda.time.LocalDate;
import org.mifosplatform.infrastructure.codes.data.CodeValueData;
import org.mifosplatform.infrastructure.codes.service.CodeValueReadPlatformService;
import org.mifosplatform.infrastructure.core.serialization.ApiRequestJsonSerializationSettings;
import org.mifosplatform.infrastructure.core.service.RoutingDataSource;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.mifosplatform.organisation.office.data.OfficeData;
import org.mifosplatform.organisation.office.service.OfficeReadPlatformService;
import org.mifosplatform.organisation.staff.data.StaffData;
import org.mifosplatform.organisation.staff.service.StaffReadPlatformService;
import org.mifosplatform.portfolio.client.api.ClientApiConstants;
import org.mifosplatform.portfolio.client.data.ClientData;
import org.mifosplatform.portfolio.client.domain.Client;
import org.mifosplatform.portfolio.client.domain.ClientRepository;
import org.mifosplatform.portfolio.group.data.GroupGeneralData;
import org.mifosplatform.portfolio.group.domain.Group;
import org.mifosplatform.portfolio.group.domain.GroupRepository;
import org.mifosplatform.portfolio.group.service.GroupReadPlatformServiceImpl;
import org.mifosplatform.portfolio.savings.data.SavingsProductData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.conflux.taskPlanner.api.TaskPlannerApiConstants;
import com.conflux.taskPlanner.data.TaskPlannerData;
import com.conflux.taskPlanner.data.TaskPlannerDetailsData;
@Service
public class TaskPlannerReadPlatformServiceImp implements TaskPlannerReadPlatformService{
	
	private final JdbcTemplate jdbcTemplate;
    private final PlatformSecurityContext context;
    private final OfficeReadPlatformService officeReadPlatformService;
    private final StaffReadPlatformService staffReadPlatformService;
    private final CodeValueReadPlatformService codeValueReadPlatformService;
    private final GroupReadPlatformServiceImpl groupReadPlatformServiceImpl;
    private final GroupRepository groupRepository;
    private final ClientRepository clientRepository;
    
    
    @Autowired
    public TaskPlannerReadPlatformServiceImp(final PlatformSecurityContext context,final OfficeReadPlatformService officeReadPlatformService,
    		final StaffReadPlatformService staffReadPlatformService,
    		final CodeValueReadPlatformService codeValueReadPlatformService,
    		final RoutingDataSource dataSource,final GroupReadPlatformServiceImpl groupReadPlatformServiceImpl,
    		final GroupRepository groupRepository,final ClientRepository clientRepository){
    	this.context=context;
    	this.codeValueReadPlatformService=codeValueReadPlatformService;
    	this.officeReadPlatformService=officeReadPlatformService;
    	this.staffReadPlatformService=staffReadPlatformService;
    	this.jdbcTemplate=new JdbcTemplate(dataSource);
    	this.groupReadPlatformServiceImpl=groupReadPlatformServiceImpl;
    	this.groupRepository =groupRepository;
    	this.clientRepository=clientRepository;
    }
    @Override
	 public TaskPlannerData retrieveTemplate(final Long groupId) {
	        this.context.authenticatedUser();
	        final Group group =groupRepository.findOne(groupId);
	        final Collection<OfficeData> offices = this.officeReadPlatformService.retrieveAllOfficesForDropdown();
	        Collection<StaffData> staffOptions = this.staffReadPlatformService.retrieveAllStaffForDropdown(group.officeId());
            Collection<GroupGeneralData> groups= this.groupReadPlatformServiceImpl.retrieveGroupsForLookup(group.officeId());    
	        final List<CodeValueData> typeOptions = new ArrayList<CodeValueData>(
	        this.codeValueReadPlatformService.retrieveCodeValuesByCode(TaskPlannerApiConstants.TASKPLANNER_GROUP_TYPE));
	           
	        return  TaskPlannerData.template(staffOptions, typeOptions);
	 }
    @Override
      public TaskPlannerData retriveClientTaskPlannerTemplate(final Long clientId){
            this.context.authenticatedUser();
            final Client client =clientRepository.findOne(clientId); 
	        Collection<StaffData> staffOptions = this.staffReadPlatformService.retrieveAllStaffForDropdown(client.officeId());
	        final List<CodeValueData> typeOptions = new ArrayList<CodeValueData>(
	    	this.codeValueReadPlatformService.retrieveCodeValuesByCode(TaskPlannerApiConstants.TASKPLANNER_CLIENT_TYPE));
	        return  TaskPlannerData.template(staffOptions, typeOptions);	            
    	
    }

}
