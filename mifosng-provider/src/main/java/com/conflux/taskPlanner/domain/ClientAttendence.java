package com.conflux.taskPlanner.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.mifosplatform.organisation.staff.domain.Staff;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.stereotype.Component;

@Entity
@Component
@Table(name = "ct_client_attendence")
public class ClientAttendence extends AbstractPersistable<Long>{
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "task_id", nullable = false)
	private TaskPlanner taskPlanner;
	
	//@ManyToOne(fetch = FetchType.LAZY)
	@Column(name = "client_id", nullable = false)
	private Long clientId;

	@Column(name ="attendence_type")
    private Integer attendanceTypeId;

	public ClientAttendence(TaskPlanner taskPlanner, Long clientId,
			Integer attendanceTypeId) {
		
		this.taskPlanner=taskPlanner;
		this.clientId = clientId;
		this.attendanceTypeId = attendanceTypeId;
	}
	

	public ClientAttendence() {
		
	}


	public static ClientAttendence updateClientAttendence(final TaskPlanner taskPlanner,final Long clientId,final Integer attendanceTypeId) {
        return new ClientAttendence( taskPlanner,clientId,attendanceTypeId);
    }
}
