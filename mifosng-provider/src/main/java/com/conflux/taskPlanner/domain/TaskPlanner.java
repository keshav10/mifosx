package com.conflux.taskPlanner.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
import org.mifosplatform.portfolio.client.api.ClientApiConstants;
import org.mifosplatform.portfolio.note.domain.Note;
import org.mifosplatform.useradministration.domain.AppUser;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.stereotype.Component;

import com.conflux.taskPlanner.api.TaskPlannerApiConstants;

@Entity
@Component
@Table(name = "ct_task_planner")
public class TaskPlanner extends AbstractPersistable<Long> {
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "type", nullable = false)
	private CodeValue type;

	@Column(name = "status", nullable = true)
	private Integer status;

	@Temporal(TemporalType.DATE)
	@Column(name = "date")
	private Date date;

	@ManyToOne
	@JoinColumn(name = "office_id", nullable = false)
	private Office office;

	@Column(name = "entity_type", nullable = true)
	private String entitytype;

	@Column(name = "entity_id", nullable = true)
	private Long entityId;

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
	@ManyToMany(cascade = CascadeType.ALL, mappedBy = "taskPlanner")
	private List<TaskStaffMapping> taskStaffMapping = new ArrayList<>();
	
	@LazyCollection(LazyCollectionOption.FALSE)
	@ManyToMany(cascade = CascadeType.ALL, mappedBy = "taskPlanner")
	private List<Note> note = new ArrayList<>();
	
	@LazyCollection(LazyCollectionOption.FALSE)
	@ManyToMany(cascade = CascadeType.ALL, mappedBy = "taskPlanner")
	private List<ClientAttendence> clientAttendence = new ArrayList<>();
	
	@LazyCollection(LazyCollectionOption.FALSE)
	@ManyToMany(cascade = CascadeType.ALL, mappedBy = "taskPlanner")
	private List<StaffAttendence> staffAttendence = new ArrayList<>();
	

	public TaskPlanner() {
	}
	
	public LocalDate getmodifieddate() {
        LocalDate modifiedDate = null;
        if (this.date != null) {
        	modifiedDate = LocalDate.fromDateFields(this.date);
        }
        return modifiedDate;
    }
	public void udateModifiedDate( Date modifiedDate){
		this.modifiedDate=modifiedDate;
	}
	public void updateModifiedBy( AppUser modifiedby){
		this.modifiedby=modifiedby;
	}
	public void updateStatus( Integer status){
		this.status=status;
	}

	public List<TaskStaffMapping> getTaskStaffMapping() {
		return taskStaffMapping;
	}

	public void updateTaskStaffMapping(List<TaskStaffMapping> taskStaffMapping) {
		this.taskStaffMapping = taskStaffMapping;
	}
	public void updatePlanedDate(final Date date) {
		this.date=date;
	}
	public void updateNote(List<Note> note) {
		this.note=note;
	}
	public void updateclientAttendence(List<ClientAttendence> clientAttendence) {
		this.clientAttendence = clientAttendence;
	}
	public void updatestaffAttendence(List<StaffAttendence> staffAttendence) {
		this.staffAttendence = staffAttendence;
	}
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public CodeValue getType() {
		return type;
	}

	public void setType(CodeValue type) {
		this.type = type;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}

	public String getEntitytype() {
		return entitytype;
	}

	public void setEntitytype(String entitytype) {
		this.entitytype = entitytype;
	}

	public Long getEntityId() {
		return entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	public AppUser getCreatedby() {
		return createdby;
	}

	public void setCreatedby(AppUser createdby) {
		this.createdby = createdby;
	}

	public AppUser getModifiedby() {
		return modifiedby;
	}

	public void setModifiedby(AppUser modifiedby) {
		this.modifiedby = modifiedby;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public List<Note> getNote() {
		return note;
	}

	public void setNote(List<Note> note) {
		this.note = note;
	}

	public void setTaskStaffMapping(List<TaskStaffMapping> taskStaffMapping) {
		this.taskStaffMapping = taskStaffMapping;
	}

	public TaskPlanner(CodeValue type, Integer status, Date date,
			Office office, String entitytype, Long entityId, AppUser createdby,
			Date createdDate, AppUser modifiedby, Date modifiedDate) {
		this.type = type;
		this.status = status;
		this.date = date;
		this.office = office;
		this.entitytype = entitytype;
		this.entityId = entityId;
		this.createdby = createdby;
		this.createdDate = createdDate;
		this.modifiedby = modifiedby;
		this.modifiedDate = modifiedDate;
	}

	public TaskPlanner(CodeValue type, TaskPlannerStatus status, Date date,
			Office office, String entitytype, Long entityId, AppUser createdby,
			Date createdDate, List<TaskStaffMapping> taskStaffMapping,List<Note>note) {

		this.type = type;
		this.status = status.getValue();
		this.date = date;
		this.office = office;
		this.entitytype = entitytype;
		this.entityId = entityId;
		this.createdby = createdby;
		this.createdDate = createdDate;
		this.taskStaffMapping = taskStaffMapping;
		this.note = note;

	}
 
	
	private TaskPlanner(CodeValue type, Integer status, Date date,
			Office office, String entitytype, Long entityId, AppUser createdby,
			Date createdDate, AppUser modifiedby, Date modifiedDate,
			List<TaskStaffMapping> taskStaffMapping, List<Note> note,
			List<ClientAttendence> clientAttendence,
			List<StaffAttendence> staffAttendence) {
		super();
		this.type = type;
		this.status = status;
		this.date = date;
		this.office = office;
		this.entitytype = entitytype;
		this.entityId = entityId;
		this.createdby = createdby;
		this.createdDate = createdDate;
		this.modifiedby = modifiedby;
		this.modifiedDate = modifiedDate;
		this.taskStaffMapping = taskStaffMapping;
		this.note = note;
		this.clientAttendence = clientAttendence;
		this.staffAttendence = staffAttendence;
	}

	private TaskPlanner(CodeValue type, Integer status, Date date,
			Office office, String entitytype, Long entityId, AppUser createdby,
			Date createdDate, AppUser modifiedby, Date modifiedDate,
			List<TaskStaffMapping> taskStaffMapping, List<Note> note
			) {		
		this.type = type;
		this.status = status;
		this.date = date;
		this.office = office;
		this.entitytype = entitytype;
		this.entityId = entityId;
		this.createdby = createdby;
		this.createdDate = createdDate;
		this.modifiedby = modifiedby;
		this.modifiedDate = modifiedDate;
		this.taskStaffMapping = taskStaffMapping;
		this.note = note;
		//this.clientAttendence = clientAttendence;
		//this.staffAttendence = staffAttendence;
	}

	public static TaskPlanner createNew(final CodeValue type,
			final TaskPlannerStatus status, final Office office,
			final Long entityId, final String entityType, final Date date,
			final AppUser currentUser,Date createdDate) {
		

		return new TaskPlanner(type,status.getValue(),date,
				office,entityType,entityId, currentUser,
				createdDate, null, null);

	}
	
	
	
	
}


