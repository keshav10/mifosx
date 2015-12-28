package com.conflux.taskPlanner.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.stereotype.Component;

@Entity
@Component
@Table(name = "ct_client_attendence")
public class ClientAttendence  extends AbstractPersistable<Long>{
	
	@ManyToOne
	@Column(name = "task_id",  nullable = false)
	private Long taskId ;

	@ManyToOne
	@Column(name = "client_id", nullable = false)
    private Long clientId;
	
	@Column(name ="attendence_type",nullable = false)
	private Integer attendenceType;

	private ClientAttendence(Long taskId, Long clientId, Integer attendenceType) {
		super();
		this.taskId = taskId;
		this.clientId = clientId;
		this.attendenceType = attendenceType;
	}

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public Long getClientId() {
		return clientId;
	}

	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}

	public Integer getAttendenceType() {
		return attendenceType;
	}

	public void setAttendenceType(Integer attendenceType) {
		this.attendenceType = attendenceType;
	}

	protected ClientAttendence() {
		
	}
	

}