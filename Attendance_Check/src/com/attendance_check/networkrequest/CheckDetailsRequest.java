package com.attendance_check.networkrequest;

import java.util.ArrayList;
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
import com.attendence_check.controller.R;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class CheckDetailsRequest extends AsyncTask<String, String, String>{
	private String week[] = new String[]{"第一次","第二次","第三次","第四次","第五次","第六次",
			"第七次","第八次","第九次","第十次","第十一次","第十二次","第十三次","第十四次","第十五次","第十六次",
			"第十七次","第十八次","第十九次","第二十次","第二十一次","第二十二次","第二十三次","第二十四次","第二十五次",
			"第二十六次","第二十七次","第二十八次","第二十九次","第三十次","第三十一次","第三十二次"};
	private String state[] = new String[32];
	private String check_in[] = new String[32];
	private String check_out[] = new String[32];
	private ListView list;
	private List<Map<String,Object>>listitems;
	private SimpleAdapter simpleadapter;
	private Activity details;
	private String student_id;
	//private String course_id;
	private String courseId;
	
	public CheckDetailsRequest(ListView list, String courseId, String student_id,Activity details){
		this.list = list;
		this.courseId = courseId;
		this.student_id = student_id;
		this.details = details;
	}

	protected String doInBackground(String... arg0) {
		// TODO Auto-generated method stub
		 HttpClient httpClient = new DefaultHttpClient();
	        List<NameValuePair> requestParams = new ArrayList<NameValuePair>();
	        System.out.println("显示一下参数！！ "+courseId);
	        requestParams.add(new BasicNameValuePair("studentId", student_id));
	        requestParams.add(new BasicNameValuePair("courseId", courseId));
	        arg0[0] = arg0[0]+"?studentId="+student_id+"&courseId="+courseId;
			System.out.println(arg0[0]);
	        try {   
		        //第二步：创建代表请求的对象,参数是访问的服务器地址
	        	HttpGet request = new HttpGet(arg0[0]);     
			    HttpResponse httpResponse=new DefaultHttpClient().execute(request);  
//			    HttpPost request = new HttpPost(arg0[0]);     
//				HttpResponse httpResponse; 
//			    HttpEntity entity = new UrlEncodedFormEntity(requestParams, "UTF-8");     
//			    request.setEntity(entity);  
//		        httpResponse = httpClient.execute(request);
	        if (httpResponse.getStatusLine().getStatusCode() == 200) {
	            //第五步：从相应对象当中取出数据，放到entity当中
	        	HttpEntity httpentity = httpResponse.getEntity();

	            String response = EntityUtils.toString(httpentity, "utf-8");//将entity当中的数据转换为字符串
	            //创建一个JSON对象
	            System.out.println("打印输出"+response.toString());
	            try {
					JSONObject jo = new JSONObject(response.toString());
					JSONArray jan = (JSONArray) jo.get("attendenceList");
					for(int p=0;jan.get(p)!=null;p++){
						JSONObject json = (JSONObject) jan.get(p); 
						check_in[p] = json.getString("checkIn");
						check_out[p] = json.getString("checkOut");
						if(json.getString("state").equals("0"))
							state[p] = "正常";
						else if(json.getString("state").equals("1"))
							state[p] = "异常";
						else if(json.getString("state").equals("2"))
							state[p] = "早退";
						else if(json.getString("state").equals("3"))
							state[p] = "旷课";
						else if(json.getString("state").equals("4"))
							state[p] = "请假";
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
//		state[0] = "缺勤";
//		check_in[0] = "未知";
//		state[1] = "迟到";
//		check_in[1] = "起晚了";
		publishProgress("Update_UI");
		return "connection success!!";
	}
	
	protected void onProgressUpdate(String... progresses) {  
		
		if(progresses[0].equals("Update_UI"))
		{
			listitems= new ArrayList<Map<String,Object>>();
			for(int i=0;i<32;i++)     //实际上应是10的循环
			{
				Map<String,Object>listitem= new HashMap<String,Object>();
				listitem.put("Week", week[i]);
				listitem.put("State", state[i]);
				listitem.put("check_in", check_in[i]);
				listitem.put("check_out", check_out[i]);
				listitems.add(listitem);
			}
			simpleadapter= new SimpleAdapter(details,listitems,
					R.layout.details_item,new String[] {"Week","State","check_in","check_out"},
					new int[] {R.id.week,R.id.state,R.id.check_in,R.id.check_out});
			list.setAdapter(simpleadapter);
		}
	}

}
