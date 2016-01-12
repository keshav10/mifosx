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
@Table(name = "ct_staff_attendence")
public class StaffAttendence extends AbstractPersistable<Long> {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "task_id", nullable = false)
	private TaskPlanner taskPlanner;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "staff_id", nullable = false)
	private Staff staffId;

	@Column(name = "attendence_type")
	private Integer attendanceTypeId;

	public TaskPlanner getTaskPlanner() {
		return taskPlanner;
	}

	public void setTaskPlanner(TaskPlanner taskPlanner) {
		this.taskPlanner = taskPlanner;
	}

	public StaffAttendence() {
		
	}

	public Staff getStaffId() {
		return staffId;
	}

	public void setStaffId(Staff staffId) {
		this.staffId = staffId;
	}

	public Integer getAttendanceTypeId() {
		return attendanceTypeId;
	}

	public void setAttendanceTypeId(Integer attendanceTypeId) {
		this.attendanceTypeId = attendanceTypeId;
	}

	private StaffAttendence(TaskPlanner taskPlanner, Staff staffId,
			Integer attendanceTypeId) {
		super();
		this.taskPlanner = taskPlanner;
		this.staffId = staffId;
		this.attendanceTypeId = attendanceTypeId;
	}
	public static StaffAttendence updateStaffAttendence(final TaskPlanner taskPlanner,final Staff staff,final Integer attendanceTypeId) {
        return new StaffAttendence( taskPlanner,staff,attendanceTypeId);
    }
	

}
