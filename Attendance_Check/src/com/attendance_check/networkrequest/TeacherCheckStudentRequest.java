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


public class TeacherCheckStudentRequest extends AsyncTask<String, String, String>{

	private static final String TAG = "tian!";
    HashMap<Integer, Double> map;
    private List<Map<String,Object>>listitems;
	private SimpleAdapter simpleadapter;
	private ListView listview;    
	private TextView newsType;
    private TeacherCheckStudent check;
    private String[] rate= new String[100];
	private String[] ID= new String[100];
	private String[] subject= new String[100];
	private String[] mydetail = new String[100];
	private String[] mystate= new String[100];
	private String[] mysubject= new String[100];
	private String ExitLoginState="";
	private String getusername[]=new String[6];
	private String getstateinfo[]=new String[6];
	private Student student[] = new Student[100];
    private String teacherid;
    private String courseid;
	private int count = 0;
   
    
    public TeacherCheckStudentRequest (ListView listview, TextView newsType, String courseid, TeacherCheckStudent check){
        this.listview = listview;
        this.newsType = newsType;
        this.courseid = courseid;
        this.check = check;
   }
    
    protected void onPreExecute() {  
        Log.i(TAG, "onPreExecute() called");  
        //textView.setText("loading...");  
    }  
      
    //doInBackground方法内部执行后台任务,不可在此方法内修改UI  
    protected String doInBackground(String... params) {  
        Log.i(TAG, "doInBackground(Params... params) called");  
        
        HttpClient httpClient = new DefaultHttpClient();
        List<NameValuePair> requestParams = new ArrayList<NameValuePair>();
        //System.out.println("显示一下参数！！！： "+userid+" "+password+" ");
        requestParams.add(new BasicNameValuePair("courseId", courseid));
        //requestParams.add(new BasicNameValuePair("密码", password));
        params[0] = params[0]+"?courseId="+courseid;
		
        try {   
	        //第二步：创建代表请求的对象,参数是访问的服务器地址
//		    HttpPost request = new HttpPost(params[0]);     
//			HttpResponse httpResponse; 
//		    HttpEntity entity = new UrlEncodedFormEntity(requestParams, "UTF-8");     
//		    request.setEntity(entity);  
//	        httpResponse = httpClient.execute(request);
        	HttpGet request = new HttpGet(params[0]);     
		    HttpResponse httpResponse=new DefaultHttpClient().execute(request);  
        if (httpResponse.getStatusLine().getStatusCode() == 200) {
            //第五步：从相应对象当中取出数据，放到entity当中
        	HttpEntity httpentity = httpResponse.getEntity();

            String response = EntityUtils.toString(httpentity, "utf-8");//将entity当中的数据转换为字符串
            //创建一个JSON对象
            //JSONObject responseObjec = new JSONObject(response.toString());
            try {
            	count = 0;
				JSONObject jo = new JSONObject(response.toString());
				JSONArray jan = (JSONArray) jo.get("studentList");
				for(int p=0;jan.get(p)!=null;p++){
					count++;
					student[p] = new Student();
					JSONObject json = (JSONObject) jan.get(p); 
					rate[p] = json.getString("checkRate");
					student[p].setStu_name(json.getString("studentName"));      //先写死了，
					student[p].setStu_id(json.getString("studentId"));
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
       
    	count=3;
		student[0] = new Student();
		rate[0] = "100%";
		student[0].setStu_name("张三");  
		student[1] = new Student();
		rate[1] = "90%";
		student[1].setStu_name("李四");
		student[2] = new Student();
		rate[2] = "95%";
		student[2].setStu_name("王五");

    	
    	if(progresses[0].equals("Update_UI"))
    	{
    		
    		listitems= new ArrayList<Map<String,Object>>();
			for(int i=0;i<count;i++)     //实际上应是10的循环
			{
				Map<String,Object>listitem= new HashMap<String,Object>();
				listitem.put("Student", student[i].getStu_name());
				listitem.put("Detail", rate[i]);
				listitem.put("ID", student[i].getStu_id());
				listitems.add(listitem);
			}
			simpleadapter= new SimpleAdapter(check,listitems,
					R.layout.attendance_state_item,new String[] {"Student","Detail","ID"},
					new int[] {R.id.subject,R.id.checkrate,R.id.ID});
			
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
