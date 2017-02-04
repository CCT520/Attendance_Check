package com.attendance_check.networkrequest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.attendance_check.controller.Details;
import com.attendance_check.controller.HomePage;
import com.attendance_check.entity.Course;
import com.attendance_check.entity.Student;
import com.attendence_check.controller.R;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;


public class TeacherCheckCourseRequest extends AsyncTask<String, String, String>{

	private static final String TAG = "tian!";
    HashMap<Integer, Double> map;
    private List<Map<String,Object>>listitems;
	private SimpleAdapter simpleadapter;
	private ListView listview;    
	private TextView newsType;
    private HomePage homepage;
    private Student[] student = new Student[100];
    private Course[] course = new Course[10];
    private String[] checkRate = new String[10];
    private String teacherid;
    private int count;
	
    public TeacherCheckCourseRequest (ListView listview, TextView newsType, HomePage homepage, String teacherid){
         this.listview = listview;
         this.newsType = newsType;
         this.homepage = homepage;
         this.teacherid = teacherid;
    }
    protected void onPreExecute() {  
        Log.i(TAG, "onPreExecute() called");  
        //textView.setText("loading...");  
    }  
      
    //doInBackground方法内部执行后台任务,不可在此方法内修改UI  
    protected String doInBackground(String... params) {  
        HttpClient httpClient = new DefaultHttpClient();
        params[0] = params[0]+"?userId="+teacherid;
        try {   
        	HttpGet request = new HttpGet(params[0]);     
		    HttpResponse httpResponse=new DefaultHttpClient().execute(request);  
        if (httpResponse.getStatusLine().getStatusCode() == 200) {
            //第五步：从相应对象当中取出数据，放到entity当中
        	HttpEntity httpentity = httpResponse.getEntity();
            String response = EntityUtils.toString(httpentity, "utf-8");//将entity当中的数据转换为字符串
            try {
            	count = 0;
				JSONObject jo = new JSONObject(response.toString());
				JSONArray jan = (JSONArray) jo.get("courseList");
				for(int p=0;jan.get(p)!=null;p++){
					count++;
					course[p] = new Course();
					JSONObject json = (JSONObject) jan.get(p); 
					course[p].setCoursename(json.getString("courseName"));      //先写死了，
					course[p].setAttendance_rate(json.getString("checkRate"));
					course[p].setCourseid(json.getString("courseId"));
				}
            }catch(JSONException e) {
            	Log.e("TAG", e.getMessage());  
            }
        }else{
        	//password wrong
        }
        }catch (Exception e) {  
            Log.e("TAG", e.getMessage());  
        }
        publishProgress("Update_UI");
        return "connection success!!";  
    }  
      
    
	//onProgressUpdate方法用于更新进度信息  
    @SuppressLint("UseSparseArrays")
	protected void onProgressUpdate(String... progresses) {  
//        count=2;
//        course[0] = new Course();course[1] = new Course();
//    	course[0].setAttendance_rate("100%");
//    	course[0].setCoursename("Java程序设计");
//    	course[0].setCourseid("1");
//    	course[1].setAttendance_rate("95%");
//    	course[1].setCoursename("面向对象分析与设计");
//    	course[1].setCourseid("1");
    	if(progresses[0].equals("Update_UI"))
    	{
    		
    		listitems= new ArrayList<Map<String,Object>>();
			for(int i=0;i<count;i++)     //实际上应是10的循环
			{
				Map<String,Object>listitem= new HashMap<String,Object>();
				listitem.put("Course", course[i].getCoursename());
				listitem.put("Detail", course[i].getAttendance_rate());
				listitem.put("ID", course[i].getCourseid());
				listitems.add(listitem);
			}
			simpleadapter= new SimpleAdapter(homepage,listitems,
					R.layout.attendance_state_item,new String[] {"Course", "Detail","ID"},
					new int[] {R.id.subject, R.id.checkrate, R.id.ID});
			
			listview.setAdapter(simpleadapter);
    	   
    		
    	}
    }  
      
    //onPostExecute方法用于在执行完后台任务后更新UI,显示结果  
    protected void onPostExecute(String result) {  
        Log.i(TAG, "onPostExecute(Result result) called"); 
        
    }  
      
    //onCancelled方法用于在取消执行中的任务时更改UI  
    protected void onCancelled() {  
        Log.i(TAG, "onCancelled() called");  
 
    }  
} 
