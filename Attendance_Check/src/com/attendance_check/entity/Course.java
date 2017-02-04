package com.attendance_check.entity;

public class Course {

	private String coursename;
	private String courseid;
	private String time_period;
	private boolean attendance;
	private String attendance_rate;
	
	public Course(){
		
	}
	
	public void setCourseid(String courseid){
		this.courseid = courseid;
	}
	
	public String getCourseid(){
		return this.courseid;
	}
	
	public void setCoursename(String coursename){
		this.coursename = coursename;
	}
	
	public String getCoursename(){
		return coursename;
	}
	
	public void setAttendance_rate(String attendance_rate){
		this.attendance_rate = attendance_rate;
	}
	
	public String getAttendance_rate(){
		return attendance_rate;
	}
}
