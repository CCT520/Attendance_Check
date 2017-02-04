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
	private String week[] = new String[]{"��һ��","�ڶ���","������","���Ĵ�","�����","������",
			"���ߴ�","�ڰ˴�","�ھŴ�","��ʮ��","��ʮһ��","��ʮ����","��ʮ����","��ʮ�Ĵ�","��ʮ���","��ʮ����",
			"��ʮ�ߴ�","��ʮ�˴�","��ʮ�Ŵ�","�ڶ�ʮ��","�ڶ�ʮһ��","�ڶ�ʮ����","�ڶ�ʮ����","�ڶ�ʮ�Ĵ�","�ڶ�ʮ���",
			"�ڶ�ʮ����","�ڶ�ʮ�ߴ�","�ڶ�ʮ�˴�","�ڶ�ʮ�Ŵ�","����ʮ��","����ʮһ��","����ʮ����"};
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
	        System.out.println("��ʾһ�²������� "+courseId);
	        requestParams.add(new BasicNameValuePair("studentId", student_id));
	        requestParams.add(new BasicNameValuePair("courseId", courseId));
	        arg0[0] = arg0[0]+"?studentId="+student_id+"&courseId="+courseId;
			System.out.println(arg0[0]);
	        try {   
		        //�ڶ�����������������Ķ���,�����Ƿ��ʵķ�������ַ
	        	HttpGet request = new HttpGet(arg0[0]);     
			    HttpResponse httpResponse=new DefaultHttpClient().execute(request);  
//			    HttpPost request = new HttpPost(arg0[0]);     
//				HttpResponse httpResponse; 
//			    HttpEntity entity = new UrlEncodedFormEntity(requestParams, "UTF-8");     
//			    request.setEntity(entity);  
//		        httpResponse = httpClient.execute(request);
	        if (httpResponse.getStatusLine().getStatusCode() == 200) {
	            //���岽������Ӧ������ȡ�����ݣ��ŵ�entity����
	        	HttpEntity httpentity = httpResponse.getEntity();

	            String response = EntityUtils.toString(httpentity, "utf-8");//��entity���е�����ת��Ϊ�ַ���
	            //����һ��JSON����
	            System.out.println("��ӡ���"+response.toString());
	            try {
					JSONObject jo = new JSONObject(response.toString());
					JSONArray jan = (JSONArray) jo.get("attendenceList");
					for(int p=0;jan.get(p)!=null;p++){
						JSONObject json = (JSONObject) jan.get(p); 
						check_in[p] = json.getString("checkIn");
						check_out[p] = json.getString("checkOut");
						if(json.getString("state").equals("0"))
							state[p] = "����";
						else if(json.getString("state").equals("1"))
							state[p] = "�쳣";
						else if(json.getString("state").equals("2"))
							state[p] = "����";
						else if(json.getString("state").equals("3"))
							state[p] = "����";
						else if(json.getString("state").equals("4"))
							state[p] = "���";
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
//		state[0] = "ȱ��";
//		check_in[0] = "δ֪";
//		state[1] = "�ٵ�";
//		check_in[1] = "������";
		publishProgress("Update_UI");
		return "connection success!!";
	}
	
	protected void onProgressUpdate(String... progresses) {  
		
		if(progresses[0].equals("Update_UI"))
		{
			listitems= new ArrayList<Map<String,Object>>();
			for(int i=0;i<32;i++)     //ʵ����Ӧ��10��ѭ��
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
