package com.conflux.taskPlanner.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.mifosplatform.organisation.staff.domain.Staff;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.stereotype.Component;

@Entity
@Component
@Table(name = "ct_task_staff_mapping")
public class TaskStaffMapping extends AbstractPersistable<Long> {

	@ManyToOne (cascade = CascadeType.ALL, optional = false)
	@JoinColumn(name = "task_id")
	private TaskPlanner taskPlanner;

	
	@ManyToOne //(optional = false)
	@JoinColumn(name = "staff_id")
	private Staff staff;

	public TaskStaffMapping(TaskPlanner taskPlanner, Staff staff) {
		this.taskPlanner = taskPlanner;
		this.staff = staff;
	}
	
	public TaskStaffMapping(Staff staff) {
		this.staff = staff;
	}

	public TaskPlanner getTaskPlanner() {
		return taskPlanner;
	}

	public void setTaskPlanner(TaskPlanner taskPlanner) {
		this.taskPlanner = taskPlanner;
	}

	public Staff getStaff() {
		return staff;
	}

	public void setStaff(Staff staff) {
		this.staff = staff;
	}

	protected TaskStaffMapping() {
		// super();
	}
	
	 public static TaskStaffMapping createNewMapping(final TaskPlanner taskPlanner,final Staff staff) {
	        return new TaskStaffMapping(taskPlanner,staff);
	    }


}
