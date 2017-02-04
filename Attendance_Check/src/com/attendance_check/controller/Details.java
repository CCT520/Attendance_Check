package com.attendance_check.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.attendance_check.networkrequest.CheckDetailsRequest;
import com.attendence_check.controller.R;


import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class Details extends Activity {

	private ListView list;
	private CheckDetailsRequest checkdetails;
	private String url = "http://10.206.17.174:8080/FingerprintAtt/student/recordDetail";
	private String courseid;
	private String studentId;
	private String userIdentity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle= new Bundle();
		bundle=this.getIntent().getExtras();//
		courseid=bundle.getString("courseid");	
		System.out.println("Details"+courseid);
		studentId=bundle.getString("studentId");
		userIdentity=bundle.getString("identity");
		
		if(userIdentity.equals("Teacher"))
			url = "http://192.168.1.144:8080/FingerprintAtt/teacher/course/student/recordDetail";
		else
			url = "http://192.168.1.144:8080/FingerprintAtt/student/recordDetail";
		
		setContentView(R.layout.activity_details);
		initial();
	}

	public void initial(){
		list = (ListView)findViewById(R.id.detaillist);
		checkdetails = new CheckDetailsRequest(list,courseid,studentId, Details.this);
		checkdetails.execute(url);
		
	}

}
