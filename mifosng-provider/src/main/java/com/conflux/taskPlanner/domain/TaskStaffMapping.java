package com.conflux.taskPlanner.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.stereotype.Component;

@Entity
@Component
@Table(name = "ct_task_staff_mapping")
public class TaskStaffMapping extends AbstractPersistable<Long> {

	@ManyToOne(optional = false)
	@JoinColumn(name = "task_id")
	private TaskPlanner taskPlanner;

	@Column(name = "staff_id")
	private Long staff;

	public TaskStaffMapping(TaskPlanner taskPlanner, Long staff) {
		super();
		this.taskPlanner = taskPlanner;
		this.staff = staff;
	}

	public TaskPlanner getTaskPlanner() {
		return taskPlanner;
	}

	public void setTaskPlanner(TaskPlanner taskPlanner) {
		this.taskPlanner = taskPlanner;
	}

	public Long getStaff() {
		return staff;
	}

	public void setStaff(Long staff) {
		this.staff = staff;
	}

	protected TaskStaffMapping() {
		// super();
	}

}
