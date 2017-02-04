package com.attendance_check.networkrequest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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
import com.attendance_check.controller.TeacherCheckStudent;
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


public class StudentCheckRequest extends AsyncTask<String, String, String>{

	private static final String TAG = "tian!";
    HashMap<Integer, Double> map;
    private List<Map<String,Object>>listitems;
	private SimpleAdapter simpleadapter;
	private ListView listview;    
	private TextView newsType;
    private HomePage homepage;
    private TeacherCheckStudent check;
    public Student student = new Student();
    private String[] detail= new String[100];
	private String[] state= new String[100];
	private String[] subject= new String[15];
	private String[] mydetail = new String[100];
	private String[] mystate= new String[100];
	private String[] mysubject= new String[100];
	private String ExitLoginState="";
	private String getusername[]=new String[6];
	private String getstateinfo[]=new String[6];
    private String student_id;
    private int count=0;
	
    public StudentCheckRequest (ListView listview, TextView newsType, String student_id,HomePage homepage){
         this.listview = listview;
         this.newsType = newsType;
         this.student_id = student_id;
         this.homepage = homepage;
    }
    
 
    
    protected void onPreExecute() {  
        Log.i(TAG, "onPreExecute() called");  
        //textView.setText("loading...");  
    }  
      
    //doInBackground�����ڲ�ִ�к�̨����,�����ڴ˷������޸�UI  
    protected String doInBackground(String... params) {  
        Log.i(TAG, "doInBackground(Params... params) called"); 
        
        HttpClient httpClient = new DefaultHttpClient();
        List<NameValuePair> requestParams = new ArrayList<NameValuePair>();
        //System.out.println("��ʾһ�²����������� "+userid+" "+password+" ");
        requestParams.add(new BasicNameValuePair("userId", student_id));
        params[0] = params[0]+"?userId="+student_id;
        
        try {   
	        //�ڶ�����������������Ķ���,�����Ƿ��ʵķ�������ַ
//		    HttpPost request = new HttpPost(params[0]);     
//			HttpResponse httpResponse; 
//		    HttpEntity entity = new UrlEncodedFormEntity(requestParams, "UTF-8");     
//		    request.setEntity(entity);  
//	        httpResponse = httpClient.execute(request);
        	HttpGet request = new HttpGet(params[0]);     
		    HttpResponse httpResponse=new DefaultHttpClient().execute(request);  
        if (httpResponse.getStatusLine().getStatusCode() == 200) {
            //���岽������Ӧ������ȡ�����ݣ��ŵ�entity����
        	HttpEntity httpentity = httpResponse.getEntity();

            String response = EntityUtils.toString(httpentity, "utf-8");//��entity���е�����ת��Ϊ�ַ���
            //����һ��JSON����
            //JSONObject responseObjec = new JSONObject(response.toString());
            try {
				JSONObject jo = new JSONObject(response.toString());
				JSONArray jan = (JSONArray) jo.get("courseList");
				List courselist = new ArrayList();
				count = 0;
				for(int p=0;p<jan.length();p++){
					count++;
					JSONObject json = (JSONObject) jan.get(p); 
					subject[p] = json.getString("courseName");      //��д���ˣ�
					detail[p] = json.getString("checkRate");
					Course course = new Course();
					course.setCourseid(json.getString("courseId"));
					course.setCoursename(json.getString("courseName"));
					System.out.println("������"+course.getCoursename());
					course.setAttendance_rate(json.getString("checkRate"));
					System.out.println("������"+course.getAttendance_rate());
					courselist.add(course);
				}
				student.setCourse(courselist);
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
      
    
	//onProgressUpdate�������ڸ��½�����Ϣ  
    @SuppressLint("UseSparseArrays")
	protected void onProgressUpdate(String... progresses) {  
    	if(progresses[0].equals("Update_UI"))
    	{
    		
    		listitems= new ArrayList<Map<String,Object>>();
    		List courseList = student.getCourse();
    		Iterator iterator = courseList.iterator();
    		while(iterator.hasNext()){
    			Course course = (Course) iterator.next();
				Map<String,Object>listitem= new HashMap<String,Object>();
				listitem.put("CourseName", course.getCoursename());
				listitem.put("Rate", course.getAttendance_rate());
				listitem.put("ID", course.getCourseid());
				//listitem.put("State", state[i]);
				listitems.add(listitem);
			}
			simpleadapter= new SimpleAdapter(homepage,listitems,
					R.layout.attendance_state_item,new String[] {"CourseName","Rate","ID"},
					new int[] {R.id.subject,R.id.checkrate,R.id.ID});
			
			listview.setAdapter(simpleadapter);
    	    
    		
    	}
    }  
      
    //onPostExecute����������ִ�����̨��������UI,��ʾ���  
    protected void onPostExecute(String result) {  
        Log.i(TAG, "onPostExecute(Result result) called"); 
        
    }  
      
    //onCancelled����������ȡ��ִ���е�����ʱ����UI  
    protected void onCancelled() {  
        Log.i(TAG, "onCancelled() called");  
 
    }  
} 
