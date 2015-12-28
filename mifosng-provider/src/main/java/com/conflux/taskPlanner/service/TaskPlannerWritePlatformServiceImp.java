package com.conflux.taskPlanner.service;

import org.mifosplatform.infrastructure.codes.domain.CodeValue;
import org.mifosplatform.infrastructure.codes.domain.CodeValueRepository;
import org.mifosplatform.infrastructure.codes.domain.CodeValueRepositoryWrapper;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResultBuilder;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.mifosplatform.organisation.office.domain.Office;
import org.mifosplatform.organisation.office.domain.OfficeRepository;
import org.mifosplatform.organisation.office.exception.OfficeNotFoundException;
import org.mifosplatform.portfolio.client.api.ClientApiConstants;
import org.mifosplatform.portfolio.client.domain.Client;
import org.mifosplatform.useradministration.domain.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.conflux.taskPlanner.api.TaskPlannerApiConstants;
import com.conflux.taskPlanner.domain.TaskPlanner;
import com.conflux.taskPlanner.domain.TaskPlannerRepository;
import com.conflux.taskPlanner.domain.TaskStaffMappingRepository;
@Service
public class TaskPlannerWritePlatformServiceImp implements TaskPlannerWritePlatformService {
	
	private final PlatformSecurityContext context;
	private final TaskStaffMappingRepository taskStaffMappingRepository;
	private final TaskPlannerRepository taskPlannerRepository;
	private final CodeValueRepositoryWrapper codeValueRepository;
	private final OfficeRepository officeRepository;
	
    
	@Autowired
	public TaskPlannerWritePlatformServiceImp(final PlatformSecurityContext context,
			final TaskStaffMappingRepository taskStaffMappingRepository,final TaskPlannerRepository taskPlannerRepository,
			final CodeValueRepositoryWrapper codeValueRepository,
			final OfficeRepository officeRepository){
		    this.context=context;
		    this.taskStaffMappingRepository=taskStaffMappingRepository;
		    this.taskPlannerRepository=taskPlannerRepository;
		    this.codeValueRepository=codeValueRepository;
		    this.officeRepository=officeRepository;
	}
	@Transactional
    @Override
    public CommandProcessingResult createTaskPlan(final JsonCommand command){
        final AppUser currentUser = this.context.authenticatedUser();
        final Long officeId = command.longValueOfParameterNamed(ClientApiConstants.officeIdParamName);
        final Office clientOffice = this.officeRepository.findOne(officeId);
        if (clientOffice == null) { throw new OfficeNotFoundException(officeId); }		
		CodeValue type = null;
        final Long typeId = command.longValueOfParameterNamed(TaskPlannerApiConstants.typeParamName);
        if (typeId != null) {
        	 type = this.codeValueRepository.findOneByCodeNameAndIdWithNotFoundDetection(TaskPlannerApiConstants.typeParamName, typeId);
        }
        final TaskPlanner tskPlanner = TaskPlanner.createNew(type,currentUser, clientOffice, 
                 command);
        this.taskPlannerRepository.save(tskPlanner);
        return new CommandProcessingResultBuilder() //
        .withCommandId(command.commandId()) //
        .withOfficeId(clientOffice.getId()) //
        .withClientId(tskPlanner.getId()) //      
        
        .build();
        

}
}
