package com.attendance_check.controller;


import com.attendance_check.networkrequest.TeacherCheckStudentRequest;
import com.attendence_check.controller.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class TeacherCheckStudent extends Activity {

	private TeacherCheckStudentRequest teachercheck;
	private String url = "http://192.168.1.144:8080/FingerprintAtt/teacher/course/student";
	private String courseid;
	private String userId;
	private TextView newsType;
	private ListView checkrecordlist;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle= new Bundle();
		bundle=this.getIntent().getExtras();//
		courseid=bundle.getString("courseid");			
		userId=bundle.getString("userId");
		setContentView(R.layout.activity_check);
		newsType=(TextView)findViewById(R.id.attendancerecord);
		checkrecordlist=(ListView)findViewById(R.id.checkrecordlist);
		teachercheck = new TeacherCheckStudentRequest(checkrecordlist, newsType, courseid, TeacherCheckStudent.this);
		teachercheck.execute(url);
	    newsType.setText("È«²¿¼ÇÂ¼");
		checkrecordlist.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				
				TextView courseidtext=(TextView)findViewById(R.id.ID);
				String studentid = courseidtext.getText().toString();
				Intent intent = new Intent();
				Bundle bundle= new Bundle();
				bundle.putString("courseid", courseid);
				bundle.putString("studentId", studentid);
				bundle.putString("identity", "Teacher");
				intent.putExtras(bundle);
				intent.setClass(TeacherCheckStudent.this, Details.class);
				startActivity(intent);
			}
			
		});
	}



}
