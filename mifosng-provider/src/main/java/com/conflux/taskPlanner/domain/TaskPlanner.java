package com.conflux.taskPlanner.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.joda.time.LocalDate;
import org.mifosplatform.infrastructure.codes.domain.CodeValue;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.service.DateUtils;
import org.mifosplatform.organisation.office.domain.Office;
import org.mifosplatform.useradministration.domain.AppUser;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.stereotype.Component;

import com.conflux.taskPlanner.api.TaskPlannerApiConstants;

@Entity
@Component
@Table(name = "ct_task_planner")
public class TaskPlanner extends AbstractPersistable<Long> {
	
	@Column(name = "type",  nullable = false)
    private CodeValue type; 
    
    @Column(name= "status", nullable = true)
    private Integer status;
    
    @Temporal(TemporalType.DATE)
    @Column(name= "date")
    private Date date;
    
    @ManyToOne
    @JoinColumn(name = "office_id", nullable = false)
    private Office office;
    
    @Column(name= "entity_type", nullable = true)
    private String entitytype;
    
    @Column(name= "entity_id", nullable = true)
    private Integer entityId;
    
    @ManyToOne(optional = true)
    @JoinColumn(name = "createdby_id", nullable = true)
    private AppUser createdby;    
    
    @Column(name = "created_date", nullable = true)
    @Temporal(TemporalType.DATE)
    private Date createdDate;
    
    @ManyToOne(optional = true)
    @JoinColumn(name = "modifiedby_id", nullable = true)
    private AppUser modifiedby; 
    
    @Column(name = "modified_date", nullable = true)
    @Temporal(TemporalType.DATE)
    private Date modifiedDate;
    
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "taskPlanner", orphanRemoval = true)
    private  List<TaskStaffMapping> taskStaffMapping ;  
    
    protected TaskPlanner() {
		
	}

public static TaskPlanner createNew(final CodeValue type,final AppUser currentUser, final Office office,            
           final JsonCommand command) {
	   List<TaskStaffMapping>taskStaff =null; 
	   TaskStaffMapping taskStaffList = new TaskStaffMapping();
	   final LocalDate date =command.localDateValueOfParameterNamed(TaskPlannerApiConstants.dateParamName);
	   
	   final Date newDate = date.toDate();
	   
	   final String entitytype =command.stringValueOfParameterNamed(TaskPlannerApiConstants.entityTypeParamName);
       final Integer entityId =  command.integerValueOfParameterNamed(TaskPlannerApiConstants.entityTypeId);
	   //final String[] staff = command.arrayValueOfParameterNamed(TaskPlannerApiConstants.staffParamName);
       LocalDate createdDate = DateUtils.getLocalDate;
       if (command.hasParameter(TaskPlannerApiConstants.createdDateParamName)) {
    	   createdDate = command.localDateValueOfParameterNamed(TaskPlannerApiConstants.createdDateParamName);
       }
       TaskPlannerStatus status = TaskPlannerStatus.ACTIVE;
       
       
       return new  TaskPlanner(type,status,newDate,office,entitytype,entityId,currentUser,newDate);
       
      }





public TaskPlanner(CodeValue type, TaskPlannerStatus status, Date date, Office office,
		String entitytype, Integer entityId, AppUser createdby, Date createdDate) {
	super();
	this.type = type;
	this.status = status.getValue();
	this.date = date;
	this.office = office;
	this.entitytype = entitytype;
	this.entityId = entityId;
	this.createdby = createdby;
	this.createdDate = createdDate;
}




 }
