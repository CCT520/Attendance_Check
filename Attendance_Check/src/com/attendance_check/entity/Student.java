package com.attendance_check.entity;

import java.util.ArrayList;
import java.util.List;

public class Student {

	private String stu_id = "";
	private String stu_name = "";
	private List course = new ArrayList<Course>();
	
	public Student(){
		
	}
	
	public void setStu_id(String stu_id){
		this.stu_id = stu_id;
	}
	
	public String getStu_id(){
		return stu_id;
	}
	
	public void setStu_name(String stu_name){
		this.stu_name = stu_name;
	}
	
	public String getStu_name(){
		return stu_name;
	}
	
	public void setCourse(List course){
		this.course = course;
	}
	
	public List getCourse(){
		return course;
	}
}
