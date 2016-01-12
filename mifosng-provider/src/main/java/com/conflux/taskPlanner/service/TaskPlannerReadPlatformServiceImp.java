package com.conflux.taskPlanner.service;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.joda.time.LocalDate;
import org.mifosplatform.infrastructure.codes.data.CodeValueData;
import org.mifosplatform.infrastructure.codes.service.CodeValueReadPlatformService;
import org.mifosplatform.infrastructure.core.data.EnumOptionData;
import org.mifosplatform.infrastructure.core.domain.JdbcSupport;
import org.mifosplatform.infrastructure.core.service.RoutingDataSource;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.mifosplatform.organisation.office.data.OfficeData;
import org.mifosplatform.organisation.office.service.OfficeReadPlatformService;
import org.mifosplatform.organisation.staff.data.StaffData;
import org.mifosplatform.organisation.staff.service.StaffReadPlatformService;
import org.mifosplatform.portfolio.accountdetails.data.LoanAccountSummaryData;
import org.mifosplatform.portfolio.client.api.ClientApiConstants;
import org.mifosplatform.portfolio.client.data.ClientData;
import org.mifosplatform.portfolio.client.data.ClientTimelineData;
import org.mifosplatform.portfolio.client.domain.Client;
import org.mifosplatform.portfolio.client.domain.ClientEnumerations;
import org.mifosplatform.portfolio.client.domain.ClientRepository;
import org.mifosplatform.portfolio.common.service.CommonEnumerations;
import org.mifosplatform.portfolio.group.data.GroupGeneralData;
import org.mifosplatform.portfolio.group.domain.Group;
import org.mifosplatform.portfolio.group.domain.GroupRepository;
import org.mifosplatform.portfolio.group.service.GroupReadPlatformServiceImpl;
import org.mifosplatform.portfolio.interestratechart.data.InterestIncentiveData;
import org.mifosplatform.portfolio.interestratechart.data.InterestRateChartSlabData;
import org.mifosplatform.portfolio.interestratechart.incentive.InterestIncentiveAttributeName;
import org.mifosplatform.portfolio.interestratechart.service.InterestIncentivesEnumerations;
import org.mifosplatform.portfolio.loanaccount.data.LoanStatusEnumData;
import org.mifosplatform.portfolio.loanproduct.service.LoanEnumerations;
import org.mifosplatform.portfolio.meeting.attendance.service.AttendanceEnumerations;
import org.mifosplatform.portfolio.savings.data.SavingsProductData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.conflux.taskPlanner.api.TaskPlannerApiConstants;
import com.conflux.taskPlanner.data.ClientAttendenceData;
import com.conflux.taskPlanner.data.StaffAttendenceData;
import com.conflux.taskPlanner.data.TaskPlannerData;
import com.conflux.taskPlanner.data.TaskPlannerDetailsData;
import com.conflux.taskPlanner.data.TaskPlannerTimelineData;
import com.conflux.taskPlanner.domain.TaskPlannerEnumeration;
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
    private final TaskPlannerExtractor taskPlannerExtractor = new TaskPlannerExtractor();

    
    
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
	@Override
	public List<TaskPlannerData> retriveGroupTaskByGroupIdAndTaskType( final Long groupId,final Long taskType){
		   this.context.authenticatedUser();
	        final String sql = "select " + this.taskPlannerExtractor.schema() ;
	        return this.jdbcTemplate.query(sql, this.taskPlannerExtractor, new Object[] { groupId,taskType });

	}
	
	
	private static final class TaskPlannerExtractor implements ResultSetExtractor<ArrayList<TaskPlannerData>> {

		 TaskDataMapper  taskDataMapper = new  TaskDataMapper();
        ClientAttendenceDataMapper clientAttendenceDataMapper = new ClientAttendenceDataMapper();
        StaffAttendenceDataMapper staffAttendenceDataMapper = new StaffAttendenceDataMapper();

        private final String schemaSql;

        public String schema() {
            return this.schemaSql;
        }

        private TaskPlannerExtractor() {
            this.schemaSql =taskDataMapper.TaskSummarySchema();
        /*final StringBuilder sqlBuilder = new StringBuilder("tp.id as tpid, tp.type as type, tp.status as status,map.firstname as createdBy,mp.firstname as updatedBy, ")
        
                .append(" tp.date as date ,tp.office_id as officeId,tp.entity_type as entityType,tp.entity_id as entityId, ")
                .append(" tp.created_date as createdDate,tp.modified_date as modifiedDate ,cv.code_value as typeValue , ").append(
                "ca.id as clid ,ca.task_id as taskId,ca.client_id as clientId,ca.attendence_type as attendenceType, " )
                .append("sa.id as said ,sa.task_id as taskId,sa.staff_id as staffId,sa.attendence_type as attendenceType, ")
                .append("n.note as note")
                .append( "from ct_task_planner tp  ")
                .append(" left join ct_client_attendence ca on tp.id =ca.task_id ")
                .append("left join ct_staff_attendence sa on sa.task_id=tp.id ")
                .append("left join m_note n on n.task_id = tp.id ")
                .append( "left join m_appuser map on map.id = tp.createdby_id ")
                .append("left join m_code_value cv on cv.id = tp.type ")
                .append(" left join m_appuser mp on mp.id = tp.modifiedby_id" );
        this. schemaSql =sqlBuilder.toString();                    
*/
	}
        @Override
        public ArrayList<TaskPlannerData> extractData(ResultSet rs) throws SQLException, DataAccessException {

            ArrayList<TaskPlannerData> taskPlannerList = new ArrayList<>();

            TaskPlannerData taskPlannerData = null;
            Long taskPlannerDataId = null;
            Long clientAttendenceDataId=null;
            Long staffAttendenceDatId =null;
            int tpIndex = 0;
            int caIndex = 0;
            int saIndex = 0;
            ArrayList<Long> clientsId =new ArrayList<>();
            ArrayList<Long> staffsId  = new ArrayList<>();
            int clientsIdIndex =0;
            int staffIdIndex=0;
            Boolean isfirstclientAttendence = false; 
            Boolean isfirststaffAttendence =false;
            Boolean isinsert=true;
            Boolean isinserts=true;
            // first row or when taskId changes
            while (rs.next()) {
                Long taskId = rs.getLong("tpid");
                if (taskPlannerData == null || (taskPlannerDataId != null && !taskPlannerDataId.equals(taskId))) {
                	taskPlannerDataId = taskId;
                	taskPlannerData= taskDataMapper.mapRow(rs, tpIndex ++);
                	taskPlannerList.add(taskPlannerData);
                	isfirstclientAttendence =true;
                	clientsId.clear();
                	staffsId.clear();
                	clientsIdIndex=0;
                }
                Long clientId =rs.getLong("clientId");               
                final ClientAttendenceData clientAttendenceData = clientAttendenceDataMapper.mapRow(rs, caIndex++);
                 if(taskPlannerDataId == clientAttendenceData.getTaskId()){
                	 if(isfirstclientAttendence){
                		 taskPlannerData.addClientAttendence(clientAttendenceData);
                		 clientsId.add(clientAttendenceData.getClientId());
                     	isfirstclientAttendence = false;
                	 }
                	 else{
                		 for(Long ciId :clientsId)
                		 {
                			 if(ciId == clientId)
                			 {
                				 isinsert = false;
                			 }
                		 }
                    if(isinsert){
                    	taskPlannerData.addClientAttendence(clientAttendenceData);
                    	clientsId.add(clientAttendenceData.getClientId());
                     	isfirstclientAttendence = false;
                	 }                 	
                }}
                Long staffId= rs.getLong("staffId");
                final StaffAttendenceData staffAttendenceData = staffAttendenceDataMapper.mapRow(rs, saIndex++);
                if(taskPlannerDataId == staffAttendenceData.getTaskId()){
               	 if(isfirststaffAttendence){
               		 taskPlannerData.addStaffAttendence(staffAttendenceData);
               		staffsId.add(staffAttendenceData.getStaffId());
                    	isfirststaffAttendence = false;
               	 }
               	 else{
               		 for(Long sid:staffsId)
               		 {
               			 if(sid==staffId)
               			 {
               				 isinserts=false;
               			 }
               		 }
                   if(isinserts){
                   	taskPlannerData.addStaffAttendence(staffAttendenceData);
                   	staffsId.add(staffAttendenceData.getStaffId());
                    	isfirststaffAttendence = false;
               	 }                 	
               }}

                	//taskPlannerData.addStaffAttendence(staffAttendenceData);
                }
           
        return taskPlannerList;
    }

	private static final class StaffAttendenceDataMapper implements RowMapper<StaffAttendenceData> {

        @Override
        public StaffAttendenceData mapRow(ResultSet rs, @SuppressWarnings("unused") int rowNum) throws SQLException {
        	 final Long id = JdbcSupport.getLong(rs, "said");
             final Long taskId = JdbcSupport.getLong(rs,"taskId");
             final Long staffId =JdbcSupport.getLong(rs,"staffId");
             final Integer attendanceTypeId = JdbcSupport.getInteger(rs,"saattendenceType");
             EnumOptionData attendanceType = null;
             if(attendanceTypeId!=null){
              attendanceType = AttendanceEnumerations.attendanceType(attendanceTypeId);
             }
             return new StaffAttendenceData(id,taskId,staffId,attendanceType);
            
        }

    }
	 private static final class ClientAttendenceDataMapper implements RowMapper<ClientAttendenceData> {
	    	
	    	@Override
	        public ClientAttendenceData mapRow(final ResultSet rs, @SuppressWarnings("unused") final int rowNum) throws SQLException {
	    		
	    		 final Long id = JdbcSupport.getLong(rs, "caid");
	             final Long taskId = JdbcSupport.getLong(rs,"taskId");
	             final Long clientId = JdbcSupport.getLong(rs,"clientId");
	             final Integer attendanceTypeId = JdbcSupport.getInteger(rs,"caattendenceType");
	             EnumOptionData attendanceType = null;
	             if(attendanceTypeId!=null){
	              attendanceType = AttendanceEnumerations.attendanceType(attendanceTypeId);
	             }
	            return new ClientAttendenceData(id,taskId,clientId,attendanceType);
	    	}  
	} 
	
    
    public static final class TaskDataMapper implements RowMapper<TaskPlannerData> {
    	public String TaskSummarySchema() {

                                
    		 final StringBuilder attendenceSummary = new StringBuilder(" ct.id as tpid, ct.type as type, ct.status as status,map.firstname as createdBy,mp.firstname as updatedBy,")             
                  .append(" ct.date as date ,ct.office_id as officeId,ct.entity_type as entityType,ct.entity_id as entityId, ") 
                   .append("ct.created_date as createdDate,ct.modified_date as modifiedDate ,cv.code_value as typeValue, ")
                   .append(" ca.id as caid ,ca.task_id as taskId,ca.client_id as clientId,ca.attendence_type as caattendenceType, ")
                   .append(" sa.id as said ,sa.task_id as taskId,sa.staff_id as staffId,sa.attendence_type as saattendenceType, ")
                    .append(" n.note as note ")
                    .append(" from ct_task_planner ct ")
                    .append(" left join ct_client_attendence ca on ca.task_id=ct.id ")
                    .append(" left join ct_staff_attendence  sa on sa.task_id=ca.task_id ")
                    .append(" left join m_note n on n.task_id=sa.task_id  ")
                     .append("left join m_appuser map on map.id = ct.createdby_id ")
                    .append(" left join m_code_value cv on cv.id = ct.type ")
                     .append("left join m_appuser mp on mp.id = ct.modifiedby_id ")
    		         .append("where ct.entity_id =? and ct.type =?  order by ct.id ");

            return attendenceSummary.toString();                    

}
    	@Override
        public TaskPlannerData mapRow(final ResultSet rs, @SuppressWarnings("unused") final int rowNum) throws SQLException {
    		
    		 final Long id = JdbcSupport.getLong(rs, "tpid");
    		 final Integer statusEnum = JdbcSupport.getInteger(rs, "status");
             final EnumOptionData status = TaskPlannerEnumeration.status(statusEnum);
             final Long officeId = JdbcSupport.getLong(rs, "officeId");
             final LocalDate date =JdbcSupport.getLocalDate(rs, "date");
             final Long entityId =JdbcSupport.getLong(rs, "entityId");
             final String entityType =rs.getString("entityType");
             final Long typeId = JdbcSupport.getLong(rs, "type");
             final String typeValue = rs.getString("typeValue");
             final CodeValueData type = CodeValueData.instance(typeId, typeValue);
             final String note =rs.getString("note");
             
             final LocalDate createdDate = JdbcSupport.getLocalDate(rs, "createdDate");
             final String createdByUsername = rs.getString("createdBy");
             final String updatedByFirstname = rs.getString("updatedBy");
             final LocalDate modifiedDate = JdbcSupport.getLocalDate(rs, "modifiedDate");             
             final TaskPlannerTimelineData timeline = new TaskPlannerTimelineData(createdDate,createdByUsername,updatedByFirstname,modifiedDate);
     
             
             
 	        return  TaskPlannerData.taskData(id, type, status,date,officeId,entityType,entityId, timeline,note);	            
    	}   	
    	

    	}


	}}   
