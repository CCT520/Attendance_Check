package com.attendance_check.networkrequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.attendance_check.controller.HomePage;
import com.attendence_check.controller.R;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class MyTaskDayOffShow extends AsyncTask<String, String, String>{

	private String[] detail= new String[100];
	private String[] state= new String[100];
	private String[] subject= new String[100];
	private ListView list;
	private Activity activity;
	private List<Map<String,Object>>listitems;
	private SimpleAdapter simpleadapter;
	
	public  MyTaskDayOffShow(ListView list,Activity activity){
		this.list = list;
		this.activity = activity;
	}
	
	@Override
	protected String doInBackground(String... arg0) {
		// TODO Auto-generated method stub
		subject[0]="2016/01/01";subject[1]="2015/12/31";subject[2]="2015/12/30";
		detail[0]="XX课需要请假，原因：XXX";detail[1]="XX课需要请假，原因：XXX";detail[2]="XX课需要请假，原因：XXX";
		state[0]="同意";state[1]="不同意";state[2]="审批中";
		listitems= new ArrayList<Map<String,Object>>();
		publishProgress("Update_UI");
		return null;
	}
	
	protected void onProgressUpdate(String... progresses) {  
		if(progresses[0].equals("Update_UI"))
		{
			for(int i=0;i<3;i++)     //实际上应是10的循环
			{
				Map<String,Object>listitem= new HashMap<String,Object>();
				listitem.put("Subject", subject[i]);
				listitem.put("Detail", detail[i]);
				listitem.put("State", state[i]);
				listitems.add(listitem);
			}
			simpleadapter= new SimpleAdapter(activity,listitems,
					R.layout.attendance_state_item,new String[] {"Time","Detail","State"},
					new int[] {R.id.subject,R.id.checkrate});
			list.setAdapter(simpleadapter);
		}
	}

}
